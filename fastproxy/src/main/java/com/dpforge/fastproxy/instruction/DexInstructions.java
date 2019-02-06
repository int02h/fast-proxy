package com.dpforge.fastproxy.instruction;

import com.dpforge.fastproxy.dex.DexField;
import com.dpforge.fastproxy.dex.DexItem;
import com.dpforge.fastproxy.dex.DexMethod;
import com.dpforge.fastproxy.dex.DexType;

public class DexInstructions {
    private DexInstructions() {
    }

    public static DexCodeInstruction returnVoid() {
        return new Instruction10x(0x0e);
    }

    /**
     * @param a destination register (4 bits)
     * @param b signed int (4 bits)
     */
    public static DexCodeInstruction const4(final int a, final int b) {
        return new Instruction11n(a, b, 0x12);
    }

    /**
     * @param a destination register (8 bits)
     */
    public static DexCodeInstruction moveResultObject(final int a) {
        return new Instruction11x(a, 0x0c);
    }

    /**
     * @param a     value register or pair; may be source or dest (4 bits)
     * @param b     object register (4 bits)
     * @param field instance field reference index (16 bits)
     */
    public static DexCodeInstruction igetObject(final int a, final int b, final DexField field) {
        return new Instruction22c(a, b, 0x54, field);
    }

    /**
     * @param a     value register or pair; may be source or dest (4 bits)
     * @param b     object register (4 bits)
     * @param field instance field reference index (16 bits)
     */
    public static DexCodeInstruction iputObject(final int a, final int b, final DexField field) {
        return new Instruction22c(a, b, 0x5b, field);
    }

    /**
     * @param a value register or pair; may be source or dest (8 bits)
     * @param b array register (8 bits)
     * @param c index register (8 bits)
     */
    public static DexCodeInstruction agetObject(final int a, final int b, final int c) {
        return new Instruction23x(a, b, c, 0x46);
    }

    /**
     * @param a value register or pair; may be source or dest (8 bits)
     * @param b array register (8 bits)
     * @param c index register (8 bits)
     */
    public static DexCodeInstruction aputObject(final int a, final int b, final int c) {
        return new Instruction23x(a, b, c, 0x4d);
    }

    /**
     * @param a        destination register (4 bits)
     * @param b        size register
     * @param itemType type index
     */
    public static DexCodeInstruction newArray(final int a, final int b, final DexType itemType) {
        return new Instruction22c(a, b, 0x23, itemType);
    }

    /**
     * @param c      argument register (4 bits)
     * @param method method reference index (16 bits)
     */
    public static DexCodeInstruction invokeDirect(final int c, final DexMethod method) {
        return new Instruction35c(c, 0x70, method);
    }

    /**
     * @param c      argument register (4 bits)
     * @param d      argument register (4 bits)
     * @param e      argument register (4 bits)
     * @param f      argument register (4 bits)
     * @param method method reference index (16 bits)
     */
    public static DexCodeInstruction invokeInterface(final int c, final int d, final int e, final int f, final DexMethod method) {
        return new Instruction35c(c, d, e, f, 0x72, method);
    }

    /**
     * @param c      argument register (4 bits)
     * @param method method reference index (16 bits)
     */
    public static DexCodeInstruction invokeStatic(final int c, final DexMethod method) {
        return new Instruction35c(c, 0x71, method);
    }

    private static class Instruction10x extends DexCodeInstruction {

        private final int op;

        Instruction10x(final int op) {
            this.op = op;
        }

        @Override
        public final int[] getByteCode() {
            return new int[]{
                    word(0x00, op)
            };
        }
    }

    private static class Instruction11n extends DexCodeInstruction {

        private final int b;
        private final int a;
        private final int op;

        Instruction11n(final int a, final int b, final int op) {
            this.b = b;
            this.a = a;
            this.op = op;
        }

        @Override
        public int[] getByteCode() {
            return new int[]{word(b, a, op)};
        }
    }

    private static class Instruction11x extends DexCodeInstruction {

        private final int a;
        private final int op;

        Instruction11x(final int a, final int op) {
            this.a = a;
            this.op = op;
        }

        @Override
        public int[] getByteCode() {
            return new int[]{word(a, op)};
        }
    }

    private static class Instruction22c extends DexCodeInstruction {

        private final int b;
        private final int a;
        private final int op;
        private final DexItem item;

        Instruction22c(final int a, final int b, final int op, final DexItem item) {
            this.b = b;
            this.a = a;
            this.op = op;
            this.item = item;
        }

        @Override
        public final int[] getByteCode() {
            return new int[]{
                    word(b, a, op),
                    item.index
            };
        }
    }

    private static class Instruction23x extends DexCodeInstruction {

        private final int aa;
        private final int bb;
        private final int cc;
        private final int op;

        Instruction23x(final int aa, final int bb, final int cc, final int op) {
            this.aa = aa;
            this.bb = bb;
            this.cc = cc;
            this.op = op;
        }

        @Override
        public int[] getByteCode() {
            return new int[]{
                    word(aa, op),
                    word(cc, bb)
            };
        }
    }

    private static class Instruction35c extends DexCodeInstruction {

        private final int a;
        private final int c;
        private final int d;
        private final int e;
        private final int f;
        private final int g;
        private final int op;
        private final DexItem item;

        Instruction35c(final int c, final int op, final DexItem item) {
            this.op = op;
            this.item = item;
            this.a = 1;
            this.c = c;
            d = e = f = g = 0;
        }

        Instruction35c(final int c, final int d, final int e, final int f, final int op, final DexItem item) {
            this.op = op;
            this.item = item;
            this.a = 4;
            this.c = c;
            this.d = d;
            this.e = e;
            this.f = f;
            g = 0;
        }

        @Override
        public final int[] getByteCode() {
            return new int[]{
                    word(a, g, op),
                    item.index,
                    word(f, e, d, c)

            };
        }
    }
}
