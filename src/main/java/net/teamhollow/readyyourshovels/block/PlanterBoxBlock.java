package net.teamhollow.readyyourshovels.block;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.teamhollow.readyyourshovels.block.enums.PlanterBoxFilledBlock;
import net.teamhollow.readyyourshovels.state.property.RYSProperties;

import java.util.Map;

@SuppressWarnings("deprecation")
public class PlanterBoxBlock extends Block {
    public static final EnumProperty<PlanterBoxFilledBlock> FILLED_BLOCK = RYSProperties.FILLED_BLOCK;
    public static final BooleanProperty WET = RYSProperties.WET;
    private static final VoxelShape RAYCAST_SHAPE = Block.createCuboidShape(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    protected static VoxelShape EMPTY_OUTLINE_SHAPE = VoxelShapes.combineAndSimplify(
        VoxelShapes.fullCube(),
        VoxelShapes.union(
            Block.createCuboidShape(2.0D, 8.0D, 2.0D, 14.0D, 9.0D, 14.0D),
            RAYCAST_SHAPE
        ), BooleanBiFunction.ONLY_FIRST);

    public static final ImmutableMap<PlanterBoxFilledBlock, Block> FILL_TYPES = new ImmutableMap.Builder<PlanterBoxFilledBlock, Block>()
        .put(PlanterBoxFilledBlock.DIRT, Blocks.DIRT)
        .put(PlanterBoxFilledBlock.SAND, Blocks.SAND)
        .build();

    public PlanterBoxBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(FILLED_BLOCK, PlanterBoxFilledBlock.EMPTY).with(WET, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(RYSProperties.FILLED_BLOCK).add(RYSProperties.WET);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (state.get(FILLED_BLOCK) == PlanterBoxFilledBlock.EMPTY) {
            for (Map.Entry<PlanterBoxFilledBlock, Block> entry : FILL_TYPES.entrySet()) {
                if (itemStack.isOf(entry.getValue().asItem())) {
                    world.playSound(null, pos, SoundEvents.BLOCK_COMPOSTER_FILL_SUCCESS, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    world.setBlockState(pos, state.with(FILLED_BLOCK, entry.getKey()));
                    if (!player.getAbilities().creativeMode) {
                        itemStack.decrement(1);
                    }

                    return ActionResult.SUCCESS;
                }
            }
        } else if (itemStack.isOf(Items.WATER_BUCKET) && !state.get(WET)) {
            world.setBlockState(pos, state.with(WET, true));
            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1, 1);
            player.setStackInHand(hand, ItemUsage.exchangeStack(itemStack, player, Items.BUCKET.getDefaultStack()));

            return ActionResult.SUCCESS;
        } else if (FabricToolTags.SHOVELS.contains(itemStack.getItem())) {
            if (!player.getAbilities().creativeMode) Block.dropStack(world,pos,new ItemStack(state.get(FILLED_BLOCK).asBlock()));

            world.playSound(null, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1, 1);
            world.setBlockState(pos, state.with(FILLED_BLOCK, PlanterBoxFilledBlock.EMPTY).with(WET, false));

            if (!world.isClient) itemStack.damage(1, player, p -> p.sendToolBreakStatus(hand));
            return ActionResult.success(world.isClient);
        }

        return ActionResult.PASS;
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        if (!player.getAbilities().creativeMode && state.get(FILLED_BLOCK) != PlanterBoxFilledBlock.EMPTY) {
            Block.dropStack(world, pos, new ItemStack(state.get(FILLED_BLOCK).asBlock()));
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(FILLED_BLOCK) == PlanterBoxFilledBlock.EMPTY ? EMPTY_OUTLINE_SHAPE : VoxelShapes.fullCube();
    }

    @Override
    public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
        return RAYCAST_SHAPE;
    }

    public static boolean isWetAndSupportive(Block block, BlockState state) {
        PlanterBoxFilledBlock filledBlock = state.get(RYSProperties.FILLED_BLOCK);
        return state.get(RYSProperties.WET) && (
            filledBlock == PlanterBoxFilledBlock.DIRT && (block instanceof CropBlock || block instanceof StemBlock || block instanceof AttachedStemBlock)
         || filledBlock == PlanterBoxFilledBlock.SAND && block == Blocks.SUGAR_CANE
        );
    }
}
