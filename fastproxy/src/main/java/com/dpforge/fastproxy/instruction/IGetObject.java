package com.dpforge.fastproxy.instruction;

import com.dpforge.fastproxy.dex.DexField;

/**
 * 54: iget-object
 */
public class IGetObject extends Instruction22c {

    /**
     * @param b object register (4 bits)
     * @param a value register or pair; may be source or dest (4 bits)
     * @param field instance field reference index (16 bits)
     */
    public IGetObject(final int b, final int a, final DexField field) {
        super(b, a, 0x54, field);
    }
}
