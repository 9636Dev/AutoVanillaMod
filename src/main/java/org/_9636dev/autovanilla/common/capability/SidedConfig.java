package org._9636dev.autovanilla.common.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class SidedConfig implements INBTSerializable<CompoundTag> {

    public enum Side {
        NONE(0),
        INPUT_1(1),
        INPUT_2(2),
        INPUT_3(3),
        OUTPUT_1(4),
        OUTPUT_2(5),
        INPUT_1_OUTPUT_1(6),
        INPUT_2_OUTPUT_1(7),
        INPUT_1_OUTPUT_2(8),
        INPUT_2_OUTPUT_2(9);

        private final int id;
        Side(int id) {
            this.id = id;
        }
    }


    /**
     * Max 10 different sides, side order N E S W U D,
     * Each side 10 bits, Most Sig 4 bits not used
     */
    private long sidesConfig;

    public SidedConfig() {
        this.sidesConfig = 0L;
    }

    private int getSide(int offset) {
        return (int) ((this.sidesConfig >> (64 - offset - 10)) & 0x03ff);
    }

    private int getOffsetOfSide(Direction direction) {
        return switch (direction) {
            case NORTH -> 0;
            case EAST -> 10;
            case SOUTH -> 20;
            case WEST -> 30;
            case UP -> 40;
            case DOWN -> 50;
        } + 4;
    }

    public Side getSide(Direction direction) {
        return Side.values()[getSide(getOffsetOfSide(direction))];
    }

    private void insertNumAtOffset(int offset, int value) {
        long clearMask = ~(0x3FFL << (64 - offset - 10));
        long shiftedValue = (long) value << (64 - offset - 10);

        this.sidesConfig = (this.sidesConfig & clearMask) | shiftedValue;
    }

    public void setSide(Direction direction, Side side) {
        int offset = getOffsetOfSide(direction);
        this.insertNumAtOffset(offset, side.id);
    }

    public Side[] getSides() {
        return new Side[] {
                this.getSide(Direction.NORTH),
                this.getSide(Direction.EAST),
                this.getSide(Direction.SOUTH),
                this.getSide(Direction.WEST),
                this.getSide(Direction.UP),
                this.getSide(Direction.DOWN),
        };
    }


    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putLong("sides", this.sidesConfig);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.sidesConfig = nbt.getLong("sides");
    }
}
