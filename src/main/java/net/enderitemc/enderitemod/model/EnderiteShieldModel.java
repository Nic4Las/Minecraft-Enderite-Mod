package net.enderitemc.enderitemod.model;

import java.util.List;
import java.util.Random;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

public class EnderiteShieldModel extends ShieldModel implements BakedModel {

    public EnderiteShieldModel(ModelPart part) {
        super(part);
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean useAmbientOcclusion() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isGui3d() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean usesBlockLight() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isCustomRenderer() {
        return true;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ItemOverrides getOverrides() {
        // TODO Auto-generated method stub
        return null;
    }
}