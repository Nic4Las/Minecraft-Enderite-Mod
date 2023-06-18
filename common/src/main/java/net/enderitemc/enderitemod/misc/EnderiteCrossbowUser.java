package net.enderitemc.enderitemod.misc;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.tools.EnderiteCrossbow;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public interface EnderiteCrossbowUser extends CrossbowUser {

    @Override
    default void shoot(LivingEntity entity, float speed) {
        Hand hand = ProjectileUtil.getHandPossiblyHolding(entity, EnderiteMod.ENDERITE_CROSSBOW.get());
        ItemStack itemStack = entity.getStackInHand(hand);
        if (entity.isHolding(EnderiteMod.ENDERITE_CROSSBOW.get())) {
            EnderiteCrossbow.shootAll(entity.world, entity, hand, itemStack, speed,
                    (float) (14 - entity.world.getDifficulty().getId() * 4));
        }

        this.postShoot();
    }
}
