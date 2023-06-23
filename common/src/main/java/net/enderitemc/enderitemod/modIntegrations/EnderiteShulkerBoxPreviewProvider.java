package net.enderitemc.enderitemod.modIntegrations;

import com.misterpemodder.shulkerboxtooltip.api.provider.BlockEntityPreviewProvider;
import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProvider;

public class EnderiteShulkerBoxPreviewProvider extends BlockEntityPreviewProvider {

    public EnderiteShulkerBoxPreviewProvider(int maxInvSize, boolean canUseLootTables) {
        super(45, true);
    }

    public static final PreviewProvider INSTANCE = new EnderiteShulkerBoxPreviewProvider(45, true);
}