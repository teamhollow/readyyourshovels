package group.rys.common.block;

import group.rys.common.tileentity.AnthillTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class AnthillBlock extends ContainerBlock {
	
	public AnthillBlock(Block.Properties properties) {
		super(properties);
	}
	
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new AnthillTileEntity();
	}
	
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
}
