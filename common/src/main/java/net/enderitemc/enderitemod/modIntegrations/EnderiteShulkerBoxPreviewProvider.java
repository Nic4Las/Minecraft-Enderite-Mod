package net.enderitemc.enderitemod.modIntegrations;

import com.misterpemodder.shulkerboxtooltip.api.PreviewContext;
import com.misterpemodder.shulkerboxtooltip.api.color.ColorKey;
import com.misterpemodder.shulkerboxtooltip.api.provider.BlockEntityPreviewProvider;
import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class EnderiteShulkerBoxPreviewProvider extends BlockEntityPreviewProvider {

    public EnderiteShulkerBoxPreviewProvider(int maxInvSize, boolean canUseLootTables) {
        super(45, true);
    }

    public static final PreviewProvider INSTANCE = new EnderiteShulkerBoxPreviewProvider(45, true);

    @Environment(EnvType.CLIENT)
    public ColorKey getWindowColorKey(PreviewContext context) {
        return ShulkerBoxTooltipApiImplementation.ClientOnly.ENDERITE_COLOR_KEY;
    }
}