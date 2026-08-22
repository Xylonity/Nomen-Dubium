package dev.xylonity.nomendubium.registry;

import dev.xylonity.nomendubium.NomenDubium;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Supplier;

public final class NomenDubiumCreativeTabs {

    public static void init() {
        ;;
    }

    public static final Supplier<CreativeModeTab> CREATIVE_TAB = NomenDubium.PLATFORM.registerCreativeTab(
        "nomendubiumtab",
        Component.translatable("creativeTab.nomendubium.tab"),
        () -> new ItemStack(NomenDubiumItems.ENCASED_FOSSIL.get()),
        List.of(
            () -> new ItemStack(NomenDubiumItems.PALEONTOLOGY_TABLE.get()),
            () -> new ItemStack(NomenDubiumItems.ENCASED_FOSSIL.get()),
            () -> new ItemStack(NomenDubiumItems.HUNTERS_ARROW.get()),
            () -> new ItemStack(NomenDubiumItems.PREHISTORIC_MAW.get()),
            () -> new ItemStack(NomenDubiumItems.FOSSILISED_MAW.get()),
            () -> new ItemStack(NomenDubiumItems.REGENERATING_CHOP.get()),
            () -> NomenDubiumItems.FOSSIL.get().createStack("shielded_head"),
            () -> NomenDubiumItems.FOSSIL.get().createStack("avian_body")
        )

    );

}
