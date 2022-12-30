package net.enderitemc.enderitemod.forge.item;

import java.util.List;
import java.util.function.Consumer;

import net.enderitemc.enderitemod.forge.renderer.EnderiteShieldRenderer;
import net.enderitemc.enderitemod.materials.EnderiteMaterial;
import net.enderitemc.enderitemod.tools.EnderiteShield;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.ShieldItem;

public class EnderiteShieldForge extends EnderiteShield {

    public EnderiteShieldForge(Settings builder) {
        super(builder);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            final NonNullLazy<BuiltinModelItemRenderer> renderer = NonNullLazy
                    .of(() -> EnderiteShieldRenderer.INSTANCE);

            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                return renderer.get();
            }
        });
    }

}