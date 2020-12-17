package group.rys.common.tileentity;

import java.util.List;

import group.rys.common.entity.GathererAntEntity;
import group.rys.core.registry.ModEntities;
import group.rys.core.registry.ModTileEntities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AnthillTileEntity extends TileEntity implements ITickableTileEntity, IInventory {
	
	private NonNullList<ItemStack> items = NonNullList.withSize(9, ItemStack.EMPTY);
	private int count = 5;
	private int maxCount = 13;
	private int delay = 0;
	private int maxDelay = 200;
	
	public AnthillTileEntity() {
		super(ModTileEntities.anthill);
	}
	
	public void read(CompoundNBT compound) {
		super.read(compound);
		
		this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
		this.count = compound.getInt("Count");
		this.maxCount = compound.getInt("MaxCount");
		this.delay = compound.getInt("Delay");
		this.maxDelay = compound.getInt("MaxDelay");
	}
	
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		
		ItemStackHelper.saveAllItems(compound, this.items);
		compound.putInt("Count", this.count);
		compound.putInt("MaxCount", this.maxCount);
		compound.putInt("Delay", this.delay);
		compound.putInt("MaxDelay", this.maxDelay);
		
		return compound;
	}
	
	public void tick() {
		World world = this.getWorld();
		BlockPos pos_1 = this.getPos();
		
		if (this.delay >= this.maxDelay) {
			List<GathererAntEntity> list = world.getEntitiesWithinAABB(GathererAntEntity.class, new AxisAlignedBB(pos_1).grow(32.0D), (entity) -> {
				return entity instanceof GathererAntEntity;
			});
			
			if (list.size() < this.count) {
				int x = this.world.rand.nextInt(9) - 4;
				int y = this.world.rand.nextInt(3) - 1;
				int z = this.world.rand.nextInt(9) - 4;
				
				BlockPos pos_2 = pos_1.add(x, y, z);
				
				if (world.isAirBlock(pos_2)) {
					GathererAntEntity ant = new GathererAntEntity(ModEntities.gatherer_ant, world);
					
					ant.setPosition(pos_2.getX() + 0.5, pos_2.getY() + 0.5, pos_2.getZ() + 0.5);
					ant.setInventoryPosition(pos_1);
					world.addEntity(ant);
				} else {
					this.delay = 0;
				}
			} else {
				this.delay = 0;
			}
			
			if (this.count < this.maxCount) {
				for (int i = 0; i < this.items.size(); i++) {
					ItemStack stack = this.getStackInSlot(i);
					
					if (stack.isFood() && stack.getCount() >= 8) {
						this.count = this.count + 1;
						this.decrStackSize(i, 8);
					}
				}
			}
			
			this.delay = 0;
		} else {
			this.delay++;
		}
	}
	
	public void clear() {
		this.items.clear();
	}
	
	public int getSizeInventory() {
		return this.items.size();
	}
	
	public boolean isEmpty() {
		for (ItemStack stack : this.items) {
			if (!stack.isEmpty()) {
				return false;
			}
		}
		
		return true;
	}
	
	public ItemStack getStackInSlot(int index) {
		if (index >= 0 && index < this.items.size()) {
			return this.items.get(index);
		} else {
			return ItemStack.EMPTY;
		}
	}
	
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(this.items, index, count);
	}
	
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.items, index);
	}
	
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (index >= 0 && index < this.items.size()) {
			this.items.set(index, stack);
		}
	}
	
	public boolean isUsableByPlayer(PlayerEntity player) {
		return false;
	}
	
}
