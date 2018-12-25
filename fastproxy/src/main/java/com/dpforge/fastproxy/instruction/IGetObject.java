package com.dpforge.fastproxy.instruction;

import com.dpforge.fastproxy.dex.DexField;

/**
 * 54: iget-object
 */
public class IGetObject extends Instruction22c {

    public IGetObject(final int b, final int a, final DexField field) {
        super(b, a, 0x54, field);
    }
}
