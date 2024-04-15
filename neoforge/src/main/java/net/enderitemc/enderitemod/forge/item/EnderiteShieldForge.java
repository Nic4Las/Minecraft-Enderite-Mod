package net.enderitemc.enderitemod.forge.item;

import java.util.List;
import java.util.function.Consumer;

import net.enderitemc.enderitemod.forge.renderer.EnderiteShieldRenderer;
import net.enderitemc.enderitemod.tools.EnderiteShield;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.common.util.NonNullLazy;

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