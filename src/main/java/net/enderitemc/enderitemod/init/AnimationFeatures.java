package net.enderitemc.enderitemod.init;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.enderitemc.enderitemod.item.EnderiteBow;
import net.enderitemc.enderitemod.item.EnderiteCrossbow;
import net.enderitemc.enderitemod.item.EnderiteElytra;
import net.minecraft.world.item.Item;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.item.Items;
import net.minecraft.resources.ResourceLocation;

public class AnimationFeatures {
        private static final Logger LOGGER = LogManager.getLogger();

        public static void init() {
                ItemProperties.register((Item) Registration.ENDERITE_BOW.get(), new ResourceLocation("pull"),
                                (itemStack, clientWorld, livingEntity, id) -> {
                                        if (livingEntity == null) {
                                                return 0.0F;
                                        } else {
                                                return livingEntity.getUseItem() != itemStack ? 0.0F
                                                                : (float) (itemStack.getUseDuration() - livingEntity
                                                                                .getUseItemRemainingTicks())
                                                                                / EnderiteBow.chargeTime;
                                        }
                                });
                ItemProperties.register((Item) Registration.ENDERITE_BOW.get(), new ResourceLocation("pulling"),
                                (itemStack, clientWorld, livingEntity, id) -> {
                                        return livingEntity != null && livingEntity.isUsingItem()
                                                        && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F;
                                });

                ItemProperties.register((Item) Registration.ENDERITE_CROSSBOW.get(), new ResourceLocation("pull"),
                                (itemStack, clientWorld, livingEntity, id) -> {
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
                ItemProperties.register((Item) Registration.ENDERITE_CROSSBOW.get(),
                                new ResourceLocation("pulling"), (itemStack, clientWorld, livingEntity, id) -> {
                                        return livingEntity != null && livingEntity.isUsingItem()
                                                        && livingEntity.getUseItem() == itemStack
                                                        && !EnderiteCrossbow.isCharged(itemStack) ? 1.0F : 0.0F;
                                });
                ItemProperties.register(Registration.ENDERITE_CROSSBOW.get(), new ResourceLocation("charged"),
                                (itemStack, clientWorld, livingEntity, id) -> {
                                        return livingEntity != null && EnderiteCrossbow.isCharged(itemStack) ? 1.0F
                                                        : 0.0F;
                                });
                ItemProperties.register(Registration.ENDERITE_CROSSBOW.get(), new ResourceLocation("firework"),
                                (itemStack, clientWorld, livingEntity, id) -> {
                                        return livingEntity != null && EnderiteCrossbow.isCharged(itemStack)
                                                        && EnderiteCrossbow.containsChargedProjectile(itemStack,
                                                                        Items.FIREWORK_ROCKET) ? 1.0F : 0.0F;
                                });
                ItemProperties.register(Registration.ENDERITE_SHIELD.get(), new ResourceLocation("blocking"),
                                (itemStack, clientWorld, livingEntity, id) -> {
                                        return livingEntity != null && livingEntity.isUsingItem()
                                                        && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F;
                                });

                ItemProperties.register(Registration.ENDERITE_ELYTRA.get(), new ResourceLocation("broken"),
                                (itemStack, clientWorld, livingEntity, id) -> {
                                        return EnderiteElytra.isUsable(itemStack) ? 0.0F : 1.0F;
                                });
        }

}
