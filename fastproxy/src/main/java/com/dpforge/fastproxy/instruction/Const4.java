package com.dpforge.fastproxy.instruction;

public class Const4 extends Instruction11n {

    /**
     * @param a destination register (4 bits)
     * @param b signed int (4 bits)
     */
    public Const4(final int a, final int b) {
        super(b, a, 0x12);
    }
}
