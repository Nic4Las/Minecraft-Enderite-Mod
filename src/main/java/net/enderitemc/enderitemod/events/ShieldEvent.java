package net.enderitemc.enderitemod.events;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.item.EnderiteShield;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = EnderiteMod.MOD_ID, bus = Bus.FORGE)
public class ShieldEvent {

    @SubscribeEvent
    public static void playerGettingAttacked(LivingAttackEvent event) {
        if (event.getEntityLiving() instanceof Player) {
            boolean flag = false;
            float f1 = 0.0F;
            float amount = event.getAmount();
            if (amount > 0.0F && canBlockDamageSource(event.getEntityLiving(), event.getSource())) {
                damageShield((Player) event.getEntityLiving(), amount);
                f1 = amount;
                amount = 0.0F;
                if (!event.getSource().isProjectile()) {
                    Entity entity = event.getSource().getDirectEntity();
                    if (entity instanceof LivingEntity) {
                        blockUsingShield((Player) event.getEntityLiving(), (LivingEntity) entity);
                    }
                }

                flag = true;
            }
        }
    }

    private static boolean canBlockDamageSource(LivingEntity blockingEntity, DamageSource damageSourceIn) {
        Entity entity = damageSourceIn.getDirectEntity();
        boolean flag = false;
        if (entity instanceof AbstractArrow) {
            AbstractArrow abstractarrowentity = (AbstractArrow) entity;
            if (abstractarrowentity.getPierceLevel() > 0) {
                flag = true;
            }
        }

        if (!damageSourceIn.isBypassArmor() && blockingEntity.isBlocking() && !flag) {
            Vec3 vector3d2 = damageSourceIn.getSourcePosition();
            if (vector3d2 != null) {
                Vec3 vector3d = blockingEntity.getViewVector(1.0F);
                Vec3 vector3d1 = vector3d2.vectorTo(blockingEntity.position()).normalize();
                vector3d1 = new Vec3(vector3d1.x, 0.0D, vector3d1.z);
                if (vector3d1.dot(vector3d) < 0.0D) {
                    return true;
                }
            }
        }

        return false;
    }

    protected static void damageShield(Player blockingPlayer, float damage) {
        if (blockingPlayer.getUseItem().isShield(blockingPlayer)) {
            if (!blockingPlayer.level.isClientSide) {
                blockingPlayer.awardStat(Stats.ITEM_USED.get(blockingPlayer.getUseItem().getItem()));
            }

            if (damage >= 3.0F) {
                int i = 1 + Mth.floor(damage);
                InteractionHand hand = blockingPlayer.getUsedItemHand();
                blockingPlayer.getUseItem().hurtAndBreak(i, blockingPlayer, (p_213833_1_) -> {
                    p_213833_1_.broadcastBreakEvent(hand);
                    net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(blockingPlayer,
                            blockingPlayer.getUseItem(), hand);
                });
                if (blockingPlayer.getUseItem().isEmpty()) {
                    if (hand == InteractionHand.MAIN_HAND) {
                        blockingPlayer.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                    } else {
                        blockingPlayer.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                    }

                    blockingPlayer.getUseItem().setCount(0);
                    blockingPlayer.playSound(SoundEvents.SHIELD_BREAK, 0.8F,
                            0.8F + blockingPlayer.level.random.nextFloat() * 0.4F);
                }
            }

        }
    }

    protected static void blockUsingShield(Player blockingPlayer, LivingEntity attacker) {
        // LivingEntity.blockUsingShield(entityIn);
        if (attacker.getMainHandItem().canDisableShield(blockingPlayer.getUseItem(), blockingPlayer, attacker)) {
            blockingPlayer.disableShield(true);
        } else {
            if (blockingPlayer.isShiftKeyDown() && blockingPlayer.getUseItem().getItem() instanceof EnderiteShield
                    && !blockingPlayer.getCooldowns().isOnCooldown(blockingPlayer.getUseItem().getItem())) {

                int charge = 0;
                if (blockingPlayer.getUseItem().getOrCreateTag().contains("teleport_charge")) {
                    charge = Integer
                            .parseInt(blockingPlayer.getUseItem().getOrCreateTag().get("teleport_charge").toString());
                }

                if (!blockingPlayer.level.isClientSide && charge > 0 && !(attacker instanceof EnderDragon
                        || attacker instanceof WitherBoss || attacker instanceof ElderGuardian)) {
                    double d = attacker.getX();
                    double e = attacker.getY();
                    double f = attacker.getZ();

                    double yaw = (double) blockingPlayer.getYRot();
                    double pitch = (double) blockingPlayer.getXRot();

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
                                (double) (blockingPlayer.getCommandSenderWorld().getHeight() - 1));
                        double j = attacker.getZ() + dZ * distance + (attacker.getRandom().nextDouble() - 0.5D) * 16.0D;
                        if (attacker.isPassenger()) {
                            attacker.stopRiding();
                        }

                        if (attacker.randomTeleport(g, h, j, true)) {
                            SoundEvent soundEvent = attacker instanceof Fox ? SoundEvents.FOX_TELEPORT
                                    : SoundEvents.CHORUS_FRUIT_TELEPORT;
                            blockingPlayer.getCommandSenderWorld().playSound((Player) null, d, e, f, soundEvent,
                                    SoundSource.PLAYERS, 1.0F, 1.0F);
                            attacker.playSound(soundEvent, 1.0F, 1.0F);

                            blockingPlayer.getUseItem().getOrCreateTag().putInt("teleport_charge", charge - 1);
                            blockingPlayer.getCooldowns().addCooldown(blockingPlayer.getUseItem().getItem(), 128);
                            break;
                        }
                    }

                }
            }
        }

    }
}
