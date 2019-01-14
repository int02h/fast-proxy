package com.dpforge.fastproxy.instruction;

public abstract class Instruction23x extends DexCodeInstruction {

    private final int aa;
    private final int bb;
    private final int cc;
    private final int op;

    protected Instruction23x(final int aa, final int bb, final int cc, final int op) {
        this.aa = aa;
        this.bb = bb;
        this.cc = cc;
        this.op = op;
    }

    @Override
    public int[] getByteCode() {
        return new int[] {
                word(aa, op),
                word(cc, bb)
        };
    }
}
