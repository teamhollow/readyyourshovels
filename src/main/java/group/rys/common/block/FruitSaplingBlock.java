package group.rys.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Random;

public class FruitSaplingBlock extends BushBlock implements IGrowable {
    private FruitTreeBlock fruitTree;

    public FruitSaplingBlock(FruitTreeBlock fruit_tree, Block.Properties properties) {
        super(properties);
        this.fruitTree = fruit_tree;
    }

    public FruitTreeBlock getFruitTree() {
        return fruitTree;
    }

    @Override
    public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
        if (!worldIn.isAreaLoaded(pos, 1)) {
            return;
        }

        if (worldIn.getLight(pos.up()) >= 9 && random.nextInt(6) == 0) {
            this.getFruitTree().placeAt(worldIn, pos, 2);
        }
    }

    public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
        return worldIn.rand.nextFloat() < 0.45F;
    }

    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, BlockState state) {
        getFruitTree().placeAt(worldIn, pos, 2);
    }
}
