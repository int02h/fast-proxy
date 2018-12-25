package com.dpforge.fastproxy.dex;

public class DexFieldDef {

    public final DexField field;

    public final AccessFlags accessFlags;

    DexFieldDef(final DexField field, final AccessFlags accessFlags) {
        this.field = field;
        this.accessFlags = accessFlags;
    }
}
