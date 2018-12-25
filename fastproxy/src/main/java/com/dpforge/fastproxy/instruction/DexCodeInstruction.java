package com.dpforge.fastproxy.instruction;

public abstract class DexCodeInstruction {

    public abstract int[] getByteCode();

    static int word(final int high, final int low) {
        return ((high & 0xFF) << 8) | (low & 0xFF);
    }

    static int word(final int highHighHalf, final int highLowHalf, final int low) {
        return word(((highHighHalf & 0x0F) << 4) | (highLowHalf & 0x0F), low);
    }

    static int word(final int highHighHalf, final int highLowHalf, final int lowHighHalf, final int lowLowHalf) {
        return word(((highHighHalf & 0x0F) << 4) | (highLowHalf & 0x0F),
                ((lowHighHalf & 0x0F) << 4) | (lowLowHalf & 0x0F));
    }
}
