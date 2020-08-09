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
    public static Map<Item, Map<ResourceLocation, IItemPropertyGetter>> field_239415_f_;

    @Shadow
    public static void func_239418_a_(Item item, ResourceLocation id, IItemPropertyGetter provider) {
        ((Map) field_239415_f_.computeIfAbsent(item, (itemx) -> {
            return Maps.newHashMap();
        })).put(id, provider);
    }

    static {
        func_239418_a_((Item) Registration.ENDERITE_BOW.get(), new ResourceLocation("pull"),
                (itemStack, clientWorld, livingEntity) -> {
                    if (livingEntity == null) {
                        return 0.0F;
                    } else {
                        return livingEntity.getActiveItemStack() != itemStack ? 0.0F
                                : (float) (itemStack.getUseDuration() - livingEntity.getItemInUseCount())
                                        / EnderiteBow.chargeTime;
                    }
                });
        func_239418_a_((Item) Registration.ENDERITE_BOW.get(), new ResourceLocation("pulling"),
                (itemStack, clientWorld, livingEntity) -> {
                    return livingEntity != null && livingEntity.isHandActive()
                            && livingEntity.getActiveItemStack() == itemStack ? 1.0F : 0.0F;
                });
        func_239418_a_((Item) Registration.ENDERITE_CROSSBOW.get(), new ResourceLocation("pull"),
                (itemStack, clientWorld, livingEntity) -> {
                    if (livingEntity == null) {
                        return 0.0F;
                    } else {
                        return EnderiteCrossbow.isCharged(itemStack) ? 0.0F
                                : (float) (itemStack.getUseDuration() - livingEntity.getItemInUseCount())
                                        / (float) EnderiteCrossbow.getChargeTime(itemStack);
                    }
                });
        func_239418_a_((Item) Registration.ENDERITE_CROSSBOW.get(), new ResourceLocation("pulling"),
                (itemStack, clientWorld, livingEntity) -> {
                    return livingEntity != null && livingEntity.isHandActive()
                            && livingEntity.getActiveItemStack() == itemStack && !EnderiteCrossbow.isCharged(itemStack)
                                    ? 1.0F
                                    : 0.0F;
                });
        func_239418_a_(Registration.ENDERITE_CROSSBOW.get(), new ResourceLocation("charged"),
                (itemStack, clientWorld, livingEntity) -> {
                    return livingEntity != null && EnderiteCrossbow.isCharged(itemStack) ? 1.0F : 0.0F;
                });
        func_239418_a_(Registration.ENDERITE_CROSSBOW.get(), new ResourceLocation("firework"),
                (itemStack, clientWorld, livingEntity) -> {
                    return livingEntity != null && EnderiteCrossbow.isCharged(itemStack)
                            && EnderiteCrossbow.hasChargedProjectile(itemStack, Items.FIREWORK_ROCKET) ? 1.0F : 0.0F;
                });
        func_239418_a_(Registration.ENDERITE_SHIELD.get(), new ResourceLocation("blocking"),
                (itemStack, clientWorld, livingEntity) -> {
                    return livingEntity != null && livingEntity.isHandActive()
                            && livingEntity.getActiveItemStack() == itemStack ? 1.0F : 0.0F;
                });
    }

}