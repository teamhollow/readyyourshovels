package group.rys.common.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import group.rys.common.block.FruitTreeBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.BushConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;
import java.util.function.Function;

public class AppleTreeFeature extends Feature<BushConfig> {

    public AppleTreeFeature(Function<Dynamic<?>, ? extends BushConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, BushConfig config) {
        boolean flag = false;

        for (int i = 0; i < 64; ++i) {
            BlockPos pos_1 = pos.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));

            if (worldIn.isAirBlock(pos_1) && worldIn.isAirBlock(pos_1.up()) && config.state.getBlock().getDefaultState().isValidPosition(worldIn, pos_1)) {
                ((FruitTreeBlock) config.state.getBlock()).placeAt(worldIn, pos_1, 2);
                flag = true;
                return flag;
            }
        }

        return flag;
    }

}
