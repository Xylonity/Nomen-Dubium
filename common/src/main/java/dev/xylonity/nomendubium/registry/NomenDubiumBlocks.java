package dev.xylonity.nomendubium.registry;

import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.knightlib.api.registrar.ResourceDispatcher;
import dev.xylonity.knightlib.api.registrar.ResourceEntry;
import dev.xylonity.knightlib.api.registrar.ResourceRegistry;
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
import net.minecraft.core.registries.BuiltInRegistries;

public final class NomenDubiumBlocks {

    public static final ResourceRegistry<net.minecraft.world.level.block.Block> BLOCKS = ResourceDispatcher.create(BuiltInRegistries.BLOCK, NomenDubium.MOD_ID);

    public static final ResourceEntry<PaleontologyTableBlock> PALEONTOLOGY_TABLE = BLOCKS.register("paleontology_table",
        () -> new PaleontologyTableBlock(BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE))
    );
    public static final ResourceEntry<RootOfLifeBlock> ROOT_OF_LIFE = BLOCKS.register("root_of_life",
        () -> new RootOfLifeBlock(BlockBehaviour.Properties.copy(Blocks.DANDELION))
    );
    public static final ResourceEntry<FossilBlock> FOSSIL_BLOCK = BLOCKS.register("fossil_block",
        () -> new FossilBlock(BlockBehaviour.Properties.copy(Blocks.BONE_BLOCK))
    );
    public static final ResourceEntry<SedimentBlock> SEDIMENT = BLOCKS.register("sediment",
        () -> new SedimentBlock(
            BlockBehaviour.Properties.copy(Blocks.SNOW)
                .strength(1.5F, 6.0F)
                .sound(SoundType.GRAVEL)
                .speedFactor(0.55F)
                .requiresCorrectToolForDrops()
                .noLootTable()
        )

    );

    public static final ResourceEntry<CoaldenPillarBlock> COALDEN_LOG = BLOCKS.register("coalden_log",
        () -> new CoaldenPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final ResourceEntry<CoaldenPillarBlock> COALDEN_WOOD = BLOCKS.register("coalden_wood",
        () -> new CoaldenPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)));
    public static final ResourceEntry<CoaldenPillarBlock> COALDEN_STRIPPED_LOG = BLOCKS.register("coalden_stripped_log",
        () -> new CoaldenPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));
    public static final ResourceEntry<CoaldenPillarBlock> COALDEN_STRIPPED_WOOD = BLOCKS.register("coalden_stripped_wood",
        () -> new CoaldenPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));
    public static final ResourceEntry<CoaldenBlock> COALDEN_PLANKS = BLOCKS.register("coalden_planks",
        () -> new CoaldenBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final ResourceEntry<CoaldenStairBlock> COALDEN_STAIRS = BLOCKS.register("coalden_stairs",
        () -> new CoaldenStairBlock(COALDEN_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS)));
    public static final ResourceEntry<CoaldenSlabBlock> COALDEN_SLAB = BLOCKS.register("coalden_slab",
        () -> new CoaldenSlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)));
    public static final ResourceEntry<CoaldenFenceBlock> COALDEN_FENCE = BLOCKS.register("coalden_fence",
        () -> new CoaldenFenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)));
    public static final ResourceEntry<CoaldenFenceGateBlock> COALDEN_FENCE_GATE = BLOCKS.register("coalden_fence_gate",
        () -> new CoaldenFenceGateBlock(NomenDubiumWoodTypes.COALDEN, BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE)));
    public static final ResourceEntry<CoaldenDoorBlock> COALDEN_DOOR = BLOCKS.register("coalden_door",
        () -> new CoaldenDoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.copy(Blocks.OAK_DOOR)));
    public static final ResourceEntry<CoaldenTrapDoorBlock> COALDEN_TRAPDOOR = BLOCKS.register("coalden_trapdoor",
        () -> new CoaldenTrapDoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR)));
    public static final ResourceEntry<CoaldenPressurePlateBlock> COALDEN_PRESSURE_PLATE = BLOCKS.register("coalden_pressure_plate",
        () -> new CoaldenPressurePlateBlock(BlockSetType.OAK, BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE)));
    public static final ResourceEntry<CoaldenButtonBlock> COALDEN_BUTTON = BLOCKS.register("coalden_button",
        () -> new CoaldenButtonBlock(BlockSetType.OAK, BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON)));
    public static final ResourceEntry<CoaldenStandingSignBlock> COALDEN_SIGN = BLOCKS.register("coalden_sign",
        () -> new CoaldenStandingSignBlock(NomenDubiumWoodTypes.COALDEN, BlockBehaviour.Properties.copy(Blocks.OAK_SIGN)));
    public static final ResourceEntry<CoaldenWallSignBlock> COALDEN_WALL_SIGN = BLOCKS.register("coalden_wall_sign",
        () -> new CoaldenWallSignBlock(
            NomenDubiumWoodTypes.COALDEN,
            BlockBehaviour.Properties.copy(Blocks.OAK_WALL_SIGN).dropsLike(COALDEN_SIGN.get())
        ));

}
