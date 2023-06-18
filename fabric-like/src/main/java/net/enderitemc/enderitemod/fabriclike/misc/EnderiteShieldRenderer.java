package net.enderitemc.enderitemod.fabriclike.misc;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.item.ShieldItem;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import net.minecraft.client.render.entity.model.ShieldEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry.DynamicItemRenderer;

public class EnderiteShieldRenderer implements DynamicItemRenderer {

        private ModelPart pl = new ModelPart((List) ModelPartBuilder.create().uv(0, 0)
                        .cuboid(-6.0F, -11.0F, -2.0F, 12.0F, 22.0F, 1.0F).build().stream().map((modelCuboidData) -> {
                                return modelCuboidData.createCuboid(64, 64);
                        }).collect(ImmutableList.toImmutableList()), Collections.<String, ModelPart>emptyMap());
        private ModelPart ha = new ModelPart((List) ModelPartBuilder.create().uv(26, 0)
                        .cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 6.0F, 6.0F).build().stream().map((modelCuboidData) -> {
                                return modelCuboidData.createCuboid(64, 64);
                        }).collect(ImmutableList.toImmutableList()), Collections.<String, ModelPart>emptyMap());

        private Map<String, ModelPart> m = Map.of("plate", pl, "handle", ha);
        private final ShieldEntityModel shieldModel = new ShieldEntityModel(
                        new ModelPart((List) ModelPartBuilder.create().build().stream().map((modelCuboidData) -> {
                                return modelCuboidData.createCuboid(64, 64);
                        }).collect(ImmutableList.toImmutableList()), /* Collections.<String, ModelPart>emptyMap() */m));

        @Override
        public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices,
                        VertexConsumerProvider vertexConsumers, int light,
                        int overlay) {
                matrices.push();

                boolean bl = stack.getSubNbt("BlockEntityTag") != null;

                if (!bl) {

                        matrices.scale(1.0f, -1.0f, -1.0f);
                        VertexConsumer vertexConsumer2 = ItemRenderer.getDirectItemGlintConsumer(vertexConsumers,
                                        shieldModel.getLayer(new Identifier(
                                                        "enderitemod:textures/entity/enderite_shield_base_nopattern.png")),
                                        false, stack.hasGlint());
                        // MinecraftClient.getInstance().getItemRenderer().renderItem(shieldStack,
                        // ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers);
                        shieldModel.render(matrices, vertexConsumer2, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);

                } else {
                        matrices.scale(1.0f, -1.0f, -1.0f);
                        SpriteIdentifier spriteIdentifier = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE,
                                        new Identifier("enderitemod:entity/enderite_shield_base"));

                        VertexConsumer vertexConsumer = spriteIdentifier.getSprite().getTextureSpecificVertexConsumer(
                                        ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, shieldModel
                                                        .getLayer(ModelLoader.SHIELD_BASE_NO_PATTERN.getAtlasId()),
                                                        true, stack.hasGlint()));
                        shieldModel.getHandle().render(matrices, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F,
                                        1.0F);
                        List<Pair<RegistryEntry<BannerPattern>, DyeColor>> list = BannerBlockEntity.getPatternsFromNbt(
                                        ShieldItem.getColor(stack), BannerBlockEntity.getPatternListNbt(stack));
                        BannerBlockEntityRenderer.renderCanvas(matrices, vertexConsumers, light, overlay,
                                        shieldModel.getPlate(), spriteIdentifier, false, list, stack.hasGlint());

                }

                // Mandatory call after GL calls
                matrices.pop();
        }

}