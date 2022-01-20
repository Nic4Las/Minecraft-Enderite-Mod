package net.enderitemc.enderitemod.mixin;

import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.enderitemc.enderitemod.tools.EnderiteElytraSeperated;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntity.class)
public abstract class ElytraLivingEntityMixin extends Entity {

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    public ElytraLivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setFlag(IZ)V"), method = "tickFallFlying", index = 1)
    private boolean flagSevenFix(boolean value) {
        // Mixin to overwrite check of flag 7
        boolean bl = this.getFlag(7);
        if (bl && !this.isOnGround() && !this.hasVehicle()) {
            ItemStack itemStack = this.getEquippedStack(EquipmentSlot.CHEST);
            if (EnderiteTag.ENDERITE_ELYTRA.contains(itemStack.getItem()) && EnderiteElytraSeperated.isUsable(itemStack)) {
                if (this.random.nextFloat() > 0.96) {
                    itemStack.damage(1, (LivingEntity) (Entity) this, (Consumer<LivingEntity>) ((livingEntity) -> {
                        livingEntity.sendEquipmentBreakStatus(EquipmentSlot.CHEST);
                    }));
                }
            }
            return EnderiteTag.ENDERITE_ELYTRA.contains(itemStack.getItem()) || value;
        }
        return value;
    }
}