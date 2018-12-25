package com.dpforge.fastproxy.instruction;

import com.dpforge.fastproxy.dex.DexString;

/**
 * 1a: const-string
 */
public class ConstString extends Instruction21c {

    public ConstString(final int a, final DexString string) {
        super(a, 0x1a, string);
    }
}
