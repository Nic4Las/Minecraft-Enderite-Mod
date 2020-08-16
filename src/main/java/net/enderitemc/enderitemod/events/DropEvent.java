package net.enderitemc.enderitemod.events;

import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.enderitemc.enderitemod.EnderiteMod;
import net.enderitemc.enderitemod.init.Registration;
import net.enderitemc.enderitemod.misc.EnderiteTag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = EnderiteMod.MOD_ID, bus = Bus.FORGE)
public class DropEvent {

    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void playerDeathDropEvent(LivingDropsEvent event) {
        event.getDrops().forEach((e) -> {
            if (e.getPosY() < 0) {
                int i = EnchantmentHelper.getEnchantmentLevel(Registration.VOID_FLOATING.get(), e.getItem());
                boolean survives = false;
                float r = (float) Math.random();
                LOGGER.info(r);
                if (r < i / 3.0) {
                    survives = true;
                }
                if ((e.getItem().getItem().isIn(EnderiteTag.ENDERITE_ITEM) || survives)) {
                    e.setNoGravity(true);
                    e.revive();
                    e.setGlowing(true);
                    e.setPosition(e.getPosX(), 5, e.getPosZ());
                    e.setVelocity(0, 0, 0);
                }
            }
        });
    }

    @SubscribeEvent
    public static void playerDropEvent(ItemTossEvent event) {
        ItemEntity entity = event.getEntityItem();
        ItemStack stack = entity.getItem();
        Item item = stack.getItem();

        if (!entity.hasNoGravity() && !event.getPlayer().getEntityWorld().isRemote() && !stack.isEmpty()
                && item.isIn(EnderiteTag.ENDERITE_ITEM)) {
            entity.setNoGravity(true);
        }
        // Slow down enderite item y velocity (to stop vertical spread)
        if (item.isIn(EnderiteTag.ENDERITE_ITEM)) {
            entity.setMotion(entity.getMotion().mul(1.0D, 0.96D, 1.0D));
        }
    }

}