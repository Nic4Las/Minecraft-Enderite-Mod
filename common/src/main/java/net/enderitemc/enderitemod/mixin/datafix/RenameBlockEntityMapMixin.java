package net.enderitemc.enderitemod.mixin.datafix;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.datafix.schemas.V99;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.HashMap;
import java.util.Map;

@Mixin(V99.class)
public class RenameBlockEntityMapMixin {

    @ModifyVariable(at = @At("HEAD"), ordinal = 0, method = "addNames(Lcom/mojang/serialization/Dynamic;Ljava/util/Map;Ljava/util/Map;)Ljava/lang/Object;", argsOnly = true)
    private static Map<String, String> editMap(Map<String, String> map) {
        HashMap<String, String> new_map = new HashMap<>(map);
        // Add mapping from item stack to block entity id for data fixers
        new_map.put(EnderiteMod.ENDERITE_SHULKER_BOX.getRegisteredName(), EnderiteMod.ENDERITE_SHULKER_BOX_BLOCK_ENTITY.getRegisteredName());
        new_map.put(EnderiteTools.ENDERITE_SHIELD.getRegisteredName(), BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(BlockEntityType.BANNER).toString());
        return new_map;
    }
}
