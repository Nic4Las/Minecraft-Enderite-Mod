package net.enderitemc.enderitemod.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.enderitemc.enderitemod.block.EnderiteRespawnAnchor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerLevel;

@Mixin(Player.class)
public class EnderiteRespawnAnchorMixin {

    @Inject(at = @At("HEAD"), method = "findRespawnPositionAndUseSpawnBlock", cancellable = true)
    private static void isEnd(ServerLevel world, BlockPos pos, float f, boolean bl, boolean bl2,
            CallbackInfoReturnable<Optional<Vec3>> info) {
        // Implements possibility to spawn in end with enderite respawn anchor
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();
        // isNether() method is actually checking if dimension is the end
        if (block instanceof EnderiteRespawnAnchor
                && (Integer) blockState.getValue(EnderiteRespawnAnchor.CHARGE) > 0
                && EnderiteRespawnAnchor.isNether(world)) {
            Optional<Vec3> optional = EnderiteRespawnAnchor.findStandUpPosition(EntityType.PLAYER, world, pos);
            if (!bl2 && optional.isPresent()) {
                world.setBlock(pos, (BlockState) blockState.setValue(EnderiteRespawnAnchor.CHARGE,
                        (Integer) blockState.getValue(EnderiteRespawnAnchor.CHARGE) - 1), 3);
            }

            info.setReturnValue(optional);
        }
    }

}