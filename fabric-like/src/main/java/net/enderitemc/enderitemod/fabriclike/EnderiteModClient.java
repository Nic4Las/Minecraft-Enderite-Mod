package net.enderitemc.enderitemod.fabriclike;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.blocks.RespawnAnchorUtils.EnderiteRespawnAnchorRenderer;
import net.enderitemc.enderitemod.fabriclike.misc.EnderiteShieldRenderer;
import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.enderitemc.enderitemod.shulker.EnderiteShulkerBoxBlockEntityRenderer;
import net.enderitemc.enderitemod.tools.EnderiteBow;
import net.enderitemc.enderitemod.tools.EnderiteCrossbow;
import net.enderitemc.enderitemod.tools.EnderiteTools;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class EnderiteModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(EnderiteMod.ENDERITE_SHULKER_BOX_BLOCK_ENTITY.get(),
            EnderiteShulkerBoxBlockEntityRenderer::new);
        BuiltinItemRendererRegistry.INSTANCE.register(EnderiteTools.ENDERITE_SHIELD.get(),
            new EnderiteShieldRenderer());

        BlockEntityRendererFactories.register(EnderiteMod.ENDERITE_RESPAWN_ANCHOR_BLOCK_ENTITY.get(),
            EnderiteRespawnAnchorRenderer::new);


        ModelPredicateProviderRegistry.register(EnderiteTools.ENDERITE_BOW.get().asItem(), Identifier.of("pull"),
            (itemStack, clientWorld, livingEntity, seed) -> {
                if (livingEntity == null) {
                    return 0.0F;
                } else {
                    return livingEntity.getActiveItem() != itemStack ? 0.0F
                        : EnderiteBow.getPullProgress(itemStack.getMaxUseTime(livingEntity)
                        - livingEntity.getItemUseTimeLeft());
                }
            });
        ModelPredicateProviderRegistry.register(EnderiteTools.ENDERITE_BOW.get().asItem(),
            Identifier.of("pulling"),
            (itemStack, clientWorld, livingEntity, seed) -> {
                return livingEntity != null && livingEntity.isUsingItem()
                    && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
            });

        ModelPredicateProviderRegistry.register(EnderiteTools.ENDERITE_CROSSBOW.get().asItem(),
            Identifier.of("pull"),
            (itemStack, clientWorld, livingEntity, seed) -> {
                if (livingEntity == null) {
                    return 0.0F;
                } else {
                    return EnderiteCrossbow.isCharged(itemStack) ? 0.0F
                        : (float) (itemStack.getMaxUseTime(livingEntity)
                        - livingEntity.getItemUseTimeLeft())
                        / (float) EnderiteCrossbow
                        .getPullTime(itemStack, livingEntity);
                }
            });
        ModelPredicateProviderRegistry.register(EnderiteTools.ENDERITE_CROSSBOW.get().asItem(),
            Identifier.of("pulling"),
            (itemStack, clientWorld, livingEntity, seed) -> {
                return livingEntity != null && livingEntity.isUsingItem()
                    && livingEntity.getActiveItem() == itemStack
                    && !EnderiteCrossbow.isCharged(itemStack)
                    ? 1.0F
                    : 0.0F;
            });
        ModelPredicateProviderRegistry.register(EnderiteTools.ENDERITE_CROSSBOW.get().asItem(),
            Identifier.of("charged"),
            (itemStack, clientWorld, livingEntity, seed) -> {
                return livingEntity != null && EnderiteCrossbow.isCharged(itemStack) ? 1.0F
                    : 0.0F;
            });
        ModelPredicateProviderRegistry.register(EnderiteTools.ENDERITE_CROSSBOW.get().asItem(),
            Identifier.of("firework"), (itemStack, clientWorld, livingEntity, seed) -> {
                ChargedProjectilesComponent chargedProjectilesComponent = itemStack.get(DataComponentTypes.CHARGED_PROJECTILES);
                return chargedProjectilesComponent != null && chargedProjectilesComponent.contains(Items.FIREWORK_ROCKET) ? 1.0f : 0.0f;
            });
        ModelPredicateProviderRegistry.register(EnderiteTools.ENDERITE_SHIELD.get().asItem(),
            Identifier.of("blocking"),
            (itemStack, clientWorld, livingEntity, seed) -> {
                return livingEntity != null && livingEntity.isUsingItem()
                    && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
            });
        ModelPredicateProviderRegistry.register(EnderiteMod.ENDERITE_ELYTRA.get().asItem(),
            Identifier.of("broken"),
            (itemStack, clientWorld, livingEntity, seed) -> {
                return LivingEntity.canGlideWith(itemStack, EquipmentSlot.BODY) ? 0.0F : 1.0F;
            });
        ModelPredicateProviderRegistry.register(EnderiteMod.ENDERITE_ELYTRA_SEPERATED.get().asItem(),
            Identifier.of("broken"), (itemStack, clientWorld, livingEntity, seed) -> {
                return LivingEntity.canGlideWith(itemStack, EquipmentSlot.BODY) ? 0.0F : 1.0F;
            });

//        LivingEntityFeatureRendererRegistrationCallback.EVENT
//            .register((entityType, entityRenderer, registrationHelper, context) -> {
//                registrationHelper
//                    .register(new EnderiteElytraFeatureRender<>(entityRenderer,
//                        context.getModelLoader()));
//            });
//        LivingEntityFeatureRenderEvents.ALLOW_CAPE_RENDER.register((player) -> this.allowCapeRender(player));

    }

    private static final boolean allowCapeRender(AbstractClientPlayerEntity player) {
        return !(player.getEquippedStack(EquipmentSlot.CHEST).isIn(EnderiteTag.ENDERITE_ELYTRA));
    }

}