package group.rys.common.entity;

import group.rys.common.block.FruitTreeBlock;
import group.rys.core.registry.ModBlocks;
import group.rys.core.util.InventoryUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.List;

public class GathererAntEntity extends CreatureEntity {
    private int lifetime;

	private static final DataParameter<BlockPos> INVENTORY = EntityDataManager.createKey(GathererAntEntity.class, DataSerializers.BLOCK_POS);
	
	public GathererAntEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
		super(type, worldIn);
		this.setCanPickUpLoot(true);
		this.setDropChance(EquipmentSlotType.MAINHAND, 1.0F);
		this.setPathPriority(PathNodeType.WATER, -1.0F);
	}
	
	public void setInventoryPosition(BlockPos pos) {
		this.dataManager.set(INVENTORY, pos);
	}
	
	public BlockPos getInventoryPosition() {
		return this.dataManager.get(INVENTORY);
	}
	
	public boolean hasInventory() {
		return this.getInventoryPosition() != BlockPos.ZERO;
	}
	
	public void resetInventoryPosition() {
		this.setInventoryPosition(BlockPos.ZERO);
	}
	
	protected void registerData() {
		super.registerData();
		
		this.dataManager.register(INVENTORY, BlockPos.ZERO);
	}
	
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);

        compound.putInt("Lifetime", this.lifetime);

		compound.putInt("InvPosX", this.getInventoryPosition().getX());
		compound.putInt("InvPosY", this.getInventoryPosition().getY());
		compound.putInt("InvPosZ", this.getInventoryPosition().getZ());
	}
	
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);

        this.lifetime = compound.getInt("Lifetime");

		int i = compound.getInt("InvPosX");
		int j = compound.getInt("InvPosY");
		int k = compound.getInt("InvPosZ");
		
		this.setInventoryPosition(new BlockPos(i, j, k));
	}
	
	protected void registerAttributes() {
		super.registerAttributes();
		
		this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(0.5F);
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5.0D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
	}
	
	protected void registerGoals() {
        //this.goalSelector.addGoal(0, new GathererAntEntity.FindInventoryGoal(this));
        //this.goalSelector.addGoal(1, new GathererAntEntity.UpdateInventoryGoal(this));
//		this.goalSelector.addGoal(2, new AntEntity.FindItemsGoal(this));
//		this.goalSelector.addGoal(3, new AntEntity.StoreItemsGoal(this));
//		this.goalSelector.addGoal(4, new AntEntity.HarvestFruitTreeGoal(this));
		this.goalSelector.addGoal(5, new GathererAntEntity.WalkAroundGoal(this));
        //this.goalSelector.addGoal(6, new GathererAntEntity.WalkAroundInventoryGoal(this));
	}
	
	protected void playStepSound(BlockPos pos, BlockState state) {
		
	}
	
	protected SoundEvent getAmbientSound() {
		return null;
	}
	
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return SoundEvents.ENTITY_SILVERFISH_HURT;
	}
	
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SILVERFISH_DEATH;
	}
	
	public void fall(float distance, float damageMultiplier) {
		
	}
	
	public CreatureAttribute getCreatureAttribute() {
		return CreatureAttribute.ARTHROPOD;
	}

    public void livingTick() {
        super.livingTick();
        if (!this.world.isRemote) {
            if (!this.isNoDespawnRequired()) {
                ++this.lifetime;
            }

            if (this.lifetime >= 2400) {
                this.remove();
            }
        }
    }

	public void tick() {
		super.tick();
	}
	
	public static class FindInventoryGoal extends Goal {
		
		private final GathererAntEntity ant;
		
		public FindInventoryGoal(GathererAntEntity ant) {
			this.ant = ant;
		}
		
		public boolean shouldExecute() {
			return !this.ant.hasInventory();
		}
		
		public void startExecuting() {
			if (this.shouldExecute()) {
				this.tick();
			}
		}
		
		public void tick() {
			BlockPos pos_1 = this.ant.getPosition();
			World world = this.ant.getEntityWorld();
			
			BlockPos pos_2 = null;
			
			for (BlockPos pos_3 : BlockPos.getAllInBoxMutable(pos_1.getX() - 16, pos_1.getY() - 16, pos_1.getZ() - 16, pos_1.getX() + 16, pos_1.getY() + 16, pos_1.getZ() + 16)) {
				if (world.getBlockState(pos_3).getBlock() == ModBlocks.anthill && InventoryUtils.canStoreStackInInventory(world, pos_3, this.ant.getHeldItemMainhand())) {
					pos_2 = pos_3;
					break;
				}
			}
			
			if (pos_2 != null) {
				this.ant.setInventoryPosition(pos_2);
			}
		}
		
	}
	
	public static class UpdateInventoryGoal extends Goal {
		
		private final GathererAntEntity ant;
		
		public UpdateInventoryGoal(GathererAntEntity ant) {
			this.ant = ant;
		}
		
		public boolean shouldExecute() {
			return this.ant.hasInventory();
		}
		
		public void startExecuting() {
			if (this.shouldExecute()) {
				this.tick();
			}
		}
		
		public void tick() {
			World world = this.ant.getEntityWorld();
			BlockPos pos = this.ant.getInventoryPosition();
			
			if (world.getBlockState(pos).getBlock() != ModBlocks.anthill || !InventoryUtils.canStoreStackInInventory(world, pos, this.ant.getHeldItemMainhand())) {
				this.ant.resetInventoryPosition();
			}
		}
		
	}
	
	public static class FindItemsGoal extends Goal {
		
		private final GathererAntEntity ant;
		
		public FindItemsGoal(GathererAntEntity ant) {
			this.ant = ant;
			this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
		}
		
		public boolean shouldExecute() {
			List<ItemEntity> list = this.ant.world.getEntitiesWithinAABB(ItemEntity.class, this.ant.getBoundingBox().grow(32.0D), (entity) -> {
				return entity instanceof ItemEntity;
			});
			
			return this.ant.getHeldItemMainhand().isEmpty() && !list.isEmpty();
		}
		
		public void startExecuting() {
			List<ItemEntity> list = this.ant.world.getEntitiesWithinAABB(ItemEntity.class, this.ant.getBoundingBox().grow(32.0D), (entity) -> {
				return entity instanceof ItemEntity;
			});
			
			if (!list.isEmpty()) {
				this.ant.getNavigator().setPath(this.ant.getNavigator().getPathToEntityLiving(list.get(0), 1), 0.75D);
			}
		}
		
	}
	
	public static class StoreItemsGoal extends Goal {
		
		private final GathererAntEntity ant;
		
		public StoreItemsGoal(GathererAntEntity ant) {
			this.ant = ant;
			this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
		}
		
		public boolean shouldExecute() {
			return !this.ant.getHeldItemMainhand().isEmpty() && this.ant.hasInventory();
		}
		
		public void startExecuting() {
			World world = this.ant.world;
			BlockPos pos = this.ant.getInventoryPosition();
			BlockState state = world.getBlockState(pos);
			
			ItemStack stack = this.ant.getHeldItemMainhand();
			
			if (state.getBlock() == ModBlocks.anthill && InventoryUtils.canStoreStackInInventory(world, pos, stack)) {
				this.ant.getMoveHelper().setMoveTo(pos.getX(), pos.getY(), pos.getZ(), 0.75D);
				
				if (this.ant.getPosition().distanceSq(pos) <= 1) {
					InventoryUtils.addStackToInventory(world, pos, stack);
					this.ant.setItemStackToSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
				}
			}
		}
		
	}
	
	public static class HarvestFruitTreeGoal extends Goal {
		
		private final GathererAntEntity ant;
		
		public HarvestFruitTreeGoal(GathererAntEntity ant) {
			this.ant = ant;
			this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
		}
		
		public boolean shouldExecute() {
			List<ItemEntity> list = this.ant.world.getEntitiesWithinAABB(ItemEntity.class, this.ant.getBoundingBox().grow(32.0D), (entity) -> {
				return entity instanceof ItemEntity;
			});
			
			return this.ant.getHeldItemMainhand().isEmpty() && list.isEmpty();
		}
		
		public void startExecuting() {
			World world = this.ant.world;
			
			BlockPos pos_1 = this.ant.getPosition();
			BlockPos pos_2 = null;
			
			for (BlockPos pos_3 : BlockPos.getAllInBoxMutable(pos_1.getX() - 16, pos_1.getY() - 16, pos_1.getZ() - 16, pos_1.getX() + 16, pos_1.getY() + 16, pos_1.getZ() + 16)) {
				BlockState state = world.getBlockState(pos_3);

				if (state.getBlock() instanceof FruitTreeBlock && state.get(FruitTreeBlock.AGE) == 3) {
					pos_2 = pos_3;
					break;
				}
			}
			
			if (pos_2 != null) {
				BlockState state = world.getBlockState(pos_2);

				this.ant.getMoveHelper().setMoveTo(pos_2.getX(), pos_2.getY(), pos_2.getZ(), 0.75D);
				
				if (this.ant.getPosition().distanceSq(pos_2) <= 1) {
					FruitTreeBlock block = (FruitTreeBlock) state.getBlock();
					
					if (world.rand.nextInt(5) == 0) {
						Block.spawnAsEntity(world, pos_2, new ItemStack(block.getRottenFruit(), 1 + world.rand.nextInt(4)));
					} else {
						Block.spawnAsEntity(world, pos_2, new ItemStack(block.getFruit(), 1 + world.rand.nextInt(4)));
					}
					
					world.setBlockState(pos_2, state.with(FruitTreeBlock.AGE, Integer.valueOf(2)), 3);
				}
			}
		}
		
	}
	
	public static class WalkAroundGoal extends Goal {
		
		private final GathererAntEntity ant;
		
		public WalkAroundGoal(GathererAntEntity ant) {
			this.ant = ant;
			this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
		}
		
		public boolean shouldExecute() {
            return this.ant.getNavigator().noPath();
		}
		
		public boolean shouldContinueExecuting() {
			return !this.ant.getNavigator().noPath();
		}
		
		public void startExecuting() {
			World world = this.ant.world;

			int x = world.rand.nextInt(32) - 16;
			int y = world.rand.nextInt(32) - 16;
			int z = world.rand.nextInt(32) - 16;
			
			BlockPos pos_1 = this.ant.getPosition();
			BlockPos pos_2 = pos_1.add(x, y, z);
			
			if (world.isAirBlock(pos_2) && !world.hasWater(pos_2) && !world.isAirBlock(pos_2.down()) && !world.hasWater(pos_2.down())) {
				this.ant.getNavigator().setPath(this.ant.getNavigator().getPathToPos(pos_2, 1), 0.75D);
			}
		}
		
	}
	
	public static class WalkAroundInventoryGoal extends Goal {
		
		private final GathererAntEntity ant;
		
		public WalkAroundInventoryGoal(GathererAntEntity ant) {
			this.ant = ant;
			this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
		}
		
		public boolean shouldExecute() {
			return this.ant.hasInventory() && this.ant.getNavigator().noPath();
		}
		
		public boolean shouldContinueExecuting() {
			return !this.ant.getNavigator().noPath();
		}
		
		public void startExecuting() {
			World world = this.ant.world;

			int x = world.rand.nextInt(32) - 16;
			int y = world.rand.nextInt(32) - 16;
			int z = world.rand.nextInt(32) - 16;
			
			BlockPos pos_1 = this.ant.getInventoryPosition();
			BlockPos pos_2 = pos_1.add(x, y, z);

			if (world.isAirBlock(pos_2) && !world.hasWater(pos_2) && !world.isAirBlock(pos_2.down()) && !world.hasWater(pos_2.down())) {
				this.ant.getNavigator().setPath(this.ant.getNavigator().getPathToPos(pos_2, 1), 0.75D);
			}
		}
		
	}
	
}
