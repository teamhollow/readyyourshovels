package net.teamhollow.readyyourshovels.state.property;

import net.minecraft.state.property.IntProperty;

public class RYSProperties {
    /**
     * A property that specifies the resource level of an ant nest.
     */
    public static final IntProperty RESOURCE_LEVEL = IntProperty.of("resource_level", 0, 5);
}
