package net.teamhollow.readyyourshovels.init;

import com.mojang.datafixers.types.Type;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import net.teamhollow.readyyourshovels.ReadyYourShovels;
import net.teamhollow.readyyourshovels.block.entity.AntNestBlockEntity;

public class RYSBlockEntities {
    public static final BlockEntityType<AntNestBlockEntity> ANT_NEST = register(AntNestBlockEntity.id, AntNestBlockEntity.builder);

    public RYSBlockEntities() {}

    private static <T extends BlockEntity> BlockEntityType<T> register(String id, BlockEntityType.Builder<T> builder) {
        Identifier identifier = new Identifier(ReadyYourShovels.MOD_ID, id);

        Type<?> type = Util.getChoiceType(TypeReferences.BLOCK_ENTITY, identifier.toString());
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, identifier, builder.build(type));
    }
}
