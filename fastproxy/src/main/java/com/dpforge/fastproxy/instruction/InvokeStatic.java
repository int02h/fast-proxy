package com.dpforge.fastproxy.instruction;

import com.dpforge.fastproxy.dex.DexMethod;

public class InvokeStatic extends Instruction35c {

    /**
     * @param c argument registers (4 bits)
     * @param method method reference index
     */
    public InvokeStatic(final int c, final DexMethod method) {
        super(c, 0x71, method);
    }
}
