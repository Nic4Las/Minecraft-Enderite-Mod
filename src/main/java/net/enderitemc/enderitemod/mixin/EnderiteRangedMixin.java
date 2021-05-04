package net.enderitemc.enderitemod.mixin;

import java.util.Map;

import com.google.common.collect.Maps;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.enderitemc.enderitemod.init.Registration;
import net.enderitemc.enderitemod.item.EnderiteBow;
import net.enderitemc.enderitemod.item.EnderiteCrossbow;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@Mixin(ItemModelsProperties.class)
public abstract class EnderiteRangedMixin {

    @Shadow
    public static Map<Item, Map<ResourceLocation, IItemPropertyGetter>> PROPERTIES;

    @Shadow
    public static void register(Item item, ResourceLocation id, IItemPropertyGetter provider) {
        ((Map) PROPERTIES.computeIfAbsent(item, (itemx) -> {
            return Maps.newHashMap();
        })).put(id, provider);
    }

    static {
        // register((Item) Registration.ENDERITE_BOW.get(), new
        // ResourceLocation("pull"),
        // (itemStack, clientWorld, livingEntity) -> {
        // if (livingEntity == null) {
        // return 0.0F;
        // } else {
        // return livingEntity.getUseItem() != itemStack ? 0.0F
        // : (float) (itemStack.getUseDuration() -
        // livingEntity.getUseItemRemainingTicks())
        // / EnderiteBow.chargeTime;
        // }
        // });
        // register((Item) Registration.ENDERITE_BOW.get(), new
        // ResourceLocation("pulling"),
        // (itemStack, clientWorld, livingEntity) -> {
        // return livingEntity != null && livingEntity.isUsingItem()
        // && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F;
        // });
        register((Item) Registration.ENDERITE_CROSSBOW.get(), new ResourceLocation("pull"),
                (itemStack, clientWorld, livingEntity) -> {
                    if (livingEntity == null) {
                        return 0.0F;
                    } else {
                        return EnderiteCrossbow.isCharged(itemStack) ? 0.0F
                                : (float) (itemStack.getUseDuration() - livingEntity.getUseItemRemainingTicks())
                                        / (float) EnderiteCrossbow.getChargeTime(itemStack);
                    }
                });
        register((Item) Registration.ENDERITE_CROSSBOW.get(), new ResourceLocation("pulling"),
                (itemStack, clientWorld, livingEntity) -> {
                    return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack
                            && !EnderiteCrossbow.isCharged(itemStack) ? 1.0F : 0.0F;
                });
        register(Registration.ENDERITE_CROSSBOW.get(), new ResourceLocation("charged"),
                (itemStack, clientWorld, livingEntity) -> {
                    return livingEntity != null && EnderiteCrossbow.isCharged(itemStack) ? 1.0F : 0.0F;
                });
        register(Registration.ENDERITE_CROSSBOW.get(), new ResourceLocation("firework"),
                (itemStack, clientWorld, livingEntity) -> {
                    return livingEntity != null && EnderiteCrossbow.isCharged(itemStack)
                            && EnderiteCrossbow.containsChargedProjectile(itemStack, Items.FIREWORK_ROCKET) ? 1.0F
                                    : 0.0F;
                });
        register(Registration.ENDERITE_SHIELD.get(), new ResourceLocation("blocking"),
                (itemStack, clientWorld, livingEntity) -> {
                    return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack
                            ? 1.0F
                            : 0.0F;
                });
    }

}