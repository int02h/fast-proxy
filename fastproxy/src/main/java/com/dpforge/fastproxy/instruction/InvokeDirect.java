package com.dpforge.fastproxy.instruction;

import com.dpforge.fastproxy.dex.DexMethod;

/**
 * 70: invoke-direct
 */
public class InvokeDirect extends Instruction35c {


    public InvokeDirect(final int c, final DexMethod method) {
        super(c, 0x70, method);
    }
}
