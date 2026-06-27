package net.enderitemc.enderitemod.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.enderitemc.enderitemod.EnderiteMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.equipment.ShieldModel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3fc;

import java.util.Objects;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class EnderiteShieldRenderer implements SpecialModelRenderer<DataComponentMap> {
    public static final SpriteId ENDERITE_SHIELD_BASE = new SpriteId(
        Sheets.SHIELD_SHEET, Identifier.fromNamespaceAndPath(EnderiteMod.MOD_ID, "entity/enderite_shield_base")
    );
    public static final SpriteId ENDERITE_SHIELD_BASE_NO_PATTERN = new SpriteId(
        Sheets.SHIELD_SHEET, Identifier.fromNamespaceAndPath(EnderiteMod.MOD_ID, "entity/enderite_shield_base_nopattern")
    );

    private final SpriteGetter sprites;
    private final ShieldModel model;
    private final boolean charged;

    public EnderiteShieldRenderer(SpriteGetter sprites, ShieldModel model, boolean charged) {
        this.sprites = sprites;
        this.model = model;
        this.charged = charged;
    }

    @Nullable
    public DataComponentMap extractArgument(ItemStack itemStack) {
        return itemStack.immutableComponents();
    }

    @Override
    public void submit(
        @Nullable DataComponentMap componentMap,
        PoseStack matrixStack,
        SubmitNodeCollector queue,
        int light,
        int overlay,
        boolean glint,
        int i
    ) {
        BannerPatternLayers bannerPatternsComponent = componentMap != null
            ? componentMap.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY)
            : BannerPatternLayers.EMPTY;
        DyeColor dyeColor = componentMap != null ? componentMap.get(DataComponents.BASE_COLOR) : null;
        boolean bl2 = !bannerPatternsComponent.layers().isEmpty() || dyeColor != null;
        matrixStack.pushPose();
        matrixStack.scale(1.0F, -1.0F, -1.0F);
        SpriteId spriteIdentifier = bl2 ? ENDERITE_SHIELD_BASE : ENDERITE_SHIELD_BASE_NO_PATTERN;
        queue.submitModelPart(
            this.model.handle(),
            matrixStack,
            this.model.renderType(spriteIdentifier.atlasLocation()),
            light,
            overlay,
            this.sprites.get(spriteIdentifier),
            false,
            glint,
            -1,
            null,
            i
        );
        if (bl2) {
            BannerRenderer.submitPatterns(
                this.sprites,
                matrixStack,
                queue,
                light,
                overlay,
                this.model,
                Unit.INSTANCE,
                false,
                (DyeColor) Objects.requireNonNullElse(dyeColor, DyeColor.WHITE),
                bannerPatternsComponent,
                null
            );
        } else {
            queue.submitModelPart(
                this.model.plate(),
                matrixStack,
                this.model.renderType(spriteIdentifier.atlasLocation()),
                light,
                overlay,
                this.sprites.get(spriteIdentifier),
                false,
                glint,
                -1,
                null,
                i
            );
        }
        if (this.charged) {
            // Custom end portal shader
            queue.submitCustomGeometry(
                matrixStack,
                RenderTypes.endPortal(),
                this::renderSides);
        }
        matrixStack.popPose();
    }

    @Override
    public void getExtents(Consumer<Vector3fc> consumer) {
        PoseStack matrixStack = new PoseStack();
        matrixStack.scale(1.0F, -1.0F, -1.0F);
        this.model.root().getExtentsForGui(matrixStack, consumer);
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
        public MapCodec<EnderiteShieldRenderer.Unbaked> type() {
            return CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(SpecialModelRenderer.BakingContext context) {
            return new EnderiteShieldRenderer(context.sprites(), new ShieldModel(context.entityModelSet().bakeLayer(ModelLayers.SHIELD)), charged);
        }
    }

    private void renderSides(PoseStack.Pose matrixEntry, VertexConsumer vertexConsumer) {
        Matrix4f matrix = matrixEntry.pose();
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
        vertices.addVertex(model, x1, y1, z);
        vertices.addVertex(model, x2, y1, z);
        vertices.addVertex(model, x2, y2, z);
        vertices.addVertex(model, x1, y2, z);
    }
}
