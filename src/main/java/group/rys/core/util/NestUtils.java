package group.rys.core.util;

import group.rys.common.tileentity.HuntingAnthillTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NestUtils {
    public static void addFed(HuntingAnthillTileEntity tileEntity) {
        tileEntity.setFoodcount(tileEntity.getFoodcount() + 1);
    }

    public static void addFed(World world, BlockPos pos) {
//		addStackToInventory(ChestBlock.getInventory(state, world, pos, false), stack);
        addFed((HuntingAnthillTileEntity) world.getTileEntity(pos));
    }
}
