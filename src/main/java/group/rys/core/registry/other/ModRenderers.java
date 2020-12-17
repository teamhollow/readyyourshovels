package group.rys.core.registry.other;

import group.rys.client.renderer.GathererAntRenderer;
import group.rys.client.renderer.HuntingAntLarvaeRenderer;
import group.rys.client.renderer.HuntingAntRenderer;
import group.rys.client.renderer.QueenAntRenderer;
import group.rys.common.block.FruitTreeBlock;
import group.rys.common.entity.GathererAntEntity;
import group.rys.common.entity.HuntingAntEntity;
import group.rys.common.entity.HuntingAntLarvaeEntity;
import group.rys.common.entity.QueenAntEntity;
import group.rys.core.registry.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ModRenderers {
	
	public static void register() {
		RenderingRegistry.registerEntityRenderingHandler(GathererAntEntity.class, GathererAntRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(HuntingAntEntity.class, HuntingAntRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(QueenAntEntity.class, QueenAntRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(HuntingAntLarvaeEntity.class, HuntingAntLarvaeRenderer::new);
        renderBlockColor();
    }

    public static void renderBlockColor() {

        Minecraft.getInstance().getBlockColors().register((p_210229_0_, p_210229_1_, p_210229_2_, p_210229_3_) -> {
            return p_210229_1_ != null && p_210229_2_ != null && p_210229_0_.get(FruitTreeBlock.HALF) == DoubleBlockHalf.UPPER && p_210229_0_.get(FruitTreeBlock.AGE) > 1 ? BiomeColors.getFoliageColor(p_210229_1_, p_210229_2_) : -1;

        }, ModBlocks.orange_fruit_tree, ModBlocks.apple_fruit_tree, ModBlocks.apricot_fruit_tree);

    }
}
