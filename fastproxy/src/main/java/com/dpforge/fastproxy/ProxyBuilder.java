package com.dpforge.fastproxy;

import android.content.Context;
import android.os.Build;

import com.dpforge.fastproxy.dex.AccessFlags;
import com.dpforge.fastproxy.dex.DexBuilder;
import com.dpforge.fastproxy.dex.DexClassDef;
import com.dpforge.fastproxy.dex.DexCode;
import com.dpforge.fastproxy.dex.DexField;
import com.dpforge.fastproxy.dex.DexMethod;
import com.dpforge.fastproxy.dex.DexProto;
import com.dpforge.fastproxy.dex.DexType;
import com.dpforge.fastproxy.dex.writer.DexWriter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import dalvik.system.DexClassLoader;

import static com.dpforge.fastproxy.instruction.DexInstructions.agetObject;
import static com.dpforge.fastproxy.instruction.DexInstructions.aputObject;
import static com.dpforge.fastproxy.instruction.DexInstructions.const4;
import static com.dpforge.fastproxy.instruction.DexInstructions.igetObject;
import static com.dpforge.fastproxy.instruction.DexInstructions.invokeDirect;
import static com.dpforge.fastproxy.instruction.DexInstructions.invokeInterface;
import static com.dpforge.fastproxy.instruction.DexInstructions.invokeStatic;
import static com.dpforge.fastproxy.instruction.DexInstructions.iputObject;
import static com.dpforge.fastproxy.instruction.DexInstructions.moveResultObject;
import static com.dpforge.fastproxy.instruction.DexInstructions.newArray;
import static com.dpforge.fastproxy.instruction.DexInstructions.returnVoid;

public class ProxyBuilder<T> {

    private static final int DEX_VERSION = 0x35;
    private static final String PROXY_CLASS_NAME_PREFIX = "_Proxy_";

    private final DexBuilder dexBuilder = new DexBuilder(DEX_VERSION);
    private final Class<T> interfaceClass;
    private final InvocationHandler invocationHandler;

    private DexType proxyType;
    private DexClassDef.Builder proxyClassBuilder;
    private DexField handlerField;
    private DexField methodsField;
    private Method[] methods;

    private ProxyBuilder(final Class<T> interfaceClass, final InvocationHandler invocationHandler) {
        this.interfaceClass = interfaceClass;
        this.invocationHandler = invocationHandler;
    }

    public static <T> T createProxy(final Context context,
                                    final Class<T> interfaceClass,
                                    final InvocationHandler invocationHandler) {
        final ProxyBuilder<T> builder = new ProxyBuilder<>(interfaceClass, invocationHandler);
        builder.prepare();
        builder.generatedFields();
        builder.generateConstructor();
        builder.generateMethods();

        builder.proxyClassBuilder.build();
        builder.writeDex(context);
        return builder.createProxyInstance(context);
    }

    private void prepare() {
        this.proxyType = dexBuilder.addType(getProxyTypeDescription());

        final DexType objectType = dexBuilder.addType(Object.class);
        this.proxyClassBuilder = dexBuilder.addClass()
                .type(proxyType)
                .accessFlags(AccessFlags.fromValue(AccessFlags.ACC_PUBLIC))
                .superClass(objectType)
                .implementedInterface(dexBuilder.addType(interfaceClass));
    }

    private void generatedFields() {
        handlerField = dexBuilder.addField(proxyType,
                dexBuilder.addString("handler"),
                dexBuilder.addType(InvocationHandler.class));
        proxyClassBuilder.instanceField(handlerField, AccessFlags.fromValue(AccessFlags.ACC_PRIVATE));

        methodsField = dexBuilder.addField(proxyType,
                dexBuilder.addString("methods"),
                dexBuilder.addType(Method[].class));
        proxyClassBuilder.instanceField(methodsField, AccessFlags.fromValue(AccessFlags.ACC_PRIVATE));
    }

    private void generateConstructor() {
        final DexType objectType = dexBuilder.addType(Object.class);
        final DexType voidType = dexBuilder.addType("V");
        final DexMethod ctr = dexBuilder.addMethod(proxyType,
                dexBuilder.addString("<init>"),
                dexBuilder.addProto(voidType, Arrays.asList(
                        dexBuilder.addType(InvocationHandler.class),
                        dexBuilder.addType(Method[].class))));
        final DexMethod objectCtr = dexBuilder.addMethod(objectType, dexBuilder.addString("<init>"),
                dexBuilder.addProto(voidType, Collections.<DexType>emptyList()));
        proxyClassBuilder.directMethod(ctr, AccessFlags.fromValue(AccessFlags.ACC_PUBLIC, AccessFlags.ACC_CONSTRUCTOR), DexCode.newBuilder()
                .registersSize(3)
                .insSize(3)
                .outsSize(1)
                .instruction(invokeDirect(0, objectCtr))
                .instruction(iputObject(1, 0, handlerField))
                .instruction(iputObject(2, 0, methodsField))
                .instruction(returnVoid())
                .build());
    }

    private void generateMethods() {
        methods = interfaceClass.getMethods();

        for (int i = 0; i < methods.length; i++) {
            final Method method = methods[i];
            generateMethodCode(method, i);
        }
    }

    private void generateMethodCode(final Method method, final int methodIndex) {
        // TODO do it once
        final DexMethod invokeMethod = dexBuilder.addMethod(dexBuilder.addType(InvocationHandler.class),
                dexBuilder.addString("invoke"),
                dexBuilder.addProto(dexBuilder.addType(Object.class), Arrays.asList(
                        dexBuilder.addType(Object.class),
                        dexBuilder.addType(Method.class),
                        dexBuilder.addType(Object[].class)
                )));

        final int argCount = method.getParameterTypes().length;
        final DexMethod dexMethod = dexBuilder.addMethod(proxyType, dexBuilder.addString(method.getName()), getDexProto(method));

        final int rHandler = 0;
        final int rMethod = 1;
        final int rMethodArray = 2;
        final int rIndex = 3;
        final int rArgArray = 4;
        final int rArrSize = 5;
        final int rBoxedArg = 6;
        final int rThis = 7;
        final int registersSize = rThis + argCount + 1;
        final int insSize = argCount + 1;
        final DexCode.Builder builder = DexCode.newBuilder()
                .registersSize(registersSize)
                .insSize(insSize)
                .outsSize(4)
                .instruction(igetObject(rHandler, rThis, handlerField))
                .instruction(igetObject(rMethodArray, rThis, methodsField))
                .instruction(const4(rIndex, methodIndex))
                .instruction(agetObject(rMethod, rMethodArray, rIndex))
                .instruction(const4(rArrSize, method.getParameterTypes().length))
                .instruction(newArray(rArgArray, rArrSize, dexBuilder.addType(Object[].class)));

        for (int i = 0; i < method.getParameterTypes().length; i++) {
            final int rArg = registersSize - argCount + i;
            final Class<?> argType = method.getParameterTypes()[i];
            builder.instruction(const4(rIndex, i));
            if (argType.isPrimitive()) {
                builder.instruction(invokeStatic(rArg, getValueOfMethod(argType)))
                        .instruction(moveResultObject(rBoxedArg))
                        .instruction(aputObject(rBoxedArg, rArgArray, rIndex));
            } else {
                builder.instruction(aputObject(rArg, rArgArray, rIndex));
            }
        }

        builder.instruction(invokeInterface(rHandler, rThis, rMethod, rArgArray, invokeMethod))
                .instruction(returnVoid());

        proxyClassBuilder.virtualMethod(dexMethod, AccessFlags.fromValue(AccessFlags.ACC_PUBLIC), builder.build());
    }

    private DexMethod getValueOfMethod(final Class<?> argType) {
        DexType boxedClass;
        DexType primitiveClass;

        if (int.class == argType) {
            boxedClass = dexBuilder.addType(Integer.class);
            primitiveClass = dexBuilder.addType(int.class);
        } else if (boolean.class == argType) {
            boxedClass = dexBuilder.addType(Boolean.class);
            primitiveClass = dexBuilder.addType(boolean.class);
        } else if (float.class == argType) {
            boxedClass = dexBuilder.addType(Float.class);
            primitiveClass = dexBuilder.addType(float.class);
        } else {
            throw new UnsupportedOperationException("Unsupported primitive type " + argType);
        }

        return dexBuilder.addMethod(boxedClass,
                dexBuilder.addString("valueOf"),
                dexBuilder.addProto(boxedClass,
                        Collections.singletonList(primitiveClass)));
    }

    private DexProto getDexProto(final Method method) {
        final List<DexType> dexArgTypes = new ArrayList<>(method.getParameterTypes().length);
        for (Class<?> argType : method.getParameterTypes()) {
            dexArgTypes.add(dexBuilder.addType(argType));
        }
        final DexType returnDexType = dexBuilder.addType(method.getReturnType());
        return dexBuilder.addProto(returnDexType, dexArgTypes);
    }

    private void writeDex(final Context context) {
        try {
            DexWriter.write(dexBuilder.build(), getProxyDexFile(context));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private T createProxyInstance(final Context context) {
        final DexClassLoader classLoader = new DexClassLoader(getProxyDexFile(context).getAbsolutePath(),
                getCodeCacheDir(context).getAbsolutePath(),
                null,
                ProxyBuilder.class.getClassLoader());
        try {
            Class<?> proxyClass = classLoader.loadClass(getProxyClassName());
            Object proxy = proxyClass.getConstructor(InvocationHandler.class, Method[].class)
                    .newInstance(invocationHandler, methods);
            return interfaceClass.cast(proxy);
        } catch (ClassNotFoundException
                | NoSuchMethodException
                | IllegalAccessException
                | InstantiationException
                | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private String getProxyClassName() {
        final String packageName = interfaceClass.getPackage() == null
                ? ""
                : interfaceClass.getPackage().getName() + ".";
        return packageName + PROXY_CLASS_NAME_PREFIX + interfaceClass.getSimpleName();
    }

    private String getProxyTypeDescription() {
        final String packageName = interfaceClass.getPackage() == null
                ? ""
                : interfaceClass.getPackage().getName().replace('.', '/') + "/";
        final String proxyName = PROXY_CLASS_NAME_PREFIX + interfaceClass.getSimpleName() + ";";
        return "L" + packageName + proxyName;
    }

    private File getProxyDexFile(final Context context) {
        File proxyDir = new File(getCodeCacheDir(context), "proxies");
        return new File(proxyDir, interfaceClass.getName() + ".generated.dex");
    }

    private static File getCodeCacheDir(final Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getCodeCacheDir();
        }
        return new File(context.getFilesDir(), "code_cache");
    }
}
