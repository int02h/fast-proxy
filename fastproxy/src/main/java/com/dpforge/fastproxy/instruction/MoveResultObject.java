package com.dpforge.fastproxy.instruction;

public class MoveResultObject extends Instruction11x {

    /**
     * @param a destination register (8 bits)
     */
    public MoveResultObject(final int a) {
        super(a, 0x0c);
    }
}
