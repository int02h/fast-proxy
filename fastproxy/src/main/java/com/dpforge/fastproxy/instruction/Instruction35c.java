package com.dpforge.fastproxy.instruction;

import com.dpforge.fastproxy.dex.DexItem;

public abstract class Instruction35c extends DexCodeInstruction {

    private final int a;
    private final int c;
    private final int d;
    private final int e;
    private final int f;
    private final int g;
    private final int op;
    private final DexItem item;

    Instruction35c(final int c, final int op, final DexItem item) {
        this.op = op;
        this.item = item;
        this.a = 1;
        this.c = c;
        d = e = f = g = 0;
    }

    Instruction35c(final int c, final int d, final int op, final DexItem item) {
        this.op = op;
        this.item = item;
        this.a = 2;
        this.c = c;
        this.d = d;
        e = f = g = 0;
    }

    Instruction35c(final int c, final int d, final int e, final int f, final int op, final DexItem item) {
        this.op = op;
        this.item = item;
        this.a = 4;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        g = 0;
    }

    @Override
    public final int[] getByteCode() {
        return new int[]{
                word(a, g, op),
                item.index,
                word(f, e, d, c)

        };
    }
}
