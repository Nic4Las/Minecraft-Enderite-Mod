package net.enderitemc.enderitemod.mixin;

import net.enderitemc.enderitemod.component.EnderiteChargeComponent;
import net.enderitemc.enderitemod.component.EnderiteDataComponents;
import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.enderitemc.enderitemod.tools.EnderiteShield;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class EnderiteShieldPlayerEntityMixin extends LivingEntity {

    protected EnderiteShieldPlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Shadow
    public abstract ItemCooldowns getCooldowns();

    @Inject(at = @At("HEAD"), method = "blockUsingItem")
    private void enderitemod$portIt(ServerLevel level, LivingEntity attacker, DamageSource source, float damage, CallbackInfo ci) {
        if (this.isShiftKeyDown() && this.useItem.getItem() instanceof EnderiteShield
            && !this.getCooldowns().isOnCooldown(this.useItem)) {

            int charge = this.useItem.getOrDefault(EnderiteDataComponents.TELEPORT_CHARGE.get(), 0).intValue();

            if (!level.isClientSide() && charge > 0 && !attacker.getType().builtInRegistryHolder().is(EnderiteTag.UNAFFECTED_BY_ENDERITE_SHIELD)) {
                double d = attacker.getX();
                double e = attacker.getY();
                double f = attacker.getZ();

                double yaw = (double) this.yHeadRot;
                double pitch = (double) this.getXRot();

                // x: 1 = -90, -1 = 90
                // y: 1 = -90, -1 = 90
                // z: 1 = 0, -1 = 180
                double temp = Math.cos(Math.toRadians(pitch));
                double dX = temp * -Math.sin(Math.toRadians(yaw));
                double dY = -Math.sin(Math.toRadians(pitch));
                double dZ = temp * Math.cos(Math.toRadians(yaw));
                double distance = 10.0;

                for (int i = 0; i < 16; ++i) {
                    double g = attacker.getX() + dX * distance + (attacker.getRandom().nextDouble() - 0.5D) * 16.0D;
                    double h = Mth.clamp(
                        attacker.getY() + dY * distance + (double) (attacker.getRandom().nextInt(16) - 8), 0.0D,
                        (double) (level.getHeight() - 1));
                    double j = attacker.getZ() + dZ * distance + (attacker.getRandom().nextDouble() - 0.5D) * 16.0D;
                    if (attacker.isPassenger()) {
                        attacker.stopRiding();
                    }

                    if (attacker.randomTeleport(g, h, j, true)) {
                        SoundEvent soundEvent = attacker instanceof Fox ? SoundEvents.FOX_TELEPORT
                            : SoundEvents.CHORUS_FRUIT_TELEPORT;
                        level.playSound((Player) null, d, e, f, soundEvent, SoundSource.PLAYERS, 1.0F, 1.0F);
                        attacker.playSound(soundEvent, 1.0F, 1.0F);

                        this.useItem.set(EnderiteDataComponents.TELEPORT_CHARGE.get(), EnderiteChargeComponent.of(charge - 1));
                        this.getCooldowns().addCooldown(this.useItem, 128);
                        break;
                    }
                }

            }
        }
    }
}
