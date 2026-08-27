package dev.xylonity.nomendubium.registry;

import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.common.item.*;
import dev.xylonity.nomendubium.common.item.fossil.EncasedFossilIem;
import dev.xylonity.nomendubium.common.item.fossil.FossilItem;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public final class NomenDubiumItems {

    public static void init() {
        ;;
    }

    public static final Supplier<BlockItem> PALEONTOLOGY_TABLE = NomenDubium.PLATFORM.registerItem("paleontology_table", key -> createBlockItem(key, NomenDubiumBlocks.PALEONTOLOGY_TABLE.get()));
    public static final Supplier<BlockItem> ROOT_OF_LIFE = NomenDubium.PLATFORM.registerItem("root_of_life", key -> createBlockItem(key, NomenDubiumBlocks.ROOT_OF_LIFE.get()));
    public static final Supplier<BlockItem> FOSSIL_BLOCK = NomenDubium.PLATFORM.registerItem("fossil_block", key -> createBlockItem(key, NomenDubiumBlocks.FOSSIL_BLOCK.get()));
    public static final Supplier<BlockItem> SEDIMENT = NomenDubium.PLATFORM.registerItem("sediment", key -> createBlockItem(key, NomenDubiumBlocks.SEDIMENT.get()));
    public static final Supplier<BlockItem> COALDEN_LOG = NomenDubium.PLATFORM.registerItem("coalden_log", key -> createBlockItem(key, NomenDubiumBlocks.COALDEN_LOG.get()));
    public static final Supplier<BlockItem> COALDEN_WOOD = NomenDubium.PLATFORM.registerItem("coalden_wood", key -> createBlockItem(key, NomenDubiumBlocks.COALDEN_WOOD.get()));
    public static final Supplier<BlockItem> COALDEN_STRIPPED_LOG = NomenDubium.PLATFORM.registerItem("coalden_stripped_log", key -> createBlockItem(key, NomenDubiumBlocks.COALDEN_STRIPPED_LOG.get()));
    public static final Supplier<BlockItem> COALDEN_STRIPPED_WOOD = NomenDubium.PLATFORM.registerItem("coalden_stripped_wood", key -> createBlockItem(key, NomenDubiumBlocks.COALDEN_STRIPPED_WOOD.get()));
    public static final Supplier<BlockItem> COALDEN_PLANKS = NomenDubium.PLATFORM.registerItem("coalden_planks", key -> createBlockItem(key, NomenDubiumBlocks.COALDEN_PLANKS.get()));
    public static final Supplier<BlockItem> COALDEN_STAIRS = NomenDubium.PLATFORM.registerItem("coalden_stairs", key -> createBlockItem(key, NomenDubiumBlocks.COALDEN_STAIRS.get()));
    public static final Supplier<BlockItem> COALDEN_SLAB = NomenDubium.PLATFORM.registerItem("coalden_slab", key -> createBlockItem(key, NomenDubiumBlocks.COALDEN_SLAB.get()));
    public static final Supplier<BlockItem> COALDEN_FENCE = NomenDubium.PLATFORM.registerItem("coalden_fence", key -> createBlockItem(key, NomenDubiumBlocks.COALDEN_FENCE.get()));
    public static final Supplier<BlockItem> COALDEN_FENCE_GATE = NomenDubium.PLATFORM.registerItem("coalden_fence_gate", key -> createBlockItem(key, NomenDubiumBlocks.COALDEN_FENCE_GATE.get()));
    public static final Supplier<BlockItem> COALDEN_DOOR = NomenDubium.PLATFORM.registerItem("coalden_door", key -> createBlockItem(key, NomenDubiumBlocks.COALDEN_DOOR.get()));
    public static final Supplier<BlockItem> COALDEN_TRAPDOOR = NomenDubium.PLATFORM.registerItem("coalden_trapdoor", key -> createBlockItem(key, NomenDubiumBlocks.COALDEN_TRAPDOOR.get()));
    public static final Supplier<BlockItem> COALDEN_PRESSURE_PLATE = NomenDubium.PLATFORM.registerItem("coalden_pressure_plate", key -> createBlockItem(key, NomenDubiumBlocks.COALDEN_PRESSURE_PLATE.get()));
    public static final Supplier<BlockItem> COALDEN_BUTTON = NomenDubium.PLATFORM.registerItem("coalden_button", key -> createBlockItem(key, NomenDubiumBlocks.COALDEN_BUTTON.get()));
    public static final Supplier<SignItem> COALDEN_SIGN = NomenDubium.PLATFORM.registerItem("coalden_sign", NomenDubiumItems::createCoaldenSignItem);
    public static final Supplier<Item> ENCASED_FOSSIL = NomenDubium.PLATFORM.registerItem("encased_fossil", key -> new EncasedFossilIem(new Item.Properties().setId(key)));
    public static final Supplier<FossilItem> FOSSIL = NomenDubium.PLATFORM.registerItem("fossil", key -> new FossilItem(new Item.Properties().stacksTo(1).setId(key)));
    public static final Supplier<Item> AMBER = NomenDubium.PLATFORM.registerItem("amber", key -> new AmberItem(new Item.Properties().setId(key)));
    public static final Supplier<Item> FOSSIL_BONE = NomenDubium.PLATFORM.registerItem("fossil_bone", key -> new FossilBone(new Item.Properties().setId(key)));
    public static final Supplier<Item> SAP_OF_LIFE = NomenDubium.PLATFORM.registerItem("sap_of_life", key -> new SapOfLifeItem(new Item.Properties().setId(key)));
    public static final Supplier<FossilisedAppleItem> FOSSILISED_APPLE = NomenDubium.PLATFORM.registerItem("fossilised_apple", key -> new FossilisedAppleItem(new Item.Properties().setId(key)));
    public static final Supplier<FossilisedShellItem> FOSSILISED_SHELL = NomenDubium.PLATFORM.registerItem("fossilised_shell", key -> new FossilisedShellItem(new Item.Properties().setId(key)));
    public static final Supplier<ShatteredDiamondItem> SHATTERED_DIAMOND = NomenDubium.PLATFORM.registerItem("shattered_diamond", key -> new ShatteredDiamondItem(new Item.Properties().setId(key)));
    public static final Supplier<HuntersArrowItem> HUNTERS_ARROW = NomenDubium.PLATFORM.registerItem("hunters_arrow", key -> new HuntersArrowItem(new Item.Properties().setId(key)));
    public static final Supplier<PrimitiveArrowItem> PRIMITIVE_ARROW = NomenDubium.PLATFORM.registerItem("primitive_arrow", key -> new PrimitiveArrowItem(new Item.Properties().setId(key)));
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

    private static SignItem createCoaldenSignItem(ResourceKey<Item> key) {
        final SignItem item = new SignItem(
            NomenDubiumBlocks.COALDEN_SIGN.get(),
            NomenDubiumBlocks.COALDEN_WALL_SIGN.get(),
            new Item.Properties().stacksTo(16).setId(key)
        );

        item.registerBlocks(Item.BY_BLOCK, item);

        return item;
    }

}
