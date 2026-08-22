package dev.xylonity.nomendubium.registry;

import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.common.item.HuntersArrowItem;
import dev.xylonity.nomendubium.common.item.FossilisedMawItem;
import dev.xylonity.nomendubium.common.item.PrehistoricMawItem;
import dev.xylonity.nomendubium.common.item.RegeneratingChopItem;
import dev.xylonity.nomendubium.common.item.fossil.EncasedFossilIem;
import dev.xylonity.nomendubium.common.item.fossil.FossilItem;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public final class NomenDubiumItems {

    public static void init() {
        ;;
    }

    public static final Supplier<BlockItem> PALEONTOLOGY_TABLE = NomenDubium.PLATFORM.registerItem("paleontology_table", key -> createBlockItem(key, NomenDubiumBlocks.PALEONTOLOGY_TABLE.get()));
    public static final Supplier<Item> ENCASED_FOSSIL = NomenDubium.PLATFORM.registerItem("encased_fossil", key -> new EncasedFossilIem(new Item.Properties().setId(key)));
    public static final Supplier<FossilItem> FOSSIL = NomenDubium.PLATFORM.registerItem("fossil", key -> new FossilItem(new Item.Properties().stacksTo(1).setId(key)));
    public static final Supplier<HuntersArrowItem> HUNTERS_ARROW = NomenDubium.PLATFORM.registerItem("hunters_arrow", key -> new HuntersArrowItem(new Item.Properties().setId(key)));
    public static final Supplier<PrehistoricMawItem> PREHISTORIC_MAW = NomenDubium.PLATFORM.registerItem("prehistoric_maw", key -> new PrehistoricMawItem(new Item.Properties().stacksTo(1).setId(key)));
    public static final Supplier<FossilisedMawItem> FOSSILISED_MAW = NomenDubium.PLATFORM.registerItem("fossilised_maw", key -> new FossilisedMawItem(new Item.Properties().stacksTo(1).setId(key)));
    public static final Supplier<RegeneratingChopItem> REGENERATING_CHOP = NomenDubium.PLATFORM.registerItem("regenerating_chop", key -> new RegeneratingChopItem(new Item.Properties().durability(100).food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.8F).build()).setId(key)));

    private static BlockItem createBlockItem(ResourceKey<Item> key, Block block) {
        final BlockItem item = new BlockItem(
            block,
            new Item.Properties().useBlockDescriptionPrefix().setId(key)
        );

        item.registerBlocks(Item.BY_BLOCK, item);

        return item;
    }

}
