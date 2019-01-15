package com.dpforge.fastproxy.instruction;

public abstract class Instruction11x extends DexCodeInstruction {

    private final int a;
    private final int op;

    protected Instruction11x(final int a, final int op) {
        this.a = a;
        this.op = op;
    }

    @Override
    public int[] getByteCode() {
        return new int[]{word(a, op)};
    }
}
