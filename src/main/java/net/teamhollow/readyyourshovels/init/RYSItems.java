package net.teamhollow.readyyourshovels.init;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.item.*;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.teamhollow.readyyourshovels.ReadyYourShovels;
import net.teamhollow.readyyourshovels.block.CaveCarrotBlock;
import net.teamhollow.readyyourshovels.item.vanilla.PublicMusicDiscItem;

@SuppressWarnings("unused")
public class RYSItems {
    public static final Item PEAT = register("peat");
    public static final Item COMBUSTING_PEAT = register("combusting_peat");

    public static final Item DAYROOT_MULCH = register("dayroot_mulch");

    public static final Item MUSIC_DISC_COLLY = register("music_disc_colly", new PublicMusicDiscItem(
            12, RYSSoundEvents.MUSIC_DISC_COLLY,
            new FabricItemSettings()
                .maxCount(1)
                .rarity(Rarity.RARE)
                .group(ReadyYourShovels.ITEM_GROUP)
            )
    );

    public static final Item CAVE_CARROT = register("cave_carrot", new BlockItem(
        RYSBlocks.CAVE_CARROT,
        new FabricItemSettings()
            .food(new FoodComponent.Builder().hunger(3).saturationModifier(0.6F).build())
            .group(ReadyYourShovels.ITEM_GROUP)
        )
    );
    public static final Item WASHED_CAVE_CARROT = register("washed_cave_carrot", new Item(
        new FabricItemSettings()
            .food(new FoodComponent.Builder().hunger(7).saturationModifier(1.25F).build())
            .group(ReadyYourShovels.ITEM_GROUP)
        )
    );

    static {
        FuelRegistry frInstance = FuelRegistry.INSTANCE;
        frInstance.add(RYSItems.PEAT, 1200);
        frInstance.add(RYSItems.COMBUSTING_PEAT, 2000);

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            BlockPos pos = hitResult.getBlockPos();
            BlockState state = world.getBlockState(pos);
            ItemStack stack = player.getStackInHand(hand);

            if (state.isOf(Blocks.WATER_CAULDRON) && stack.getItem() == RYSItems.CAVE_CARROT) {
                if (!world.isClient) {
                    player.playSound(SoundEvents.ITEM_BUCKET_EMPTY, 1.0F, 1.0F);
                    ItemStack exchangeStack = ItemUsage.exchangeStack(stack, player, RYSItems.WASHED_CAVE_CARROT.getDefaultStack());
                    player.setStackInHand(hand, exchangeStack);

                    player.incrementStat(RYSStats.WASH_CAVE_CARROT);
                    LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
                }

                return ActionResult.success(world.isClient());
            }

            return ActionResult.PASS;
        });
    }

    private static Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(ReadyYourShovels.MOD_ID, id), item);
    }
    private static Item register(String id) {
        return register(id, new Item(new FabricItemSettings().group(ReadyYourShovels.ITEM_GROUP)));
    }
}
