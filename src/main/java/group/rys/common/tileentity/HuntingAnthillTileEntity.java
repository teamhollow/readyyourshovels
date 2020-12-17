package group.rys.common.tileentity;

import group.rys.common.entity.HuntingAntEntity;
import group.rys.core.registry.ModEntities;
import group.rys.core.registry.ModTileEntities;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class HuntingAnthillTileEntity extends TileEntity implements ITickableTileEntity {

    private int foodcount = 0;
    private int maxCount = 32;
    private int delay = 0;
    private int maxDelay = 300;

    public HuntingAnthillTileEntity() {
        super(ModTileEntities.hunting_anthill);
    }

    public void read(CompoundNBT compound) {
        super.read(compound);

        this.foodcount = compound.getInt("FoodCount");
        this.maxCount = compound.getInt("MaxCount");
        this.delay = compound.getInt("Delay");
        this.maxDelay = compound.getInt("MaxDelay");
    }

    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);

        compound.putInt("FoodCount", this.foodcount);
        compound.putInt("MaxCount", this.maxCount);
        compound.putInt("Delay", this.delay);
        compound.putInt("MaxDelay", this.maxDelay);

        return compound;
    }

    public void tick() {
        World world = this.getWorld();
        BlockPos pos_1 = this.getPos();


        if (this.delay >= this.maxDelay) {
            List<HuntingAntEntity> list = world.getEntitiesWithinAABB(HuntingAntEntity.class, new AxisAlignedBB(pos_1).grow(32.0D), (entity) -> {
                return entity instanceof HuntingAntEntity;
            });

            if (list.size() < 8) {
                int x = this.world.rand.nextInt(9) - 4;
                int y = this.world.rand.nextInt(3) - 1;
                int z = this.world.rand.nextInt(9) - 4;

                BlockPos pos_2 = pos_1.add(x, y, z);

                if (world.isAirBlock(pos_2)) {
                    HuntingAntEntity ant = new HuntingAntEntity(ModEntities.hunting_ant, world);

                    ant.setPosition(pos_2.getX() + 0.5, pos_2.getY() + 0.5, pos_2.getZ() + 0.5);
                    ant.setInventoryPosition(pos_1);
                    world.addEntity(ant);
                }
            }

            this.delay = 0;
        } else {
            this.delay++;
        }
    }

    public int getFoodcount() {
        return foodcount;
    }

    public void setFoodcount(int foodcount) {
        this.foodcount = foodcount;
    }


}
