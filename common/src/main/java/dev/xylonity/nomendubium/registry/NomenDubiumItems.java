package dev.xylonity.nomendubium.registry;

import dev.xylonity.knightlib.api.registrar.ResourceDispatcher;
import dev.xylonity.knightlib.api.registrar.ResourceEntry;
import dev.xylonity.knightlib.api.registrar.ResourceRegistry;
import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.common.item.*;
import dev.xylonity.nomendubium.common.item.fossil.EncasedFossilItem;
import dev.xylonity.nomendubium.common.item.fossil.FossilItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.Block;

public final class NomenDubiumItems {

    public static final ResourceRegistry<Item> ITEMS = ResourceDispatcher.create(BuiltInRegistries.ITEM, NomenDubium.MOD_ID);

    public static final ResourceEntry<BlockItem> PALEONTOLOGY_TABLE = ITEMS.register("paleontology_table", () -> createBlockItem(NomenDubiumBlocks.PALEONTOLOGY_TABLE.get()));
    public static final ResourceEntry<BlockItem> ROOT_OF_LIFE = ITEMS.register("root_of_life", () -> createBlockItem(NomenDubiumBlocks.ROOT_OF_LIFE.get()));
    public static final ResourceEntry<BlockItem> FOSSIL_BLOCK = ITEMS.register("fossil_block", () -> createBlockItem(NomenDubiumBlocks.FOSSIL_BLOCK.get()));
    public static final ResourceEntry<BlockItem> SEDIMENT = ITEMS.register("sediment", () -> createBlockItem(NomenDubiumBlocks.SEDIMENT.get()));
    public static final ResourceEntry<BlockItem> COALDEN_LOG = ITEMS.register("coalden_log", () -> createBlockItem(NomenDubiumBlocks.COALDEN_LOG.get()));
    public static final ResourceEntry<BlockItem> COALDEN_WOOD = ITEMS.register("coalden_wood", () -> createBlockItem(NomenDubiumBlocks.COALDEN_WOOD.get()));
    public static final ResourceEntry<BlockItem> COALDEN_STRIPPED_LOG = ITEMS.register("coalden_stripped_log", () -> createBlockItem(NomenDubiumBlocks.COALDEN_STRIPPED_LOG.get()));
    public static final ResourceEntry<BlockItem> COALDEN_STRIPPED_WOOD = ITEMS.register("coalden_stripped_wood", () -> createBlockItem(NomenDubiumBlocks.COALDEN_STRIPPED_WOOD.get()));
    public static final ResourceEntry<BlockItem> COALDEN_PLANKS = ITEMS.register("coalden_planks", () -> createBlockItem(NomenDubiumBlocks.COALDEN_PLANKS.get()));
    public static final ResourceEntry<BlockItem> COALDEN_STAIRS = ITEMS.register("coalden_stairs", () -> createBlockItem(NomenDubiumBlocks.COALDEN_STAIRS.get()));
    public static final ResourceEntry<BlockItem> COALDEN_SLAB = ITEMS.register("coalden_slab", () -> createBlockItem(NomenDubiumBlocks.COALDEN_SLAB.get()));
    public static final ResourceEntry<BlockItem> COALDEN_FENCE = ITEMS.register("coalden_fence", () -> createBlockItem(NomenDubiumBlocks.COALDEN_FENCE.get()));
    public static final ResourceEntry<BlockItem> COALDEN_FENCE_GATE = ITEMS.register("coalden_fence_gate", () -> createBlockItem(NomenDubiumBlocks.COALDEN_FENCE_GATE.get()));
    public static final ResourceEntry<BlockItem> COALDEN_DOOR = ITEMS.register("coalden_door", () -> createBlockItem(NomenDubiumBlocks.COALDEN_DOOR.get()));
    public static final ResourceEntry<BlockItem> COALDEN_TRAPDOOR = ITEMS.register("coalden_trapdoor", () -> createBlockItem(NomenDubiumBlocks.COALDEN_TRAPDOOR.get()));
    public static final ResourceEntry<BlockItem> COALDEN_PRESSURE_PLATE = ITEMS.register("coalden_pressure_plate", () -> createBlockItem(NomenDubiumBlocks.COALDEN_PRESSURE_PLATE.get()));
    public static final ResourceEntry<BlockItem> COALDEN_BUTTON = ITEMS.register("coalden_button", () -> createBlockItem(NomenDubiumBlocks.COALDEN_BUTTON.get()));
    public static final ResourceEntry<SignItem> COALDEN_SIGN = ITEMS.register("coalden_sign", NomenDubiumItems::createCoaldenSignItem);

    public static final ResourceEntry<Item> ENCASED_FOSSIL = ITEMS.register("encased_fossil", () -> new EncasedFossilItem(new Item.Properties()));
    public static final ResourceEntry<FossilItem> FOSSIL = ITEMS.register("fossil", () -> new FossilItem(new Item.Properties().stacksTo(1)));
    public static final ResourceEntry<Item> AMBER = ITEMS.register("amber", () -> new AmberItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.1F).alwaysEat().build())));
    public static final ResourceEntry<Item> FOSSIL_BONE = ITEMS.register("fossil_bone", () -> new FossilBone(new Item.Properties()));
    public static final ResourceEntry<Item> SAP_OF_LIFE = ITEMS.register("sap_of_life", () -> new SapOfLifeItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.1F).alwaysEat().build())));
    public static final ResourceEntry<FruitOfLifeItem> FRUIT_OF_LIFE = ITEMS.register("fruit_of_life", () -> new FruitOfLifeItem(new Item.Properties().stacksTo(16)));
    public static final ResourceEntry<FossilisedAppleItem> FOSSILISED_APPLE = ITEMS.register("fossilised_apple", () -> new FossilisedAppleItem(new Item.Properties()));
    public static final ResourceEntry<FossilisedShellItem> FOSSILISED_SHELL = ITEMS.register("fossilised_shell", () -> new FossilisedShellItem(new Item.Properties()));
    public static final ResourceEntry<ShatteredDiamondItem> SHATTERED_DIAMOND = ITEMS.register("shattered_diamond", () -> new ShatteredDiamondItem(new Item.Properties()));
    public static final ResourceEntry<HuntersArrowItem> HUNTERS_ARROW = ITEMS.register("hunters_arrow", () -> new HuntersArrowItem(new Item.Properties()));
    public static final ResourceEntry<PrimitiveArrowItem> PRIMITIVE_ARROW = ITEMS.register("primitive_arrow", () -> new PrimitiveArrowItem(new Item.Properties()));
    public static final ResourceEntry<PrehistoricMawItem> PREHISTORIC_MAW = ITEMS.register("prehistoric_maw", () -> new PrehistoricMawItem(new Item.Properties().stacksTo(1)));
    public static final ResourceEntry<FossilisedMawItem> FOSSILISED_MAW = ITEMS.register("fossilised_maw", () -> new FossilisedMawItem(new Item.Properties().stacksTo(1)));
    public static final ResourceEntry<RegeneratingChopItem> REGENERATING_CHOP = ITEMS.register("regenerating_chop", () -> new RegeneratingChopItem(new Item.Properties().durability(100).food(new FoodProperties.Builder().nutrition(6).saturationMod(0.8F).build())));

    private static BlockItem createBlockItem(Block block) {
        return new BlockItem(block, new Item.Properties());
    }

    private static SignItem createCoaldenSignItem() {
        return new SignItem(new Item.Properties().stacksTo(16), NomenDubiumBlocks.COALDEN_SIGN.get(), NomenDubiumBlocks.COALDEN_WALL_SIGN.get());
    }

}