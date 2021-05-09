package net.teamhollow.readyyourshovels.init;

import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.teamhollow.readyyourshovels.ReadyYourShovels;
import net.teamhollow.readyyourshovels.particle.vanilla.PublicDefaultParticleType;

public class RYSParticles {
    public static final DefaultParticleType ITEM_COMBUSTING_PEAT = register("item_combusting_peat", false);
    public static final DefaultParticleType DAYROOT = register("dayroot", false);

    private static DefaultParticleType register(String id, boolean alwaysShow) {
        return Registry.register(Registry.PARTICLE_TYPE, new Identifier(ReadyYourShovels.MOD_ID, id), new PublicDefaultParticleType(alwaysShow));
    }
}
