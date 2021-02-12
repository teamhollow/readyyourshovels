package net.teamhollow.readyyourshovels.mixin.client;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModels;
import net.teamhollow.readyyourshovels.client.init.RYSEntityModelLayers;
import net.teamhollow.readyyourshovels.entity.ant.garden_ant.GardenAntEntityModel;
import net.teamhollow.readyyourshovels.entity.peaty_slime.PeatySlimeEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Environment(EnvType.CLIENT)
@Mixin(EntityModels.class)
public class EntityModelsMixin {
    @Inject(method = "getModels", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private static void getModels(CallbackInfoReturnable<Map<EntityModelLayer, TexturedModelData>> cir, ImmutableMap.Builder<EntityModelLayer, TexturedModelData> builder) {
        builder.put(RYSEntityModelLayers.GARDEN_ANT, GardenAntEntityModel.getTexturedModelData());
        builder.put(RYSEntityModelLayers.PEATY_SLIME, PeatySlimeEntityModel.getTexturedModelData());
    }
}
