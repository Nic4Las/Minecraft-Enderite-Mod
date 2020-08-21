package net.enderitemc.enderitemod.mixin;

import java.util.Map;

import com.google.common.collect.Maps;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.tools.EnderiteCrossbow;
import net.enderitemc.enderitemod.tools.EnderiteElytraSeperated;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ModelPredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
@Mixin(ModelPredicateProviderRegistry.class)
public abstract class EnderiteRangedMixin {

    @Shadow
    public static Map<Item, Map<Identifier, ModelPredicateProvider>> ITEM_SPECIFIC;

    @Shadow
    public static void register(Item item, Identifier id, ModelPredicateProvider provider) {
        ((Map) ITEM_SPECIFIC.computeIfAbsent(item, (itemx) -> {
            return Maps.newHashMap();
        })).put(id, provider);
    }

    static {
        register((Item) EnderiteMod.ENDERITE_BOW, new Identifier("pull"), (itemStack, clientWorld, livingEntity) -> {
            if (livingEntity == null) {
                return 0.0F;
            } else {
                return livingEntity.getActiveItem() != itemStack ? 0.0F
                        : (float) (itemStack.getMaxUseTime() - livingEntity.getItemUseTimeLeft()) / 20.0F;
            }
        });
        register((Item) EnderiteMod.ENDERITE_BOW, new Identifier("pulling"), (itemStack, clientWorld, livingEntity) -> {
            return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack
                    ? 1.0F
                    : 0.0F;
        });
        register((Item) EnderiteMod.ENDERITE_CROSSBOW, new Identifier("pull"),
                (itemStack, clientWorld, livingEntity) -> {
                    if (livingEntity == null) {
                        return 0.0F;
                    } else {
                        return EnderiteCrossbow.isCharged(itemStack) ? 0.0F
                                : (float) (itemStack.getMaxUseTime() - livingEntity.getItemUseTimeLeft())
                                        / (float) EnderiteCrossbow.getPullTime(itemStack);
                    }
                });
        register((Item) EnderiteMod.ENDERITE_CROSSBOW, new Identifier("pulling"),
                (itemStack, clientWorld, livingEntity) -> {
                    return livingEntity != null && livingEntity.isUsingItem()
                            && livingEntity.getActiveItem() == itemStack && !EnderiteCrossbow.isCharged(itemStack)
                                    ? 1.0F
                                    : 0.0F;
                });
        register(EnderiteMod.ENDERITE_CROSSBOW, new Identifier("charged"), (itemStack, clientWorld, livingEntity) -> {
            return livingEntity != null && EnderiteCrossbow.isCharged(itemStack) ? 1.0F : 0.0F;
        });
        register(EnderiteMod.ENDERITE_CROSSBOW, new Identifier("firework"), (itemStack, clientWorld, livingEntity) -> {
            return livingEntity != null && EnderiteCrossbow.isCharged(itemStack)
                    && EnderiteCrossbow.hasProjectile(itemStack, Items.FIREWORK_ROCKET) ? 1.0F : 0.0F;
        });
        register(EnderiteMod.ENDERITE_SHIELD, new Identifier("blocking"), (itemStack, clientWorld, livingEntity) -> {
            return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack
                    ? 1.0F
                    : 0.0F;
        });
        register(EnderiteMod.ENDERITE_ELYTRA, new Identifier("broken"), (itemStack, clientWorld, livingEntity) -> {
            return EnderiteElytraSeperated.isUsable(itemStack) ? 0.0F : 1.0F;
        });
        register(EnderiteMod.ENDERITE_ELYTRA_SEPERATED, new Identifier("broken"),
                (itemStack, clientWorld, livingEntity) -> {
                    return EnderiteElytraSeperated.isUsable(itemStack) ? 0.0F : 1.0F;
                });
    }

}