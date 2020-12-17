package group.rys.common.world.gen.carver;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.ProbabilityConfig;

import java.util.BitSet;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class ValleyCanyonRavineCarver extends WorldCarver<ProbabilityConfig> {

    public ValleyCanyonRavineCarver(Function<Dynamic<?>, ? extends ProbabilityConfig> p_i49921_1_, int p_i49921_2_) {
        super(p_i49921_1_, p_i49921_2_);
    }

    @Override
    public boolean carve(IChunk chunkIn, Random rand, int seaLevel, int chunkX, int chunkZ, int p_212867_6_, int p_212867_7_, BitSet carvingMask, ProbabilityConfig config)
    {
        int lvt_10_1_ = (this.func_222704_c() * 2 - 1) * 16;
        double lvt_11_1_ = (double)(chunkX * 16 + rand.nextInt(16));
        double lvt_13_1_ = (double)(rand.nextInt(rand.nextInt(40) + 8) + 0);  // + 20
        double lvt_15_1_ = (double)(chunkZ * 16 + rand.nextInt(16));
        float lvt_17_1_ = rand.nextFloat() * 6.2831855F;
        float lvt_18_1_ = (rand.nextFloat() - 0.5F) * 2.0F / 0.05F;  //0.5F) * 2.0F / 8.0F; is default but then i try / 0.05F
        float lvt_21_1_ = (rand.nextFloat() * 2.0F + rand.nextFloat()) * 2.0F;
        int lvt_22_1_ = lvt_10_1_ - rand.nextInt(lvt_10_1_ / 4);

        this.tryPlace(chunkIn, rand.nextLong(), seaLevel, p_212867_6_, p_212867_7_, lvt_11_1_, lvt_13_1_, lvt_15_1_, lvt_21_1_, lvt_17_1_, lvt_18_1_, 0, lvt_22_1_, 3.0D, carvingMask);
        return true;
    }

    private void tryPlace(IChunk chunkIn, long randomizer, int seaLevel, int p_222729_5_, int p_222729_6_, double p_222729_7_, double p_222729_9_, double p_222729_11_, float p_222729_13_, float p_222729_14_, float p_222729_15_, int p_222729_16_, int p_222729_17_, double p_222729_18_, BitSet p_222729_20_)
    {
        Random lvt_21_1_ = new Random(randomizer);

        float lvt_23_2_ = 0.0F;
        float lvt_24_1_ = 0.0F;

        for(int lvt_25_1_ = p_222729_16_; lvt_25_1_ < p_222729_17_; ++lvt_25_1_) {
            double lvt_26_1_ = 1.5D + (double)(MathHelper.sin((float)lvt_25_1_ * 3.1415927F / (float)p_222729_17_) * p_222729_13_);
            double lvt_28_1_ = lvt_26_1_ * p_222729_18_;
            lvt_26_1_ *= (double)lvt_21_1_.nextFloat() * 0.25D + 0.75D;
            lvt_28_1_ *= (double)lvt_21_1_.nextFloat() * 0.25D + 0.75D;
            float lvt_30_1_ = MathHelper.cos(p_222729_15_);
            float lvt_31_1_ = MathHelper.sin(p_222729_15_);

            p_222729_7_ += (double)(MathHelper.cos(p_222729_14_) * lvt_30_1_);
            p_222729_9_ += (double)lvt_31_1_;
            p_222729_11_ += (double)(MathHelper.sin(p_222729_14_) * lvt_30_1_);
            p_222729_15_ *= 0.7F;
            p_222729_15_ += lvt_24_1_ * 0.05F;
            p_222729_14_ += lvt_23_2_ * 0.05F;
            lvt_24_1_ *= 0.8F; //0.8F;
            lvt_23_2_ *= 0.5F; //0.5F;
            lvt_24_1_ += (lvt_21_1_.nextFloat() - lvt_21_1_.nextFloat()) * lvt_21_1_.nextFloat() * 2.0F;
            lvt_23_2_ += (lvt_21_1_.nextFloat() - lvt_21_1_.nextFloat()) * lvt_21_1_.nextFloat() * 4.0F;
            if (lvt_21_1_.nextInt(4) != 0) {
                if (!this.func_222702_a(p_222729_5_, p_222729_6_, p_222729_7_, p_222729_11_, lvt_25_1_, p_222729_17_, p_222729_13_)) {
                    return;
                }
                this.func_222705_a(chunkIn, randomizer, seaLevel, p_222729_5_, p_222729_6_, p_222729_7_, p_222729_9_, p_222729_11_, lvt_26_1_, lvt_28_1_, p_222729_20_);
                System.out.println(p_222729_7_ + " / " +  p_222729_9_ + " / " + p_222729_11_);
//                this.dig(chunkIn, p_222729_7_, p_222729_9_, p_222729_11_, seaLevel);
//                this.dig(chunkIn, p_222729_7_ + 1, p_222729_9_ + 1, p_222729_11_ + 1, seaLevel);
//                this.dig(chunkIn, p_222729_7_ + 1, p_222729_9_ + 1, p_222729_11_ - 1, seaLevel);
//                this.dig(chunkIn, p_222729_7_ - 1, p_222729_9_ + 1, p_222729_11_ + 1, seaLevel);
//                this.dig(chunkIn, p_222729_7_ - 1, p_222729_9_ + 1, p_222729_11_ - 1, seaLevel);

                //chunkIn.setBlockState(new BlockPos(p_222729_7_, p_222729_9_, p_222729_11_), Blocks.BIRCH_PLANKS.getDefaultState(), false);
            }
        }
    }


//    private void dig(IChunk chunkIn, Double x, Double y, Double z, int seaLevel)
//    {
//        if (y > seaLevel) {
//            for (Double height = y + 1; height > seaLevel; --height)
//            {
//                chunkIn.setBlockState(new BlockPos(x, height, z), Blocks.CAVE_AIR.getDefaultState(), false);
//            }
//        }
//    }


        @Override
    public boolean shouldCarve(Random rand, int chunkX, int chunkZ, ProbabilityConfig config) {
        return rand.nextFloat() <= config.probability;
    }

    @Override
    protected boolean func_222708_a(double p_222708_1_, double p_222708_3_, double p_222708_5_, int p_222708_7_) {
        return true;
    }
}
