package com.dpforge.fastproxy.dex;

import com.dpforge.fastproxy.instruction.DexCodeInstruction;

import java.util.ArrayList;
import java.util.List;

public class DexCode {

    public final int registersSize;

    public final int insSize;

    public final int outsSize;

    public final List<DexCodeInstruction> instructions;

    private DexCode(final Builder builder) {
        registersSize = builder.registersSize;
        insSize = builder.insSize;
        outsSize = builder.outsSize;
        instructions = builder.instructions;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private int registersSize;
        private int insSize;
        private int outsSize;
        private final List<DexCodeInstruction> instructions = new ArrayList<>();

        private Builder() {
        }

        public Builder registersSize(final int registersSize) {
            this.registersSize = registersSize;
            return this;
        }

        public Builder insSize(final int insSize) {
            this.insSize = insSize;
            return this;
        }

        public Builder outsSize(final int outsSize) {
            this.outsSize = outsSize;
            return this;
        }

        public Builder instruction(final DexCodeInstruction instruction) {
            this.instructions.add(instruction);
            return this;
        }

        public DexCode build() {
            return new DexCode(this);
        }
    }
}
