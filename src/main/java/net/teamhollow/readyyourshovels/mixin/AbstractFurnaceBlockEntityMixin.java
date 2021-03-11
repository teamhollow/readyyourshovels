package net.teamhollow.readyyourshovels.mixin;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.world.World;
import net.teamhollow.readyyourshovels.tag.RYSItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceBlockEntityMixin {
    @Inject(method = "getCookTime", at = @At("RETURN"), cancellable = true)
    private static void getCookTime(World world, RecipeType<? extends AbstractCookingRecipe> recipeType, Inventory inventory, CallbackInfoReturnable<Integer> cir) {
        if (inventory.getStack(1).isIn(RYSItemTags.PEAT)) cir.setReturnValue(cir.getReturnValueI() / 2);
    }
}
