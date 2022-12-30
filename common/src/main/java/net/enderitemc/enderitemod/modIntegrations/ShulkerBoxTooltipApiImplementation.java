package net.enderitemc.enderitemod.modIntegrations;

import com.misterpemodder.shulkerboxtooltip.api.ShulkerBoxTooltipApi;
import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProviderRegistry;

import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.util.Identifier;

public class ShulkerBoxTooltipApiImplementation implements ShulkerBoxTooltipApi {

    @Override
    public void registerProviders(PreviewProviderRegistry registry) {
        registry.register(new Identifier("enderitemod", "enderite_shulker_box"),
                EnderiteShulkerBoxPreviewProvider.INSTANCE,
                EnderiteMod.ENDERITE_SHULKER_BOX.get().asItem());
    }

}