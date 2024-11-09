package net.enderitemc.enderitemod.forge.item;

import com.google.common.base.Suppliers;
import net.enderitemc.enderitemod.renderer.EnderiteShieldRenderer;
import net.enderitemc.enderitemod.tools.EnderiteShield;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class EnderiteShieldForge extends EnderiteShield {

    public EnderiteShieldForge(Settings builder) {
        super(builder);
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            final Supplier<BuiltinModelItemRenderer> renderer = Suppliers.memoize(() -> EnderiteShieldRenderer.INSTANCE);

            @Override
            public @NotNull BuiltinModelItemRenderer getCustomRenderer() {
                return renderer.get();
            }
        });
    }

}