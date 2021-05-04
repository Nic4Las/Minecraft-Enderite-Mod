package net.enderitemc.enderitemod.init;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.enderitemc.enderitemod.item.EnderiteBow;
import net.enderitemc.enderitemod.item.EnderiteCrossbow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

public class AnimationFeatures {
        private static final Logger LOGGER = LogManager.getLogger();

        public static void init() {
                ItemModelsProperties.register((Item) Registration.ENDERITE_BOW.get(), new ResourceLocation("pull"),
                                (itemStack, clientWorld, livingEntity) -> {
                                        if (livingEntity == null) {
                                                return 0.0F;
                                        } else {
                                                return livingEntity.getUseItem() != itemStack ? 0.0F
                                                                : (float) (itemStack.getUseDuration() - livingEntity
                                                                                .getUseItemRemainingTicks())
                                                                                / EnderiteBow.chargeTime;
                                        }
                                });
                ItemModelsProperties.register((Item) Registration.ENDERITE_BOW.get(), new ResourceLocation("pulling"),
                                (itemStack, clientWorld, livingEntity) -> {
                                        return livingEntity != null && livingEntity.isUsingItem()
                                                        && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F;
                                });

                ItemModelsProperties.register((Item) Registration.ENDERITE_CROSSBOW.get(), new ResourceLocation("pull"),
                                (itemStack, clientWorld, livingEntity) -> {
                                        if (livingEntity == null) {
                                                return 0.0F;
                                        } else {
                                                return EnderiteCrossbow.isCharged(itemStack) ? 0.0F
                                                                : (float) (itemStack.getUseDuration() - livingEntity
                                                                                .getUseItemRemainingTicks())
                                                                                / (float) EnderiteCrossbow
                                                                                                .getChargeTime(itemStack);
                                        }
                                });
                ItemModelsProperties.register((Item) Registration.ENDERITE_CROSSBOW.get(),
                                new ResourceLocation("pulling"), (itemStack, clientWorld, livingEntity) -> {
                                        return livingEntity != null && livingEntity.isUsingItem()
                                                        && livingEntity.getUseItem() == itemStack
                                                        && !EnderiteCrossbow.isCharged(itemStack) ? 1.0F : 0.0F;
                                });
                ItemModelsProperties.register(Registration.ENDERITE_CROSSBOW.get(), new ResourceLocation("charged"),
                                (itemStack, clientWorld, livingEntity) -> {
                                        return livingEntity != null && EnderiteCrossbow.isCharged(itemStack) ? 1.0F
                                                        : 0.0F;
                                });
                ItemModelsProperties.register(Registration.ENDERITE_CROSSBOW.get(), new ResourceLocation("firework"),
                                (itemStack, clientWorld, livingEntity) -> {
                                        return livingEntity != null && EnderiteCrossbow.isCharged(itemStack)
                                                        && EnderiteCrossbow.containsChargedProjectile(itemStack,
                                                                        Items.FIREWORK_ROCKET) ? 1.0F : 0.0F;
                                });
                ItemModelsProperties.register(Registration.ENDERITE_SHIELD.get(), new ResourceLocation("blocking"),
                                (itemStack, clientWorld, livingEntity) -> {
                                        return livingEntity != null && livingEntity.isUsingItem()
                                                        && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F;
                                });
        }

}
