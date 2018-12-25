package com.dpforge.fastproxy.instruction;

import com.dpforge.fastproxy.dex.DexItem;

public abstract class Instruction22c extends DexCodeInstruction {

    private final int b;
    private final int a;
    private final int op;
    private final DexItem item;

    Instruction22c(final int b, final int a, final int op, final DexItem item) {
        this.b = b;
        this.a = a;
        this.op = op;
        this.item = item;
    }

    @Override
    public final int[] getByteCode() {
        return new int[]{
                word(b, a, op),
                item.index
        };
    }
}
