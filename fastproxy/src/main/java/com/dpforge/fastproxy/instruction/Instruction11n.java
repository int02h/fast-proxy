package com.dpforge.fastproxy.instruction;

public abstract class Instruction11n extends DexCodeInstruction {

    private final int b;
    private final int a;
    private final int op;

    protected Instruction11n(final int b, final int a, final int op) {
        this.b = b;
        this.a = a;
        this.op = op;
    }

    @Override
    public int[] getByteCode() {
        return new int[]{word(b, a, op)};
    }
}
