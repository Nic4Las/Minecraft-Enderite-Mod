package net.enderitemc.enderitemod.mixin;

import net.enderitemc.enderitemod.misc.EnderiteSulfurCubeRenderState;
import net.minecraft.client.renderer.entity.state.SulfurCubeRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SulfurCubeRenderState.class)
public class SulfurCubeRenderStateMixin implements EnderiteSulfurCubeRenderState {
    @Unique
    private boolean enderitemod$crackedEnderiteOre;

    @Override
    public void enderitemod$setCrackedEnderiteOre(boolean crackedEnderiteOre) {
        this.enderitemod$crackedEnderiteOre = crackedEnderiteOre;
    }

    @Override
    public boolean enderitemod$isCrackedEnderiteOre() {
        return this.enderitemod$crackedEnderiteOre;
    }
}
