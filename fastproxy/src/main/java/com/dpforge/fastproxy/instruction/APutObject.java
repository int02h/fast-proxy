package com.dpforge.fastproxy.instruction;

public class APutObject extends Instruction23x {

    /**
     * @param a value register or pair; may be source or dest (8 bits)
     * @param b array register (8 bits)
     * @param c index register (8 bits)
     */
    public APutObject(final int a, final int b, final int c) {
        super(a, b, c, 0x4d);
    }
}
