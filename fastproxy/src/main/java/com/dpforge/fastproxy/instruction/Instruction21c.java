package com.dpforge.fastproxy.instruction;

import com.dpforge.fastproxy.dex.DexItem;

public abstract class Instruction21c extends DexCodeInstruction {

    private int aa;
    private int op;
    private final DexItem item;

    Instruction21c(final int aa, final int op, final DexItem item) {
        this.aa = aa;
        this.op = op;
        this.item = item;
    }

    @Override
    public final int[] getByteCode() {
        return new int[]{
                word(aa, op),
                item.index
        };
    }
}
