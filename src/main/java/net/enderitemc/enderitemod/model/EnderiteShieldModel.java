package net.enderitemc.enderitemod.model;

import java.util.List;
import java.util.Random;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.ShieldModel;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;

public class EnderiteShieldModel extends ShieldModel implements IBakedModel {

    public EnderiteShieldModel() {
        super();
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isAmbientOcclusion() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isGui3d() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean func_230044_c_() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return true;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ItemOverrideList getOverrides() {
        // TODO Auto-generated method stub
        return null;
    }
}