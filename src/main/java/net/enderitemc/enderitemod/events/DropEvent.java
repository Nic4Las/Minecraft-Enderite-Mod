package net.enderitemc.enderitemod.events;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.init.EnderiteModConfig;
import net.enderitemc.enderitemod.init.Registration;
import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = EnderiteMod.MOD_ID, bus = Bus.FORGE)
public class DropEvent {

    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void entityJoinEvent(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        Level world = event.getWorld();

        if (entity instanceof ItemEntity) {
            ItemStack stack = ((ItemEntity) entity).getItem();
            Item item = stack.getItem();
            if (EnderiteModConfig.ENDERITE_ITEMS_NO_GRAVITY.get() && !entity.isNoGravity() && !world.isClientSide()
                    && !stack.isEmpty() && (item.getDefaultInstance().is(EnderiteTag.ENDERITE_ITEM) || (stack.getTag() != null
                            && stack.getTag().getAsString().indexOf("tconstruct:void_floating") >= 0))) {
                entity.setNoGravity(true);
            }
            if (entity.getY() < world.getMinBuildHeight() && !world.isClientSide() && !stack.isEmpty()) {
                int i = EnchantmentHelper.getItemEnchantmentLevel(Registration.VOID_FLOATING.get(), stack);
                // Mute NullPointerException if tag not present
                try {
                    i += stack.getTag().getCompound("tic_volatile_data").getInt("tconstruct:void_floating");
                } catch (NullPointerException e) {
                }
                i = i > 3 ? 3 : i;
                boolean survives = false;
                float r = (float) Math.random();
                // LOGGER.info(r);
                if (r <= i / 3.0) {
                    survives = true;
                }
                if ((item.getDefaultInstance().is(EnderiteTag.ENDERITE_ITEM) || survives)) {
                    entity.setNoGravity(true);
                    entity.revive();
                    entity.setGlowingTag(true);
                    entity.setPos(entity.getX(), world.getMinBuildHeight()+5, entity.getZ());
                    entity.setDeltaMovement(0, 0, 0);
                }
            }
        }

    }

}