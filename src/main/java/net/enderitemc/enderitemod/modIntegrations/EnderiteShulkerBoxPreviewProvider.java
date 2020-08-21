package net.enderitemc.enderitemod.modIntegrations;

import com.misterpemodder.shulkerboxtooltip.api.provider.BlockEntityPreviewProvider;
import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProvider;

import net.minecraft.item.ItemStack;

public class EnderiteShulkerBoxPreviewProvider extends BlockEntityPreviewProvider {

    public EnderiteShulkerBoxPreviewProvider(int maxInvSize, boolean canUseLootTables) {
        super(45, true);
    }

    public static final PreviewProvider INSTANCE = new EnderiteShulkerBoxPreviewProvider(45, true);

    @Override
    public float[] getWindowColor(ItemStack stack) {
        return new float[] { (float) 20 * 2.5f / 255.0F, (float) 66 * 2.5f / 255.0F, (float) 58 * 2.5f / 255.0F };
    }

}