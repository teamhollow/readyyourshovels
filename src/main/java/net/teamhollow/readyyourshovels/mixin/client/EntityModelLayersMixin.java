package net.teamhollow.readyyourshovels.mixin.client;

import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityModelLayers.class)
public class EntityModelLayersMixin {
    @Inject(method = "create", at = @At("HEAD"), cancellable = true)
    private static void create(String id, String layer, CallbackInfoReturnable<EntityModelLayer> cir) {
        Identifier identifier = new Identifier(id);
        if (!identifier.getNamespace().equals("minecraft")) cir.setReturnValue(new EntityModelLayer(identifier, layer));
    }
}
