package com.dpforge.fastproxy.dex;

public class AccessFlags {

    static final int ACC_PUBLIC = 0x1;
    static final int ACC_PRIVATE = 0x2;
    static final int ACC_PROTECTED = 0x4;
    static final int ACC_STATIC = 0x8;
    static final int ACC_FINAL = 0x10;
    static final int ACC_SYNCHRONIZED = 0x20;
    static final int ACC_VOLATILE = 0x40;
    static final int ACC_BRIDGE = 0x40;
    static final int ACC_TRANSIENT = 0x80;
    static final int ACC_VARARGS = 0x80;
    static final int ACC_NATIVE = 0x100;
    static final int ACC_INTERFACE = 0x200;
    static final int ACC_ABSTRACT = 0x400;
    static final int ACC_STRICT = 0x800;
    static final int ACC_SYNTHETIC = 0x1000;
    static final int ACC_ANNOTATION = 0x2000;
    static final int ACC_ENUM = 0x4000;
    static final int ACC_CONSTRUCTOR = 0x10000;
    static final int ACC_DECLARED_SYNCHRONIZED = 0x20000;

    public final int value;

    private AccessFlags(final int value) {
        this.value = value;
    }

    public static AccessFlags fromValue(final int... values) {
        int flags = 0;
        for (int value : values) {
            flags |= value;
        }
        return new AccessFlags(flags);
    }
}
