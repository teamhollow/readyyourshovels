package net.teamhollow.readyyourshovels.block.enums;

import net.minecraft.block.Block;
import net.minecraft.util.StringIdentifiable;
import net.teamhollow.readyyourshovels.block.PlanterBoxBlock;

public enum PlanterBoxFilledBlock implements StringIdentifiable {
    EMPTY,
    SAND,
    DIRT;

    PlanterBoxFilledBlock() {}

    @Override
    public String toString() {
        return this.asString();
    }

    @Override
    public String asString() {
        switch (this) {
            default:
            case EMPTY:
                return "empty";
            case SAND:
                return "sand";
            case DIRT:
                return "dirt";
        }
    }

    public Block asBlock() {
        return PlanterBoxBlock.FILL_TYPES.get(this);
    }
}
