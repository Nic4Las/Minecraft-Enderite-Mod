package net.enderitemc.enderitemod.mixin.datafix;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.datafixer.schema.Schema99;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.HashMap;
import java.util.Map;

@Mixin(Schema99.class)
public class RenameBlockEntityMapMixin {

    @ModifyVariable(at = @At("HEAD"), ordinal = 0, method = "updateBlockEntityTags(Lcom/mojang/serialization/Dynamic;Ljava/util/Map;Ljava/util/Map;)Ljava/lang/Object;", argsOnly = true)
    private static Map<String, String> editMap(Map<String, String> map) {
        HashMap<String, String> new_map = new HashMap<>(map);
        // Add mapping from item stack to block entity id for data fixers
        new_map.put(EnderiteMod.ENDERITE_SHULKER_BOX.getIdAsString(), EnderiteMod.ENDERITE_SHULKER_BOX_BLOCK_ENTITY.getIdAsString());
        new_map.put(EnderiteTools.ENDERITE_SHIELD.getIdAsString(), Registries.BLOCK_ENTITY_TYPE.getId(BlockEntityType.BANNER).toString());
        return new_map;
    }
}
