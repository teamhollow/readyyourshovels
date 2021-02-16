package net.teamhollow.readyyourshovels.init;

import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.teamhollow.readyyourshovels.ReadyYourShovels;
import net.teamhollow.readyyourshovels.block.CaveCarrotBlock;

public class RYSStats {
    public static final Identifier WASH_CAVE_CARROT = register("wash_" + CaveCarrotBlock.id, StatFormatter.DEFAULT);

    public RYSStats() {}

    private static Identifier register(String id, StatFormatter statFormatter) {
        Identifier identifier = new Identifier(ReadyYourShovels.MOD_ID, id);

        Registry.register(Registry.CUSTOM_STAT, id, identifier);
        Stats.CUSTOM.getOrCreateStat(identifier, statFormatter);

        return identifier;
    }
}
