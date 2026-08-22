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
            () -> new ItemStack(NomenDubiumItems.AMBER.get()),
            () -> new ItemStack(NomenDubiumItems.FOSSIL_BONE.get()),
            () -> new ItemStack(NomenDubiumItems.SAP_OF_LIFE.get()),
            () -> new ItemStack(NomenDubiumItems.HUNTERS_ARROW.get()),
            () -> new ItemStack(NomenDubiumItems.PRIMITIVE_ARROW.get()),
            () -> new ItemStack(NomenDubiumItems.PREHISTORIC_MAW.get()),
            () -> new ItemStack(NomenDubiumItems.FOSSILISED_MAW.get()),
            () -> new ItemStack(NomenDubiumItems.REGENERATING_CHOP.get()),
            () -> NomenDubiumItems.FOSSIL.get().createStack("hulking_body"),
            () -> NomenDubiumItems.FOSSIL.get().createStack("shelled_body"),
            () -> NomenDubiumItems.FOSSIL.get().createStack("avian_body"),
            () -> NomenDubiumItems.FOSSIL.get().createStack("lanky_body"),
            () -> NomenDubiumItems.FOSSIL.get().createStack("puffy_body"),
            () -> NomenDubiumItems.FOSSIL.get().createStack("crunching_head"),
            () -> NomenDubiumItems.FOSSIL.get().createStack("shielded_head"),
            () -> NomenDubiumItems.FOSSIL.get().createStack("snarled_head"),
            () -> NomenDubiumItems.FOSSIL.get().createStack("beaked_head"),
            () -> NomenDubiumItems.FOSSIL.get().createStack("snorting_head"),
            () -> NomenDubiumItems.FOSSIL.get().createStack("spiked_tail"),
            () -> NomenDubiumItems.FOSSIL.get().createStack("stubby_tail"),
            () -> NomenDubiumItems.FOSSIL.get().createStack("clubbed_tail"),
            () -> NomenDubiumItems.FOSSIL.get().createStack("fan_tail"),
            () -> NomenDubiumItems.FOSSIL.get().createStack("speared_tail"),
            () -> NomenDubiumItems.FOSSIL.get().createStack("boney_plates_back"),
            () -> NomenDubiumItems.FOSSIL.get().createStack("dorsal_scales_back"),
            () -> NomenDubiumItems.FOSSIL.get().createStack("spikes_back"),
            () -> NomenDubiumItems.FOSSIL.get().createStack("spine_sail_back"),
            () -> NomenDubiumItems.FOSSIL.get().createStack("thorns_back")
        )

    );

}
