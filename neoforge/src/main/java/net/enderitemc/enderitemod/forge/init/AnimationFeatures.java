package net.enderitemc.enderitemod.forge.init;

import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.forge.tools.EnderiteElytraChestplate;
import net.enderitemc.enderitemod.forge.tools.EnderiteElytraSeperated;
import net.enderitemc.enderitemod.tools.EnderiteBow;
import net.enderitemc.enderitemod.tools.EnderiteCrossbow;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class AnimationFeatures {
        private static final Logger LOGGER = LogManager.getLogger();

        public static void init() {
                ModelPredicateProviderRegistry.register((Item) EnderiteTools.ENDERITE_BOW.get(), new Identifier("pull"),
                                (itemStack, clientWorld, livingEntity, id) -> {
                                        if (livingEntity == null) {
                                                return 0.0F;
                                        } else {
                                                return livingEntity.getActiveItem() != itemStack ? 0.0F
                                                                : (float) (itemStack.getMaxUseTime() - livingEntity
                                                                                .getItemUseTimeLeft())
                                                                                / EnderiteBow.chargeTime;
                                        }
                                });
                ModelPredicateProviderRegistry.register((Item) EnderiteTools.ENDERITE_BOW.get(),
                                new Identifier("pulling"),
                                (itemStack, clientWorld, livingEntity, id) -> {
                                        return livingEntity != null && livingEntity.isUsingItem()
                                                        && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
                                });

                ModelPredicateProviderRegistry.register((Item) EnderiteTools.ENDERITE_CROSSBOW.get(),
                                new Identifier("pull"),
                                (itemStack, clientWorld, livingEntity, id) -> {
                                        if (livingEntity == null) {
                                                return 0.0F;
                                        } else {
                                                return EnderiteCrossbow.isCharged(itemStack) ? 0.0F
                                                                : (float) (itemStack.getMaxUseTime() - livingEntity
                                                                                .getItemUseTimeLeft())
                                                                                / (float) EnderiteCrossbow
                                                                                                .getPullTime(itemStack);
                                        }
                                });
                ModelPredicateProviderRegistry.register((Item) EnderiteTools.ENDERITE_CROSSBOW.get(),
                                new Identifier("pulling"), (itemStack, clientWorld, livingEntity, id) -> {
                                        return livingEntity != null && livingEntity.isUsingItem()
                                                        && livingEntity.getActiveItem() == itemStack
                                                        && !EnderiteCrossbow.isCharged(itemStack) ? 1.0F : 0.0F;
                                });
                ModelPredicateProviderRegistry.register(EnderiteTools.ENDERITE_CROSSBOW.get(), new Identifier("charged"),
                                (itemStack, clientWorld, livingEntity, id) -> {
                                        return livingEntity != null && EnderiteCrossbow.isCharged(itemStack) ? 1.0F
                                                        : 0.0F;
                                });
                ModelPredicateProviderRegistry.register(EnderiteTools.ENDERITE_CROSSBOW.get(), new Identifier("firework"),
                                (itemStack, clientWorld, livingEntity, id) -> {
                                    ChargedProjectilesComponent chargedProjectilesComponent = itemStack.get(DataComponentTypes.CHARGED_PROJECTILES);
                                    return chargedProjectilesComponent != null && chargedProjectilesComponent.contains(Items.FIREWORK_ROCKET) ? 1.0F : 0.0F;
                                });
                ModelPredicateProviderRegistry.register(EnderiteTools.ENDERITE_SHIELD.get(), new Identifier("blocking"),
                                (itemStack, clientWorld, livingEntity, id) -> {
                                        return livingEntity != null && livingEntity.isUsingItem()
                                                        && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
                                });

                ModelPredicateProviderRegistry.register(EnderiteMod.ENDERITE_ELYTRA.get(), new Identifier("broken"),
                                (itemStack, clientWorld, livingEntity, id) -> {
                                        return EnderiteElytraChestplate.isUsable(itemStack) ? 0.0F : 1.0F;
                                });
                ModelPredicateProviderRegistry.register(EnderiteMod.ENDERITE_ELYTRA_SEPERATED.get().asItem(),
                                new Identifier("broken"), (itemStack, clientWorld, livingEntity, seed) -> {
                                        return EnderiteElytraSeperated.isUsable(itemStack) ? 0.0F : 1.0F;
                                });
        }

}
