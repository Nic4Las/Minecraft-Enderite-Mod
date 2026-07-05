package net.enderitemc.enderitemod.mixin;

import net.enderitemc.enderitemod.blocks.EnderiteRespawnAnchor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ServerPlayer.class)
public class EnderiteRespawnAnchorMixin {

    @Inject(at = @At("HEAD"), method = "findRespawnAndUseSpawnBlock", cancellable = true)
    private static void enderitemod$isEnd(ServerLevel world, ServerPlayer.RespawnConfig respawn, boolean is_dead, CallbackInfoReturnable<Optional<ServerPlayer.RespawnPosAngle>> cir) {
        // Implements possibility to spawn in end with enderite respawn anchor
        BlockPos pos = respawn.respawnData().pos();
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();
        boolean spawnForced = respawn.forced();
        // isNether() method is actually checking if dimension is the end
        if (block instanceof EnderiteRespawnAnchor && (spawnForced || blockState.getValue(EnderiteRespawnAnchor.CHARGE) > 0)
            && EnderiteRespawnAnchor.shouldRespawnPlayer(world, is_dead)) {
            Optional<Vec3> optional = EnderiteRespawnAnchor.findStandUpPosition(EntityTypes.PLAYER, world, pos);
            if (!spawnForced && optional.isPresent()) {
                world.setBlock(pos, blockState.setValue(EnderiteRespawnAnchor.CHARGE,
                    blockState.getValue(EnderiteRespawnAnchor.CHARGE) - 1), 3);
            }

            cir.setReturnValue(optional.map(respawnPos -> ServerPlayer.RespawnPosAngle.of(respawnPos, pos, 35.0F)));
        }
    }

}
