package net.teamhollow.readyyourshovels.block;

import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.teamhollow.readyyourshovels.init.RYSBlocks;
import net.teamhollow.readyyourshovels.init.RYSItems;
import net.teamhollow.readyyourshovels.init.RYSParticles;

import java.util.Random;

@SuppressWarnings("deprecation")
public class DayrootCrownBlock extends Block {
    public static final BlockState TOUGH_DIRT_STATE = RYSBlocks.TOUGH_DIRT.getDefaultState();

    public DayrootCrownBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack != null && FabricToolTags.SHEARS.contains(stack.getItem())) {
            BlockPos hitPos = pos.offset(hit.getSide());

            if (world.isClient) {
                spawnParticles(world, pos);
            } else {
                int rand = 1 + world.random.nextInt(2);
                dropStack(world, hitPos, new ItemStack(RYSItems.DAYROOT_MULCH, rand));

                world.playSound(null, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1.0f, 0.8f + world.random.nextFloat() * 0.4f);
                world.setBlockState(pos, TOUGH_DIRT_STATE, Block.NOTIFY_ALL);

                stack.damage(1, player, (e) -> e.sendToolBreakStatus(hand));
            }

            return ActionResult.success(world.isClient);
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }

    private static void spawnParticles(World world, BlockPos pos) {
        Random random = world.random;
        for (Direction direction : Direction.values()) {
            BlockPos dirPos = pos.offset(direction);
            if (!world.getBlockState(dirPos).isOpaqueFullCube(world, dirPos)) {
                Direction.Axis axis = direction.getAxis();
                for (int i = 0; i < 4; i++) {
                    double xOff = axis == Direction.Axis.X ? 0.5D + 0.5625D * (double) direction.getOffsetX() : (double) random.nextFloat();
                    double yOff = axis == Direction.Axis.Y ? 0.5D + 0.6625D * (double) direction.getOffsetY() : (double) random.nextFloat();
                    double zOff = axis == Direction.Axis.Z ? 0.5D + 0.5625D * (double) direction.getOffsetZ() : (double) random.nextFloat();

                    world.addParticle(RYSParticles.DAYROOT, (double) pos.getX() + xOff, (double) pos.getY() + yOff, (double) pos.getZ() + zOff, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }
}
