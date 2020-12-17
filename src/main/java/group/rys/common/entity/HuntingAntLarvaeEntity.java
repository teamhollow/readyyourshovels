package group.rys.common.entity;

import group.rys.core.registry.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class HuntingAntLarvaeEntity extends ThrowableEntity {

    public HuntingAntLarvaeEntity(EntityType<? extends HuntingAntLarvaeEntity> p_i50173_1_, World p_i50173_2_) {
        super(p_i50173_1_, p_i50173_2_);
    }

    public HuntingAntLarvaeEntity(World worldIn, LivingEntity throwerIn) {
        super(ModEntities.hunting_ant_larvae, throwerIn, worldIn);
    }

    public HuntingAntLarvaeEntity(World worldIn, double x, double y, double z) {
        super(ModEntities.hunting_ant_larvae, x, y, z, worldIn);
    }

    public HuntingAntLarvaeEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        this(ModEntities.hunting_ant_larvae, world);
    }

    @Override
    protected void registerData() {

    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (result.getType() == RayTraceResult.Type.ENTITY) {
            Entity entity = ((EntityRayTraceResult) result).getEntity();

            if (this.getThrower() == entity && this.ticksExisted < 4) {
                return;
            }

            spawnHuntingAnt();
        } else if (result.getType() == RayTraceResult.Type.BLOCK) {
            spawnHuntingAnt();
        }
        this.remove();
    }

    private void spawnHuntingAnt() {
        if (!this.world.isRemote) {
            for (int i = 0; i < 1 + this.world.rand.nextInt(2); i++) {
                HuntingAntEntity creepieEntity = ModEntities.hunting_ant.create(this.world);
                creepieEntity.setLocationAndAngles(this.getPosition().getX() + 0.5F, this.getPosition().getY(), this.getPosition().getZ() + 0.5F, 0.0F, 0.0F);

                this.world.addEntity(creepieEntity);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        this.setPosition(x, y, z);
        this.setRotation(yaw, pitch);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
