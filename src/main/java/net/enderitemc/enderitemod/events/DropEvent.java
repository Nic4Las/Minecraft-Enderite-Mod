package net.enderitemc.enderitemod.events;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.init.EnderiteModConfig;
import net.enderitemc.enderitemod.init.Registration;
import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
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
        World world = event.getWorld();

        if (entity instanceof ItemEntity) {
            ItemStack stack = ((ItemEntity) entity).getItem();
            Item item = stack.getItem();
            if (EnderiteModConfig.ENDERITE_ITEMS_NO_GRAVITY.get() && !entity.isNoGravity() && !world.isClientSide()
                    && !stack.isEmpty() && (item.is(EnderiteTag.ENDERITE_ITEM)
                            || stack.getOrCreateTag().getAsString().indexOf("tconstruct:void_floating") >= 0)) {
                entity.setNoGravity(true);
            }
            if (entity.getY() < 0 && !world.isClientSide() && !stack.isEmpty()) {
                int i = EnchantmentHelper.getItemEnchantmentLevel(Registration.VOID_FLOATING.get(), stack);
                i += stack.getOrCreateTag().getCompound("tic_volatile_data").getInt("tconstruct:void_floating");
                i = i > 3 ? 3 : i;
                boolean survives = false;
                float r = (float) Math.random();
                // LOGGER.info(r);
                if (r <= i / 3.0) {
                    survives = true;
                }
                if ((item.is(EnderiteTag.ENDERITE_ITEM) || survives)) {
                    entity.setNoGravity(true);
                    entity.revive();
                    entity.setGlowing(true);
                    entity.setPos(entity.getX(), 5, entity.getZ());
                    entity.setDeltaMovement(0, 0, 0);
                }
            }
        }

    }

}