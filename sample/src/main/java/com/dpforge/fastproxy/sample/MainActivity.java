package com.dpforge.fastproxy.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dpforge.fastproxy.ProxyBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Foo foo = ProxyBuilder.createProxy(getApplicationContext(), Foo.class, new InvocationHandler() {
            @Override
            public Object invoke(final Object proxy, final Method method, final Object[] args) {
                dumpMethodInfo(method, args);
                return null;
            }
        });
        foo.bar();
        foo.zzz(true, "Hello World!", -5);
        foo.argName(this);
    }

    private void dumpMethodInfo(final Method method, final Object[] args) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Method '").append(method).append("' called with args: [");

        String argSeparator = "";
        for (final Object arg : args) {
            final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            for (final Annotation[] annotations : parameterAnnotations) {
                if (annotations.length > 0) {
                    builder.append('(');
                    String annotationSeparator = "";
                    for (Annotation a : annotations) {
                        if (a instanceof ArgName) {
                            builder.append(annotationSeparator).append(((ArgName) a).value());
                            annotationSeparator = " ";
                        }
                    }
                    builder.append(") ");
                }
            }
            builder.append(argSeparator).append(arg);
            argSeparator = ", ";
        }

        builder.append(']');

        Log.d("_@_", builder.toString());
    }
}
