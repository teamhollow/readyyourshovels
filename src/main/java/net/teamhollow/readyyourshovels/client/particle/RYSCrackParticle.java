package net.teamhollow.readyyourshovels.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.CrackParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DefaultParticleType;
import net.teamhollow.readyyourshovels.init.RYSItems;

@Environment(EnvType.CLIENT)
public class RYSCrackParticle extends CrackParticle {
    public RYSCrackParticle(ClientWorld world, double x, double y, double z, ItemStack stack) {
        super(world, x, y, z, stack);
    }

    @Environment(EnvType.CLIENT)
    public static class PeatFactory implements ParticleFactory<DefaultParticleType> {
        @Override
        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double x, double y, double z, double g, double h, double i) {
            return new RYSCrackParticle(clientWorld, x, y, z, new ItemStack(RYSItems.PEAT));
        }
    }
}
