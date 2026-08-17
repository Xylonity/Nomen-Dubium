package dev.xylonity.nomendubium.registry;

import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.common.item.fossil.EncasedFossilIem;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public final class NomenDubiumItems {

    public static void init() {
        ;;
    }

    public static final Supplier<BlockItem> GEOLOGIST_TABLE = NomenDubium.PLATFORM.registerItem("geologist_table", key -> createBlockItem(key, NomenDubiumBlocks.GEOLOGIST_TABLE.get()));
    public static final Supplier<Item> ENCASED_FOSSIL = NomenDubium.PLATFORM.registerItem("encased_fossil", key -> new EncasedFossilIem(new Item.Properties().setId(key)));
    public static final Supplier<Item> FOSSIL = NomenDubium.PLATFORM.registerItem("fossil", key -> new Item(new Item.Properties().stacksTo(1).setId(key)));

    private static BlockItem createBlockItem(ResourceKey<Item> key, Block block) {
        final BlockItem item = new BlockItem(
            block,
            new Item.Properties().useBlockDescriptionPrefix().setId(key)
        );

        item.registerBlocks(Item.BY_BLOCK, item);

        return item;
    }

}