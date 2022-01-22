package net.enderitemc.enderitemod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;

//DEPRECATED
//May be used in the future

@Mixin(PlayerEntity.class)
public abstract class EnderiteArmorMixin extends LivingEntity {

    @Shadow
    public abstract Iterable<ItemStack> getArmorItems();

    protected EnderiteArmorMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void hasFullEnderiteArmor(CallbackInfo info) {
        boolean notFull = false;
        for (ItemStack item : getArmorItems()) {
            if (!(EnderiteTag.ENDERITE_ITEM.contains(item.getItem())) && item.getItem() != Items.ELYTRA) {
                notFull = true;
            }
        }
        if (notFull == false && (isSneaking())) {
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 5, 0, false, false, false));
        }
    }

}