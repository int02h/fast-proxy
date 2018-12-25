package com.dpforge.fastproxy.instruction;

import com.dpforge.fastproxy.dex.DexMethod;

/**
 * 72: invoke-interface
 */
public class InvokeInterface extends Instruction35c {

    public InvokeInterface(final int c, final int d, final DexMethod method) {
        super(c, d, 0x72, method);
    }
}
