package net.enderitemc.enderitemod.mixin;

import net.enderitemc.enderitemod.init.Registration;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.network.play.ClientPlayNetHandler;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(ClientPlayerEntity.class)
public abstract class ElytraClientPlayerEntityMixin extends ElytraPlayerEntityMixin {

    protected ElytraClientPlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    @Final
    public ClientPlayNetHandler connection;

    @Inject(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/entity/player/ClientPlayerEntity;getItemStackFromSlot(Lnet/minecraft/inventory/EquipmentSlotType;)Lnet/minecraft/item/ItemStack;"), method = "livingTick")
    public void tickMovement(CallbackInfo info) {
        ItemStack itemStack = this.getItemStackFromSlot(EquipmentSlotType.CHEST);
        if (itemStack.getItem() == Registration.ENDERITE_ELYTRA.get() && this.tryToStartFallFlying()) {
            // Send packet that player is fall flying when he got enderite elytra and should
            // be fall flyin
            this.connection.sendPacket(new CEntityActionPacket(this, CEntityActionPacket.Action.START_FALL_FLYING));
        }
    }
}