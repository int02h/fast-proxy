package com.dpforge.fastproxy.instruction;

import com.dpforge.fastproxy.dex.DexField;

/**
 * 5b: iput-object
 */
public class IPutObject extends Instruction22c {

    public IPutObject(final int b, final int a, final DexField field) {
        super(b, a, 0x5b, field);
    }
}
