package net.enderitemc.enderitemod;

import net.enderitemc.enderitemod.misc.EnderiteElytraFeatureRender;
import net.enderitemc.enderitemod.misc.EnderiteShieldRenderer;
import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxBlockEntityRenderer;
import net.enderitemc.enderitemod.tools.EnderiteCrossbow;
import net.enderitemc.enderitemod.tools.EnderiteElytraSeperated;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class EnderiteModClient implements ClientModInitializer {

        @Override
        public void onInitializeClient() {
                BlockEntityRendererFactories.register(EnderiteMod.ENDERITE_SHULKER_BOX_BLOCK_ENTITY,
                                EnderiteShulkerBoxBlockEntityRenderer::new);
                BuiltinItemRendererRegistry.INSTANCE.register(EnderiteMod.ENDERITE_SHIELD,
                                new EnderiteShieldRenderer());
                // ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)
                // .register((atlaxTexture, registry) -> {
                // if (atlaxTexture.getId() == SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE) {
                // registry.register(new Identifier(
                // "enderitemod:entity/enderite_shield_base"));
                // }
                // });

                ModelPredicateProviderRegistry.register(EnderiteMod.ENDERITE_BOW.asItem(), new Identifier("pull"),
                                (itemStack, clientWorld, livingEntity, seed) -> {
                                        if (livingEntity == null) {
                                                return 0.0F;
                                        } else {
                                                return livingEntity.getActiveItem() != itemStack ? 0.0F
                                                                : (float) (itemStack.getMaxUseTime()
                                                                                - livingEntity.getItemUseTimeLeft())
                                                                                / 20.0F;
                                        }
                                });
                ModelPredicateProviderRegistry.register(EnderiteMod.ENDERITE_BOW.asItem(), new Identifier("pulling"),
                                (itemStack, clientWorld, livingEntity, seed) -> {
                                        return livingEntity != null && livingEntity.isUsingItem()
                                                        && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
                                });

                ModelPredicateProviderRegistry.register(EnderiteMod.ENDERITE_CROSSBOW.asItem(), new Identifier("pull"),
                                (itemStack, clientWorld, livingEntity, seed) -> {
                                        if (livingEntity == null) {
                                                return 0.0F;
                                        } else {
                                                return EnderiteCrossbow.isCharged(itemStack) ? 0.0F
                                                                : (float) (itemStack.getMaxUseTime()
                                                                                - livingEntity.getItemUseTimeLeft())
                                                                                / (float) EnderiteCrossbow
                                                                                                .getPullTime(itemStack);
                                        }
                                });
                ModelPredicateProviderRegistry.register(EnderiteMod.ENDERITE_CROSSBOW.asItem(),
                                new Identifier("pulling"),
                                (itemStack, clientWorld, livingEntity, seed) -> {
                                        return livingEntity != null && livingEntity.isUsingItem()
                                                        && livingEntity.getActiveItem() == itemStack
                                                        && !EnderiteCrossbow.isCharged(itemStack)
                                                                        ? 1.0F
                                                                        : 0.0F;
                                });
                ModelPredicateProviderRegistry.register(EnderiteMod.ENDERITE_CROSSBOW.asItem(),
                                new Identifier("charged"),
                                (itemStack, clientWorld, livingEntity, seed) -> {
                                        return livingEntity != null && EnderiteCrossbow.isCharged(itemStack) ? 1.0F
                                                        : 0.0F;
                                });
                ModelPredicateProviderRegistry.register(EnderiteMod.ENDERITE_CROSSBOW.asItem(),
                                new Identifier("firework"), (itemStack, clientWorld, livingEntity, seed) -> {
                                        return livingEntity != null && EnderiteCrossbow.isCharged(itemStack)
                                                        && EnderiteCrossbow.hasProjectile(itemStack,
                                                                        Items.FIREWORK_ROCKET) ? 1.0F : 0.0F;
                                });
                ModelPredicateProviderRegistry.register(EnderiteMod.ENDERITE_SHIELD.asItem(),
                                new Identifier("blocking"),
                                (itemStack, clientWorld, livingEntity, seed) -> {
                                        return livingEntity != null && livingEntity.isUsingItem()
                                                        && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
                                });
                ModelPredicateProviderRegistry.register(EnderiteMod.ENDERITE_ELYTRA.asItem(), new Identifier("broken"),
                                (itemStack, clientWorld, livingEntity, seed) -> {
                                        return EnderiteElytraSeperated.isUsable(itemStack) ? 0.0F : 1.0F;
                                });
                ModelPredicateProviderRegistry.register(EnderiteMod.ENDERITE_ELYTRA_SEPERATED.asItem(),
                                new Identifier("broken"), (itemStack, clientWorld, livingEntity, seed) -> {
                                        return EnderiteElytraSeperated.isUsable(itemStack) ? 0.0F : 1.0F;
                                });

                LivingEntityFeatureRendererRegistrationCallback.EVENT
                                .register((entityType, entityRenderer, registrationHelper, context) -> {
                                        registrationHelper
                                                        .register(new EnderiteElytraFeatureRender<>(entityRenderer,
                                                                        context.getModelLoader()));
                                });
                LivingEntityFeatureRenderEvents.ALLOW_CAPE_RENDER.register((player) -> allowCapeRender(player));

        }

        @Environment(EnvType.CLIENT)
        private static final boolean allowCapeRender(AbstractClientPlayerEntity player) {
                return !(player.getEquippedStack(EquipmentSlot.CHEST).isIn(EnderiteTag.ENDERITE_ELYTRA));
        }

}