package net.teamhollow.readyyourshovels.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.teamhollow.readyyourshovels.block.AntNestBlock;
import net.teamhollow.readyyourshovels.entity.garden_ant.GardenAntEntity;
import net.teamhollow.readyyourshovels.init.RYSBlockEntities;
import net.teamhollow.readyyourshovels.init.RYSEntities;

import java.util.List;
import java.util.Objects;

public class AntNestBlockEntity extends BlockEntity implements Tickable, Inventory {
    public static final String id = AntNestBlock.id;

    private DefaultedList<ItemStack> items = DefaultedList.ofSize(9, ItemStack.EMPTY);
    private int count = 5;
    private int maxCount = 13;
    private int delay = 0;
    private int maxDelay = 200;

    public AntNestBlockEntity() {
        super(RYSBlockEntities.ANT_NEST);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);

        this.items = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        this.count = tag.getInt("Count");
        this.maxCount = tag.getInt("MaxCount");
        this.delay = tag.getInt("Delay");
        this.maxDelay = tag.getInt("MaxDelay");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);

        Inventories.toTag(tag, this.items);
        tag.putInt("Count", this.count);
        tag.putInt("MaxCount", this.maxCount);
        tag.putInt("Delay", this.delay);
        tag.putInt("MaxDelay", this.maxDelay);

        return tag;
    }

    @Override
    public void tick() {
        World world = this.getWorld();
        BlockPos pos = this.getPos();
        assert this.world != null;
        BlockState state = this.world.getBlockState(pos);
        Direction direction = state.get(AntNestBlock.FACING);
        BlockPos frontPos = pos.offset(direction);

        if (this.delay >= this.maxDelay) {
            assert world != null;
            List<GardenAntEntity> list = world.getEntitiesByClass(GardenAntEntity.class, new Box(pos).expand(32.0D), Objects::nonNull);

            if (list.size() < this.count) {
                if (world.isAir(frontPos)) {
                    GardenAntEntity entity = RYSEntities.GARDEN_ANT.create(world);

                    assert entity != null;
                    float f = entity.getWidth();
                    double d = !this.world.getBlockState(frontPos).getCollisionShape(this.world, frontPos).isEmpty() ? 0.0D : 0.55D + (double) (f / 2.0F);
                    double x = pos.getX() + 0.5D + d * (double) direction.getOffsetX();
                    double y = (double) pos.getY() + 0.5D - (double) (entity.getHeight() / 2.0F);
                    double z = pos.getZ() + 0.5D + d * (double) direction.getOffsetZ();

                    entity.refreshPositionAndAngles(x, y, z, entity.yaw, entity.pitch);
                    this.world.spawnEntity(entity);
                } else {
                    this.delay = 0;
                }
            } else {
                this.delay = 0;
            }

            if (this.count < this.maxCount) {
                for (int i = 0; i < this.items.size(); i++) {
                    ItemStack stack = this.getStack(i);

                    if (stack.isFood() && stack.getCount() >= 8) {
                        this.count = this.count + 1;
                        this.removeStack(i, 8);
                    }
                }
            }

            this.delay = 0;
        } else {
            this.delay++;
        }
    }

    @Override
    public void clear() {
        this.items.clear();
    }

    @Override
    public int size() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getStack(int index) {
        if (index >= 0 && index < this.items.size()) {
            return this.items.get(index);
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public ItemStack removeStack(int index, int count) {
        return Inventories.splitStack(this.items, index, count);
    }

    @Override
    public ItemStack removeStack(int index) {
        return Inventories.removeStack(this.items, index);
    }

    @Override
    public void setStack(int index, ItemStack stack) {
        if (index >= 0 && index < this.items.size()) {
            this.items.set(index, stack);
        }
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return false;
    }
}
