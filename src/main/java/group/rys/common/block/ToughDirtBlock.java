package group.rys.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

public class ToughDirtBlock extends Block {
	
	public ToughDirtBlock(Block.Properties properties) {
		super(properties);
	}
	
	public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction direction, IPlantable plantable) {
		PlantType type = plantable.getPlantType(world, pos.offset(direction));
		
		if (type == PlantType.Plains || type == PlantType.Cave) {
			return true;
		}
		
		return false;
	}
	
}
