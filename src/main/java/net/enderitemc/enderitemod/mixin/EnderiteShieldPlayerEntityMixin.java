package net.enderitemc.enderitemod.mixin;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.enderitemc.enderitemod.tools.EnderiteShield;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.ElderGuardianEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class EnderiteShieldPlayerEntityMixin extends LivingEntity {

    protected EnderiteShieldPlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    public abstract void incrementStat(Stat<?> stat);

    @Shadow
    public abstract ItemCooldownManager getItemCooldownManager();

    @Inject(at = @At("HEAD"), method = "damageShield")
    private void damageIt(float amount, CallbackInfo info) {
        if (this.activeItemStack.getItem() instanceof EnderiteShield) {
            if (!this.world.isClient) {
                this.incrementStat(Stats.USED.getOrCreateStat(this.activeItemStack.getItem()));
            }

            if (amount >= 3.0F) {
                int i = 1 + MathHelper.floor(amount);
                Hand hand = this.getActiveHand();
                this.activeItemStack.damage(i, (LivingEntity) this, (Consumer<LivingEntity>) ((playerEntity) -> {
                    playerEntity.sendToolBreakStatus(hand);
                }));
                if (this.activeItemStack.isEmpty()) {
                    if (hand == Hand.MAIN_HAND) {
                        this.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                    } else {
                        this.equipStack(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                    }

                    this.activeItemStack = ItemStack.EMPTY;
                    this.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + this.world.random.nextFloat() * 0.4F);
                }
            }

        }
    }

    @Inject(at = @At("HEAD"), method = "takeShieldHit")
    private void portIt(LivingEntity attacker, CallbackInfo info) {
        if (this.isSneaking() && this.activeItemStack.getItem() instanceof EnderiteShield
                && !this.getItemCooldownManager().isCoolingDown(this.activeItemStack.getItem())) {

            int charge = 0;
            if (this.activeItemStack.getTag().contains("teleport_charge")) {
                charge = Integer.parseInt(this.activeItemStack.getTag().get("teleport_charge").asString());
            }

            if (!world.isClient && charge > 0 && !(attacker instanceof EnderDragonEntity
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

                        this.activeItemStack.getTag().putInt("teleport_charge", charge - 1);
                        this.getItemCooldownManager().set(this.activeItemStack.getItem(), 128);
                        break;
                    }
                }

            }
        }
    }
}