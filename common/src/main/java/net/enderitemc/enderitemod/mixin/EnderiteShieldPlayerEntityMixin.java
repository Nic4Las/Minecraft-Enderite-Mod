package net.enderitemc.enderitemod.mixin;

import net.enderitemc.enderitemod.component.EnderiteChargeComponent;
import net.enderitemc.enderitemod.component.EnderiteDataComponents;
import net.enderitemc.enderitemod.tools.EnderiteShield;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.ElderGuardianEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class EnderiteShieldPlayerEntityMixin extends LivingEntity {

    protected EnderiteShieldPlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    public abstract ItemCooldownManager getItemCooldownManager();

    @Inject(at = @At("HEAD"), method = "takeShieldHit")
    private void portIt(ServerWorld world, LivingEntity attacker, CallbackInfo ci) {
        if (this.isSneaking() && this.activeItemStack.getItem() instanceof EnderiteShield
            && !this.getItemCooldownManager().isCoolingDown(this.activeItemStack)) {

            int charge = this.activeItemStack.getOrDefault(EnderiteDataComponents.TELEPORT_CHARGE.get(), 0).intValue();

            if (!world.isClient() && charge > 0 && !(attacker instanceof EnderDragonEntity
                || attacker instanceof WitherEntity || attacker instanceof ElderGuardianEntity)) {
                double d = attacker.getX();
                double e = attacker.getY();
                double f = attacker.getZ();

                double yaw = (double) this.headYaw;
                double pitch = (double) this.getPitch();

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
                    double h = MathHelper.clamp(
                        attacker.getY() + dY * distance + (double) (attacker.getRandom().nextInt(16) - 8), 0.0D,
                        (double) (world.getHeight() - 1));
                    double j = attacker.getZ() + dZ * distance + (attacker.getRandom().nextDouble() - 0.5D) * 16.0D;
                    if (attacker.hasVehicle()) {
                        attacker.stopRiding();
                    }

                    if (attacker.teleport(g, h, j, true)) {
                        SoundEvent soundEvent = attacker instanceof FoxEntity ? SoundEvents.ENTITY_FOX_TELEPORT
                            : SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT;
                        world.playSound((PlayerEntity) null, d, e, f, soundEvent, SoundCategory.PLAYERS, 1.0F, 1.0F);
                        attacker.playSound(soundEvent, 1.0F, 1.0F);

                        this.activeItemStack.set(EnderiteDataComponents.TELEPORT_CHARGE.get(), EnderiteChargeComponent.of(charge - 1));
                        this.getItemCooldownManager().set(this.activeItemStack, 128);
                        break;
                    }
                }

            }
        }
    }
}