package net.enderitemc.enderitemod.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.enderitemc.enderitemod.blocks.EnderiteRespawnAnchor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

@Mixin(PlayerEntity.class)
public class EnderiteRespawnAnchorMixin {

    @Inject(at = @At("HEAD"), method = "findRespawnPosition", cancellable = true)
    private static void isEnd(ServerWorld world, BlockPos pos, boolean bl, boolean bl2,
            CallbackInfoReturnable<Optional<Vec3d>> info) {
        // Implements possibility to spawn in end with enderite respawn anchor
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();
        // isNether() method is actually checking if dimension is the end
        if (block instanceof EnderiteRespawnAnchor && (Integer) blockState.get(EnderiteRespawnAnchor.CHARGES) > 0
                && EnderiteRespawnAnchor.isNether(world)) {
            Optional<Vec3d> optional = EnderiteRespawnAnchor.findRespawnPosition(EntityType.PLAYER, world, pos);
            if (!bl2 && optional.isPresent()) {
                world.setBlockState(pos, (BlockState) blockState.with(EnderiteRespawnAnchor.CHARGES,
                        (Integer) blockState.get(EnderiteRespawnAnchor.CHARGES) - 1), 3);
            }

            info.setReturnValue(optional);
        }
    }

}