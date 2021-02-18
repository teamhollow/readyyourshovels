package net.teamhollow.readyyourshovels.state.property;

import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.teamhollow.readyyourshovels.block.enums.PlanterBoxFilledBlock;

public class RYSProperties {
    /**
     * A property that specifies the acid level of an ant nest.
     */
    public static final IntProperty ACID_LEVEL = IntProperty.of("acid_level", 0, 5);
    /**
     * A property that defines whether a dayroot is connected to a dirt-like block.
     */
    public static final BooleanProperty ROOT = BooleanProperty.of("root");
    /**
     * A property that defines which block a planter box is filled with.
     */
    public static final EnumProperty<PlanterBoxFilledBlock> FILLED_BLOCK = EnumProperty.of("filled_block", PlanterBoxFilledBlock.class);
    /**
     * A property that defines whether a planter box is wet.
     */
    public static final BooleanProperty WET = BooleanProperty.of("wet");
}
