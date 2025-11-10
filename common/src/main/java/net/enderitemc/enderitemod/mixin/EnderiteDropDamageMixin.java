package net.enderitemc.enderitemod.mixin;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.trim.ArmorTrim;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class EnderiteDropDamageMixin extends Entity {

    @Shadow
    public abstract ItemStack getStack();

    protected EnderiteDropDamageMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void damageItem(CallbackInfo info) {
        // Items in the void get teleported up and will live on (because they float)
        if (this.getY() < this.getEntityWorld().getBottomY()) {
            int i = 0;

            // Survival rate with Void Floating Enchantment
            var enchant = this.getEntityWorld().getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getEntry(EnderiteMod.VOID_FLOATING_ENCHANTMENT_ID);
            if (enchant.isPresent()) {
                i += EnchantmentHelper.getLevel(enchant.get(), getStack());
            }

            // Survival rate with Enderite Armor Trim
            ArmorTrim trim = getStack().get(DataComponentTypes.TRIM);
            if (trim != null && trim.material().getIdAsString().equals("enderitemod:enderite")) {
                i += 1;
            }

            // Calculate survival for chance
            float r = random.nextFloat();
            boolean survives = r < i / 3.0f;

            // Always survives if in Enderite Item Tag
            if (getStack().isIn(EnderiteTag.ENDERITE_ITEM) || survives) {
                this.unsetRemoved(); //this.removed = false;
                // ItemEntity itemEntity = new ItemEntity(this.world, this.getX(), 10,
                // this.getZ(), getStack());
                this.requestTeleport(this.getX(), this.getEntityWorld().getBottomY() + 10, this.getZ());
                this.setVelocity(0, 0, 0);
                this.setNoGravity(true);
                this.setGlowing(true);
            }
        }
    }
}