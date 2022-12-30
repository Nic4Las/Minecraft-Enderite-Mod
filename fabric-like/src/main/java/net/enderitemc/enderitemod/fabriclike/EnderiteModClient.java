package net.enderitemc.enderitemod.fabriclike;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.fabriclike.misc.EnderiteShieldRenderer;
import net.enderitemc.enderitemod.fabriclike.tools.EnderiteElytraChestplate;
import net.enderitemc.enderitemod.fabriclike.tools.EnderiteElytraSeperated;
import net.enderitemc.enderitemod.misc.EnderiteElytraFeatureRender;
import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxBlockEntity;
import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxBlockEntityRenderer;
import net.enderitemc.enderitemod.tools.EnderiteCrossbow;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class EnderiteModClient implements ClientModInitializer {

        @Override
        public void onInitializeClient() {
                BlockEntityRendererRegistry.register(
                                (BlockEntityType<EnderiteShulkerBoxBlockEntity>) EnderiteMod.ENDERITE_SHULKER_BOX_BLOCK_ENTITY
                                                .get(),
                                EnderiteShulkerBoxBlockEntityRenderer::new);
                BuiltinItemRendererRegistry.INSTANCE.register(EnderiteMod.ENDERITE_SHIELD.get(),
                                new EnderiteShieldRenderer());
                ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)
                                .register((atlaxTexture, registry) -> {
                                        if (atlaxTexture.getId() == SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE) {
                                                registry.register(new Identifier(
                                                                "enderitemod:entity/enderite_shield_base"));
                                        }
                                });

                ModelPredicateProviderRegistry.register(EnderiteMod.ENDERITE_BOW.get().asItem(), new Identifier("pull"),
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
                ModelPredicateProviderRegistry.register(EnderiteMod.ENDERITE_BOW.get().asItem(),
                                new Identifier("pulling"),
                                (itemStack, clientWorld, livingEntity, seed) -> {
                                        return livingEntity != null && livingEntity.isUsingItem()
                                                        && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
                                });

                ModelPredicateProviderRegistry.register(EnderiteMod.ENDERITE_CROSSBOW.get().asItem(),
                                new Identifier("pull"),
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
                ModelPredicateProviderRegistry.register(EnderiteMod.ENDERITE_CROSSBOW.get().asItem(),
                                new Identifier("pulling"),
                                (itemStack, clientWorld, livingEntity, seed) -> {
                                        return livingEntity != null && livingEntity.isUsingItem()
                                                        && livingEntity.getActiveItem() == itemStack
                                                        && !EnderiteCrossbow.isCharged(itemStack)
                                                                        ? 1.0F
                                                                        : 0.0F;
                                });
                ModelPredicateProviderRegistry.register(EnderiteMod.ENDERITE_CROSSBOW.get().asItem(),
                                new Identifier("charged"),
                                (itemStack, clientWorld, livingEntity, seed) -> {
                                        return livingEntity != null && EnderiteCrossbow.isCharged(itemStack) ? 1.0F
                                                        : 0.0F;
                                });
                ModelPredicateProviderRegistry.register(EnderiteMod.ENDERITE_CROSSBOW.get().asItem(),
                                new Identifier("firework"), (itemStack, clientWorld, livingEntity, seed) -> {
                                        return livingEntity != null && EnderiteCrossbow.isCharged(itemStack)
                                                        && EnderiteCrossbow.hasProjectile(itemStack,
                                                                        Items.FIREWORK_ROCKET) ? 1.0F : 0.0F;
                                });
                ModelPredicateProviderRegistry.register(EnderiteMod.ENDERITE_SHIELD.get().asItem(),
                                new Identifier("blocking"),
                                (itemStack, clientWorld, livingEntity, seed) -> {
                                        return livingEntity != null && livingEntity.isUsingItem()
                                                        && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
                                });
                ModelPredicateProviderRegistry.register(EnderiteMod.ENDERITE_ELYTRA.get().asItem(),
                                new Identifier("broken"),
                                (itemStack, clientWorld, livingEntity, seed) -> {
                                        return EnderiteElytraChestplate.isUsable(itemStack) ? 0.0F : 1.0F;
                                });
                ModelPredicateProviderRegistry.register(EnderiteMod.ENDERITE_ELYTRA_SEPERATED.get().asItem(),
                                new Identifier("broken"), (itemStack, clientWorld, livingEntity, seed) -> {
                                        return EnderiteElytraSeperated.isUsable(itemStack) ? 0.0F : 1.0F;
                                });

                LivingEntityFeatureRendererRegistrationCallback.EVENT
                                .register((entityType, entityRenderer, registrationHelper, context) -> {
                                        registrationHelper
                                                        .register(new EnderiteElytraFeatureRender<>(entityRenderer,
                                                                        context.getModelLoader()));
                                });
                LivingEntityFeatureRenderEvents.ALLOW_CAPE_RENDER.register((player) -> this.allowCapeRender(player));

        }

        private static final boolean allowCapeRender(AbstractClientPlayerEntity player) {
                return !(player.getEquippedStack(EquipmentSlot.CHEST).isIn(EnderiteTag.ENDERITE_ELYTRA));
        }

}