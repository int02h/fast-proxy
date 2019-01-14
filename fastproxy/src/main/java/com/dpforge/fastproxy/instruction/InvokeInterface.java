package com.dpforge.fastproxy.instruction;

import com.dpforge.fastproxy.dex.DexMethod;

/**
 * 72: invoke-interface
 */
public class InvokeInterface extends Instruction35c {

    public InvokeInterface(final int c, final int d, final DexMethod method) {
        super(c, d, 0x72, method);
    }

    public InvokeInterface(final int c, final int d, final int e, final int f, final DexMethod method) {
        super(c, d, e, f, 0x72, method);
    }
}
