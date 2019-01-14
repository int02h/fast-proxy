package com.dpforge.fastproxy.instruction;

public class AGetObject extends Instruction23x {

    /**
     * @param a value register or pair; may be source or dest (8 bits)
     * @param b array register (8 bits)
     * @param c index register (8 bits)
     */
    public AGetObject(final int a, final int b, final int c) {
        super(a, b, c, 0x46);
    }
}
