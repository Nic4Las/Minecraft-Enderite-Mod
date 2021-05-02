package net.enderitemc.enderitemod.misc;

import java.util.List;

import com.mojang.datafixers.util.Pair;

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRenderer;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.item.ShieldItem;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import net.minecraft.client.render.entity.model.ShieldEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

public class EnderiteShieldRenderer implements BuiltinItemRenderer {

        // private static ItemStack shieldStack = new
        // ItemStack(EnderiteMod.ENDERITE_BLOCK, 1);

        private final ShieldEntityModel shieldModel = new ShieldEntityModel();

        @Override
        public void render(ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
                        int overlay) {
                matrices.push();

                boolean bl = stack.getSubTag("BlockEntityTag") != null;

                if (!bl) {

                        matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180));
                        VertexConsumer vertexConsumer2 = ItemRenderer.getDirectItemGlintConsumer(vertexConsumers,
                                        shieldModel.getLayer(new Identifier(
                                                        "enderitemod:textures/entity/enderite_shield_base_nopattern.png")),
                                        false, stack.hasGlint());
                        // MinecraftClient.getInstance().getItemRenderer().renderItem(shieldStack,
                        // ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers);
                        shieldModel.render(matrices, vertexConsumer2, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);

                } else {
                        matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180));
                        SpriteIdentifier spriteIdentifier = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE,
                                        new Identifier("enderitemod:entity/enderite_shield_base"));

                        VertexConsumer vertexConsumer = spriteIdentifier.getSprite().getTextureSpecificVertexConsumer(
                                        ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, shieldModel
                                                        .getLayer(ModelLoader.SHIELD_BASE_NO_PATTERN.getAtlasId()),
                                                        true, stack.hasGlint()));
                        shieldModel.getHandle().render(matrices, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F,
                                        1.0F);
                        List<Pair<BannerPattern, DyeColor>> list = BannerBlockEntity.method_24280(
                                        ShieldItem.getColor(stack), BannerBlockEntity.getPatternListTag(stack));
                        BannerBlockEntityRenderer.renderCanvas(matrices, vertexConsumers, light, overlay,
                                        shieldModel.getPlate(), spriteIdentifier, false, list, stack.hasGlint());

                }

                // Mandatory call after GL calls
                matrices.pop();
        }

}