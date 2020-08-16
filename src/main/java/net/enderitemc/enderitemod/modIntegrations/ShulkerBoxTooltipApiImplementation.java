package net.enderitemc.enderitemod.modIntegrations;

import java.util.List;
import java.util.Map;

import com.misterpemodder.shulkerboxtooltip.api.ShulkerBoxTooltipApi;
import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProvider;

import net.enderitemc.enderitemod.EnderiteMod;
import net.minecraft.item.Item;

public class ShulkerBoxTooltipApiImplementation implements ShulkerBoxTooltipApi {

    @Override
    public String getModId() {
        return "enderitemod";
    }

    @Override
    public void registerProviders(Map<PreviewProvider, List<Item>> previewProviders) {
        List<Item> list = List.of(EnderiteMod.ENDERITE_SHULKER_BOX.getItemStack().getItem());
        previewProviders.put(EnderiteShulkerBoxPreviewProvider.INSTANCE, list);
    }

}