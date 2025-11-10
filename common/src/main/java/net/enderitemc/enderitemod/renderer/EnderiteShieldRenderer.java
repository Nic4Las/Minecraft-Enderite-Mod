package net.enderitemc.enderitemod.renderer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.enderitemc.enderitemod.EnderiteMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.ShieldEntityModel;
import net.minecraft.client.render.item.model.special.SpecialModelRenderer;
import net.minecraft.client.texture.SpriteHolder;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Objects;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class EnderiteShieldRenderer implements SpecialModelRenderer<ComponentMap> {
    public static final SpriteIdentifier ENDERITE_SHIELD_BASE = new SpriteIdentifier(
        TexturedRenderLayers.SHIELD_PATTERNS_ATLAS_TEXTURE, Identifier.of(EnderiteMod.MOD_ID, "entity/enderite_shield_base")
    );
    public static final SpriteIdentifier ENDERITE_SHIELD_BASE_NO_PATTERN = new SpriteIdentifier(
        TexturedRenderLayers.SHIELD_PATTERNS_ATLAS_TEXTURE, Identifier.of(EnderiteMod.MOD_ID, "entity/enderite_shield_base_nopattern")
    );

    private final SpriteHolder spriteHolder;
    private final ShieldEntityModel model;
    private final boolean charged;

    public EnderiteShieldRenderer(SpriteHolder spriteHolder, ShieldEntityModel model, boolean charged) {
        this.spriteHolder = spriteHolder;
        this.model = model;
        this.charged = charged;
    }

    @Nullable
    public ComponentMap getData(ItemStack itemStack) {
        return itemStack.getImmutableComponents();
    }

    @Override
    public void render(
        @Nullable ComponentMap componentMap,
        ItemDisplayContext displayContext,
        MatrixStack matrixStack,
        OrderedRenderCommandQueue queue,
        int light,
        int overlay,
        boolean glint,
        int i
    ) {
        BannerPatternsComponent bannerPatternsComponent = componentMap != null
            ? componentMap.getOrDefault(DataComponentTypes.BANNER_PATTERNS, BannerPatternsComponent.DEFAULT)
            : BannerPatternsComponent.DEFAULT;
        DyeColor dyeColor = componentMap != null ? componentMap.get(DataComponentTypes.BASE_COLOR) : null;
        boolean bl2 = !bannerPatternsComponent.layers().isEmpty() || dyeColor != null;
        matrixStack.push();
        matrixStack.scale(1.0F, -1.0F, -1.0F);
        SpriteIdentifier spriteIdentifier = bl2 ? ENDERITE_SHIELD_BASE : ENDERITE_SHIELD_BASE_NO_PATTERN;
        queue.submitModelPart(
            this.model.getHandle(),
            matrixStack,
            this.model.getLayer(spriteIdentifier.getAtlasId()),
            light,
            overlay,
            this.spriteHolder.getSprite(spriteIdentifier),
            false,
            glint,
            -1,
            null,
            i
        );
        if (bl2) {
            BannerBlockEntityRenderer.renderCanvas(
                this.spriteHolder,
                matrixStack,
                queue,
                light,
                overlay,
                this.model,
                Unit.INSTANCE,
                spriteIdentifier,
                false,
                (DyeColor) Objects.requireNonNullElse(dyeColor, DyeColor.WHITE),
                bannerPatternsComponent,
                glint,
                null,
                i
            );
        } else {
            queue.submitModelPart(
                this.model.getPlate(),
                matrixStack,
                this.model.getLayer(spriteIdentifier.getAtlasId()),
                light,
                overlay,
                this.spriteHolder.getSprite(spriteIdentifier),
                false,
                glint,
                -1,
                null,
                i
            );
        }
        if (this.charged &&
            (displayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND
                || displayContext == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)) {
            // Custom end portal shader
            queue.submitCustom(
                matrixStack,
                RenderLayer.getEndPortal(),
                this::renderSides);
        }
        matrixStack.pop();
    }

    @Override
    public void collectVertices(Set<Vector3f> vertices) {
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.scale(1.0F, -1.0F, -1.0F);
        this.model.getRootPart().collectVertices(matrixStack, vertices);
    }

    @Environment(EnvType.CLIENT)
    public record Unbaked(boolean charged) implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<EnderiteShieldRenderer.Unbaked> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.BOOL.optionalFieldOf("charged", false).forGetter(EnderiteShieldRenderer.Unbaked::charged)
                )
                .apply(instance, EnderiteShieldRenderer.Unbaked::new)
        );

        @Override
        public MapCodec<EnderiteShieldRenderer.Unbaked> getCodec() {
            return CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(SpecialModelRenderer.BakeContext context) {
            return new EnderiteShieldRenderer(context.spriteHolder(), new ShieldEntityModel(context.entityModelSet().getModelPart(EntityModelLayers.SHIELD)), charged);
        }
    }

    private void renderSides(MatrixStack.Entry matrixEntry, VertexConsumer vertexConsumer) {
        Matrix4f matrix = matrixEntry.getPositionMatrix();
        float border = 1.0f;
        float f = (-6.0F + border) / 16.0f;
        float g = (-11.0F + border) / 16.0f;
        float h = -2.0F / 16.0f;
        float df = (12.0F - 2 * border) / 16.0f;
        float dg = (22.0F - 2 * border) / 16.0f;
        float dh = -0.001F / 16.0f;
        //, , , 12.0F, 22.0F, 1.0F
        this.renderSide(matrix, vertexConsumer, f, f + df, g + dg, g, h + dh);
    }

    private void renderSide(Matrix4f model, VertexConsumer vertices, float x1, float x2, float y1, float y2, float z) {
        vertices.vertex(model, x1, y1, z);
        vertices.vertex(model, x2, y1, z);
        vertices.vertex(model, x2, y2, z);
        vertices.vertex(model, x1, y2, z);
    }
}