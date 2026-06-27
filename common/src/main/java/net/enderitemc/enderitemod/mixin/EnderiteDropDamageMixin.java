package net.enderitemc.enderitemod.mixin;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class EnderiteDropDamageMixin extends Entity {

    @Shadow
    public abstract ItemStack getItem();

    protected EnderiteDropDamageMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void enderitemod$damageItem(CallbackInfo info) {
        // Items in the void get teleported up and will live on (because they float)
        if (this.getY() < this.level().getMinY()) {
            int i = 0;

            // Survival rate with Void Floating Enchantment
            var enchant = this.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(EnderiteMod.VOID_FLOATING_ENCHANTMENT_ID);
            if (enchant.isPresent()) {
                i += EnchantmentHelper.getItemEnchantmentLevel(enchant.get(), getItem());
            }

            // Survival rate with Enderite Armor Trim
            ArmorTrim trim = getItem().get(DataComponents.TRIM);
            if (trim != null && trim.material().getRegisteredName().equals("enderitemod:enderite")) {
                i += 1;
            }

            // Calculate survival for chance
            float r = random.nextFloat();
            boolean survives = r < i / 3.0f;

            // Always survives if in Enderite Item Tag
            if (getItem().is(EnderiteTag.ENDERITE_ITEM) || survives) {
                this.unsetRemoved(); //this.removed = false;
                // ItemEntity itemEntity = new ItemEntity(this.world, this.getX(), 10,
                // this.getZ(), getStack());
                this.teleportTo(this.getX(), this.level().getMinY() + 10, this.getZ());
                this.setDeltaMovement(0, 0, 0);
                this.setNoGravity(true);
                this.setGlowingTag(true);
            }
        }
    }
}
