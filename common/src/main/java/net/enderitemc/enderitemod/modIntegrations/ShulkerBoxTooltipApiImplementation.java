package net.enderitemc.enderitemod.modIntegrations;

import com.misterpemodder.shulkerboxtooltip.api.ShulkerBoxTooltipApi;
import com.misterpemodder.shulkerboxtooltip.api.color.ColorKey;
import com.misterpemodder.shulkerboxtooltip.api.color.ColorRegistry;
import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProviderRegistry;

import net.enderitemc.enderitemod.EnderiteMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

public class ShulkerBoxTooltipApiImplementation implements ShulkerBoxTooltipApi {

    @Override
    public void registerProviders(PreviewProviderRegistry registry) {
        registry.register(new Identifier("enderitemod", "enderite_shulker_box"),
                EnderiteShulkerBoxPreviewProvider.INSTANCE,
                EnderiteMod.ENDERITE_SHULKER_BOX.get().asItem());
    }

    public static final ColorKey ENDERITE_COLOR_KEY = ColorKey.ofRgb(new float[] { (float) 20 * 2.5f / 255.0F, (float) 66 * 2.5f / 255.0F, (float) 58 * 2.5f / 255.0F });

    @Environment(EnvType.CLIENT)
    public void registerColors(ColorRegistry registry) {
        registry.category(new Identifier("enderitemod","enderite_category")).register(ENDERITE_COLOR_KEY, "enderite_color");
    }

}