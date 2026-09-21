package dev.xylonity.nomendubium.registry;

import dev.xylonity.knightlib.api.registrar.ResourceDispatcher;
import dev.xylonity.knightlib.api.registrar.ResourceEntry;
import dev.xylonity.knightlib.api.registrar.ResourceRegistry;
import dev.xylonity.nomendubium.NomenDubium;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public final class NomenDubiumCreativeTabs {

    public static final ResourceRegistry<CreativeModeTab> CREATIVE_TABS = ResourceDispatcher.create(BuiltInRegistries.CREATIVE_MODE_TAB, NomenDubium.MOD_ID);

    public static final ResourceEntry<CreativeModeTab> CREATIVE_TAB_1 = register(
        "nomendubiumtab1", CreativeModeTab.Row.TOP, 0,
        Component.translatable("creativetab.nomendubium.title"), () -> new ItemStack(NomenDubiumItems.ENCASED_FOSSIL.get()),
            () -> new ItemStack(NomenDubiumItems.PALEONTOLOGY_TABLE.get()),
            () -> new ItemStack(NomenDubiumItems.ROOT_OF_LIFE.get()),
            () -> new ItemStack(NomenDubiumItems.FOSSIL_BLOCK.get()),
            () -> new ItemStack(NomenDubiumItems.SEDIMENT.get()),
            () -> new ItemStack(NomenDubiumItems.ENCASED_FOSSIL.get()),
            () -> new ItemStack(NomenDubiumItems.AMBER.get()),
            () -> new ItemStack(NomenDubiumItems.FOSSIL_BONE.get()),
            () -> new ItemStack(NomenDubiumItems.FOSSILISED_APPLE.get()),
            () -> new ItemStack(NomenDubiumItems.FOSSILISED_SHELL.get()),
            () -> new ItemStack(NomenDubiumItems.SHATTERED_DIAMOND.get()),
            () -> new ItemStack(NomenDubiumItems.PRIMITIVE_ARROW.get()),
            () -> new ItemStack(NomenDubiumItems.FOSSILISED_MAW.get()),
            () -> new ItemStack(NomenDubiumItems.HUNTERS_ARROW.get()),
            () -> new ItemStack(NomenDubiumItems.PREHISTORIC_MAW.get()),
            () -> new ItemStack(NomenDubiumItems.REGENERATING_CHOP.get()),
            () -> new ItemStack(NomenDubiumItems.SAP_OF_LIFE.get()),
            () -> new ItemStack(NomenDubiumItems.FRUIT_OF_LIFE.get()),
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
            () -> new ItemStack(NomenDubiumItems.COALDEN_SIGN.get()));

    public static final ResourceEntry<CreativeModeTab> CREATIVE_TAB_2 = register(
            "nomendubiumtab2", CreativeModeTab.Row.TOP, 1,
            Component.translatable("creativetab.nomendubium.title2"), () -> NomenDubiumItems.FOSSIL.get().createStack("shielded_head"),
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
                () -> NomenDubiumItems.FOSSIL.get().createStack("thorns_back"));

    @SafeVarargs
    private static ResourceEntry<CreativeModeTab> register(String name, CreativeModeTab.Row row, int column, Component title, Supplier<ItemStack> icon, Supplier<ItemStack>... entries) {
        return CREATIVE_TABS.register(name, () -> CreativeModeTab.builder(row, column)
            .title(title)
            .icon(icon)
            .displayItems((parameters, output) -> {
                for (Supplier<ItemStack> entry : entries) {
                    output.accept(entry.get());
                }
            })
            .build());
    }

}