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

    public static final Supplier<CreativeModeTab> MAIN_CREATIVE_TAB = NomenDubium.PLATFORM.registerCreativeTab(
        "nomendubiumtab",
        Component.translatable("creativetab.nomendubium.title"),
        () -> new ItemStack(NomenDubiumItems.ENCASED_FOSSIL.get()),
        List.of(
            () -> new ItemStack(NomenDubiumItems.PALEONTOLOGY_TABLE.get()),
            () -> new ItemStack(NomenDubiumItems.ROOT_OF_LIFE.get()),
            () -> new ItemStack(NomenDubiumItems.FOSSIL_BLOCK.get()),
            () -> new ItemStack(NomenDubiumItems.SEDIMENT.get()),
            () -> new ItemStack(NomenDubiumItems.COALDEN_LOG.get()),
            () -> new ItemStack(NomenDubiumItems.COALDEN_WOOD.get()),
            () -> new ItemStack(NomenDubiumItems.COALDEN_STRIPPED_LOG.get()),
            () -> new ItemStack(NomenDubiumItems.COALDEN_STRIPPED_WOOD.get()),
            () -> new ItemStack(NomenDubiumItems.COALDEN_PLANKS.get()),
            () -> new ItemStack(NomenDubiumItems.COALDEN_STAIRS.get()),
            () -> new ItemStack(NomenDubiumItems.COALDEN_SLAB.get()),
            () -> new ItemStack(NomenDubiumItems.COALDEN_FENCE.get()),
            () -> new ItemStack(NomenDubiumItems.COALDEN_FENCE_GATE.get()),
            () -> new ItemStack(NomenDubiumItems.COALDEN_DOOR.get()),
            () -> new ItemStack(NomenDubiumItems.COALDEN_TRAPDOOR.get()),
            () -> new ItemStack(NomenDubiumItems.COALDEN_PRESSURE_PLATE.get()),
            () -> new ItemStack(NomenDubiumItems.COALDEN_BUTTON.get()),
            () -> new ItemStack(NomenDubiumItems.COALDEN_SIGN.get()),
            () -> new ItemStack(NomenDubiumItems.ENCASED_FOSSIL.get()),
            () -> new ItemStack(NomenDubiumItems.AMBER.get()),
            () -> new ItemStack(NomenDubiumItems.FOSSIL_BONE.get()),
            () -> new ItemStack(NomenDubiumItems.SAP_OF_LIFE.get()),
            () -> new ItemStack(NomenDubiumItems.FOSSILISED_APPLE.get()),
            () -> new ItemStack(NomenDubiumItems.FOSSILISED_SHELL.get()),
            () -> new ItemStack(NomenDubiumItems.SHATTERED_DIAMOND.get()),
            () -> new ItemStack(NomenDubiumItems.HUNTERS_ARROW.get()),
            () -> new ItemStack(NomenDubiumItems.PRIMITIVE_ARROW.get()),
            () -> new ItemStack(NomenDubiumItems.PREHISTORIC_MAW.get()),
            () -> new ItemStack(NomenDubiumItems.FOSSILISED_MAW.get()),
            () -> new ItemStack(NomenDubiumItems.REGENERATING_CHOP.get())
        )

    );

    public static final Supplier<CreativeModeTab> CHIMERA_CREATIVE_TAB = NomenDubium.PLATFORM.registerCreativeTab(
            "nomendubiumchimeratab",
            Component.translatable("creativetab.nomendubium.chimera.title"),
            () -> NomenDubiumItems.FOSSIL.get().createStack("shielded_head"),
            List.of(
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
