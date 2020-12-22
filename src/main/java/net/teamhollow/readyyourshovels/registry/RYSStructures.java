package net.teamhollow.readyyourshovels.registry;

import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.teamhollow.readyyourshovels.world.feature.AntHillStructure;
import net.teamhollow.readyyourshovels.ReadyYourShovels;

@Mod.EventBusSubscriber(modid = ReadyYourShovels.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RYSStructures {
    public static final Structure<NoFeatureConfig> ANT_HILL = new AntHillStructure(NoFeatureConfig.field_236558_a_);

    public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> ANT_HILL_FEATURE = func_244162_a(prefix("ant_hill"), ANT_HILL.withConfiguration(NoFeatureConfig.field_236559_b_));


    @SubscribeEvent
    public static void registerStructure(RegistryEvent.Register<Structure<?>> registry) {
        DimensionSettings.func_242746_i().getStructures().func_236195_a_().put(ANT_HILL, new StructureSeparationSettings(32, 8, 238429572));

        registry.getRegistry().register(ANT_HILL.setRegistryName(ReadyYourShovels.MOD_ID, "ant_hill"));
        Structure.NAME_STRUCTURE_BIMAP.put(prefix("ant_hill"), ANT_HILL);
    }

    private static <FC extends IFeatureConfig, F extends Structure<FC>> StructureFeature<FC, F> func_244162_a(String p_244162_0_, StructureFeature<FC, F> p_244162_1_) {
        return WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, p_244162_0_, p_244162_1_);
    }

    private static String prefix(String path) {
        return ReadyYourShovels.MOD_ID + ":" + path;
    }
}
