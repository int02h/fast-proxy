package com.dpforge.fastproxy.instruction;

public abstract class Instruction10x extends DexCodeInstruction {

    private final int op;

    Instruction10x(final int op) {
        this.op = op;
    }

    @Override
    public final int[] getByteCode() {
        return new int[]{
                word(0x00, op)
        };
    }
}
