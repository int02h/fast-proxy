package com.dpforge.fastproxy.instruction;

import com.dpforge.fastproxy.dex.DexType;

public class NewArray extends Instruction22c {

    /**
     * @param a        destination register (4 bits)
     * @param b        size register
     * @param itemType type index
     */
    public NewArray(final int a, final int b, final DexType itemType) {
        super(b, a, 0x23, itemType);
    }
}
