package dev.xylonity.nomendubium.registry;

import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.common.block.CoaldenBlock;
import dev.xylonity.nomendubium.common.block.CoaldenButtonBlock;
import dev.xylonity.nomendubium.common.block.CoaldenDoorBlock;
import dev.xylonity.nomendubium.common.block.CoaldenFenceBlock;
import dev.xylonity.nomendubium.common.block.CoaldenFenceGateBlock;
import dev.xylonity.nomendubium.common.block.CoaldenPillarBlock;
import dev.xylonity.nomendubium.common.block.CoaldenPressurePlateBlock;
import dev.xylonity.nomendubium.common.block.CoaldenSlabBlock;
import dev.xylonity.nomendubium.common.block.CoaldenStairBlock;
import dev.xylonity.nomendubium.common.block.CoaldenStandingSignBlock;
import dev.xylonity.nomendubium.common.block.CoaldenTrapDoorBlock;
import dev.xylonity.nomendubium.common.block.CoaldenWallSignBlock;
import dev.xylonity.nomendubium.common.block.FossilBlock;
import dev.xylonity.nomendubium.common.block.PaleontologyTableBlock;
import dev.xylonity.nomendubium.common.block.RootOfLifeBlock;
import dev.xylonity.nomendubium.common.block.SedimentBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;

import java.util.Optional;
import java.util.function.Supplier;

public final class NomenDubiumBlocks {

    public static void init() {
        ;;
    }

    public static final Supplier<PaleontologyTableBlock> PALEONTOLOGY_TABLE = NomenDubium.PLATFORM.registerBlock("paleontology_table",
        key -> new PaleontologyTableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRAFTING_TABLE).setId(key))
    );
    public static final Supplier<RootOfLifeBlock> ROOT_OF_LIFE = NomenDubium.PLATFORM.registerBlock("root_of_life",
        key -> new RootOfLifeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DANDELION).setId(key))
    );
    public static final Supplier<FossilBlock> FOSSIL_BLOCK = NomenDubium.PLATFORM.registerBlock("fossil_block",
        key -> new FossilBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BONE_BLOCK).setId(key))
    );
    public static final Supplier<SedimentBlock> SEDIMENT = NomenDubium.PLATFORM.registerBlock("sediment",
        key -> new SedimentBlock(
            BlockBehaviour.Properties.ofFullCopy(Blocks.SNOW)
                .strength(1.5F, 6.0F)
                .sound(SoundType.GRAVEL)
                .speedFactor(0.55F)
                .requiresCorrectToolForDrops()
                .setId(key)
        )
    );

    public static final Supplier<CoaldenPillarBlock> COALDEN_LOG = NomenDubium.PLATFORM.registerBlock("coalden_log",
        key -> new CoaldenPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG).setId(key)));
    public static final Supplier<CoaldenPillarBlock> COALDEN_WOOD = NomenDubium.PLATFORM.registerBlock("coalden_wood",
        key -> new CoaldenPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD).setId(key)));
    public static final Supplier<CoaldenPillarBlock> COALDEN_STRIPPED_LOG = NomenDubium.PLATFORM.registerBlock("coalden_stripped_log",
        key -> new CoaldenPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG).setId(key)));
    public static final Supplier<CoaldenPillarBlock> COALDEN_STRIPPED_WOOD = NomenDubium.PLATFORM.registerBlock("coalden_stripped_wood",
        key -> new CoaldenPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD).setId(key)));
    public static final Supplier<CoaldenBlock> COALDEN_PLANKS = NomenDubium.PLATFORM.registerBlock("coalden_planks",
        key -> new CoaldenBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).setId(key)));
    public static final Supplier<CoaldenStairBlock> COALDEN_STAIRS = NomenDubium.PLATFORM.registerBlock("coalden_stairs",
        key -> new CoaldenStairBlock(COALDEN_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS).setId(key)));
    public static final Supplier<CoaldenSlabBlock> COALDEN_SLAB = NomenDubium.PLATFORM.registerBlock("coalden_slab",
        key -> new CoaldenSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB).setId(key)));
    public static final Supplier<CoaldenFenceBlock> COALDEN_FENCE = NomenDubium.PLATFORM.registerBlock("coalden_fence",
        key -> new CoaldenFenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE).setId(key)));
    public static final Supplier<CoaldenFenceGateBlock> COALDEN_FENCE_GATE = NomenDubium.PLATFORM.registerBlock("coalden_fence_gate",
        key -> new CoaldenFenceGateBlock(NomenDubiumWoodTypes.COALDEN, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE).setId(key)));
    public static final Supplier<CoaldenDoorBlock> COALDEN_DOOR = NomenDubium.PLATFORM.registerBlock("coalden_door",
        key -> new CoaldenDoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR).setId(key)));
    public static final Supplier<CoaldenTrapDoorBlock> COALDEN_TRAPDOOR = NomenDubium.PLATFORM.registerBlock("coalden_trapdoor",
        key -> new CoaldenTrapDoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR).setId(key)));
    public static final Supplier<CoaldenPressurePlateBlock> COALDEN_PRESSURE_PLATE = NomenDubium.PLATFORM.registerBlock("coalden_pressure_plate",
        key -> new CoaldenPressurePlateBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE).setId(key)));
    public static final Supplier<CoaldenButtonBlock> COALDEN_BUTTON = NomenDubium.PLATFORM.registerBlock("coalden_button",
        key -> new CoaldenButtonBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON).setId(key)));
    public static final Supplier<CoaldenStandingSignBlock> COALDEN_SIGN = NomenDubium.PLATFORM.registerBlock("coalden_sign",
        key -> new CoaldenStandingSignBlock(NomenDubiumWoodTypes.COALDEN, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SIGN).setId(key)));
    public static final Supplier<CoaldenWallSignBlock> COALDEN_WALL_SIGN = NomenDubium.PLATFORM.registerBlock("coalden_wall_sign",
        key -> new CoaldenWallSignBlock(
            NomenDubiumWoodTypes.COALDEN,
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_SIGN)
                .overrideLootTable(Optional.of(ResourceKey.create(Registries.LOOT_TABLE, NomenDubium.of("blocks/coalden_sign"))))
                .setId(key)
        ));

}
