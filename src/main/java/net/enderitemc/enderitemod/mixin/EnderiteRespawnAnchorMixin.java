package net.enderitemc.enderitemod.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.enderitemc.enderitemod.block.EnderiteRespawnAnchor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

@Mixin(PlayerEntity.class)
public class EnderiteRespawnAnchorMixin {

    @Inject(at = @At("HEAD"), method = "func_234567_a_", cancellable = true)
    private static void isEnd(ServerWorld world, BlockPos pos, boolean bl, boolean bl2,
            CallbackInfoReturnable<Optional<Vector3d>> info) {
        // Implements possibility to spawn in end with enderite respawn anchor
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();
        // isNether() method is actually checking if dimension is the end
        if (block instanceof EnderiteRespawnAnchor
                && (Integer) blockState.get(EnderiteRespawnAnchor.field_235559_a_) > 0
                && EnderiteRespawnAnchor.isNether(world)) {
            Optional<Vector3d> optional = EnderiteRespawnAnchor.func_235560_a_(EntityType.PLAYER, world, pos);
            if (!bl2 && optional.isPresent()) {
                world.setBlockState(pos, (BlockState) blockState.with(EnderiteRespawnAnchor.field_235559_a_,
                        (Integer) blockState.get(EnderiteRespawnAnchor.field_235559_a_) - 1), 3);
            }

            info.setReturnValue(optional);
        }
    }

}