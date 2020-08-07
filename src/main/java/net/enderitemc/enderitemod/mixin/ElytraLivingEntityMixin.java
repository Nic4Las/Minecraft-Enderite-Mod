package net.enderitemc.enderitemod.mixin;

import net.enderitemc.enderitemod.init.Registration;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntity.class)
public abstract class ElytraLivingEntityMixin extends Entity {

    @Shadow
    public abstract ItemStack getItemStackFromSlot(EquipmentSlotType type);

    public ElytraLivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setFlag(IZ)V"), method = "updateElytra", index = 1)
    private boolean flagSevenFix(boolean value) {
        // Mixin to overwrite check of flag 7
        boolean bl = this.getFlag(7);
        if (bl && !this.onGround && !this.isPassenger()) {
            ItemStack itemStack = this.getItemStackFromSlot(EquipmentSlotType.CHEST);
            return itemStack.getItem() == Registration.ENDERITE_ELYTRA.get() || value;
        }
        return value;
    }
}