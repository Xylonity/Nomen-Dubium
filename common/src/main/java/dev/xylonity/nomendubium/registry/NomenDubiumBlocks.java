package dev.xylonity.nomendubium.registry;

import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.common.block.CoaldenBlocks;
import dev.xylonity.nomendubium.common.block.PaleontologyTableBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallSignBlock;
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

    public static final Supplier<RotatedPillarBlock> COALDEN_LOG = NomenDubium.PLATFORM.registerBlock("coalden_log",
        key -> CoaldenBlocks.pillar(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG).setId(key)));
    public static final Supplier<RotatedPillarBlock> COALDEN_WOOD = NomenDubium.PLATFORM.registerBlock("coalden_wood",
        key -> CoaldenBlocks.pillar(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD).setId(key)));
    public static final Supplier<RotatedPillarBlock> COALDEN_STRIPPED_LOG = NomenDubium.PLATFORM.registerBlock("coalden_stripped_log",
        key -> CoaldenBlocks.pillar(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG).setId(key)));
    public static final Supplier<RotatedPillarBlock> COALDEN_STRIPPED_WOOD = NomenDubium.PLATFORM.registerBlock("coalden_stripped_wood",
        key -> CoaldenBlocks.pillar(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD).setId(key)));
    public static final Supplier<Block> COALDEN_PLANKS = NomenDubium.PLATFORM.registerBlock("coalden_planks",
        key -> CoaldenBlocks.block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).setId(key)));
    public static final Supplier<StairBlock> COALDEN_STAIRS = NomenDubium.PLATFORM.registerBlock("coalden_stairs",
        key -> CoaldenBlocks.stairs(COALDEN_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS).setId(key)));
    public static final Supplier<SlabBlock> COALDEN_SLAB = NomenDubium.PLATFORM.registerBlock("coalden_slab",
        key -> CoaldenBlocks.slab(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB).setId(key)));
    public static final Supplier<FenceBlock> COALDEN_FENCE = NomenDubium.PLATFORM.registerBlock("coalden_fence",
        key -> CoaldenBlocks.fence(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE).setId(key)));
    public static final Supplier<FenceGateBlock> COALDEN_FENCE_GATE = NomenDubium.PLATFORM.registerBlock("coalden_fence_gate",
        key -> CoaldenBlocks.fenceGate(NomenDubiumWoodTypes.COALDEN, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE).setId(key)));
    public static final Supplier<DoorBlock> COALDEN_DOOR = NomenDubium.PLATFORM.registerBlock("coalden_door",
        key -> CoaldenBlocks.door(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR).setId(key)));
    public static final Supplier<TrapDoorBlock> COALDEN_TRAPDOOR = NomenDubium.PLATFORM.registerBlock("coalden_trapdoor",
        key -> CoaldenBlocks.trapdoor(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR).setId(key)));
    public static final Supplier<PressurePlateBlock> COALDEN_PRESSURE_PLATE = NomenDubium.PLATFORM.registerBlock("coalden_pressure_plate",
        key -> CoaldenBlocks.pressurePlate(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE).setId(key)));
    public static final Supplier<ButtonBlock> COALDEN_BUTTON = NomenDubium.PLATFORM.registerBlock("coalden_button",
        key -> CoaldenBlocks.button(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON).setId(key)));
    public static final Supplier<StandingSignBlock> COALDEN_SIGN = NomenDubium.PLATFORM.registerBlock("coalden_sign",
        key -> CoaldenBlocks.standingSign(NomenDubiumWoodTypes.COALDEN, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SIGN).setId(key)));
    public static final Supplier<WallSignBlock> COALDEN_WALL_SIGN = NomenDubium.PLATFORM.registerBlock("coalden_wall_sign",
        key -> CoaldenBlocks.wallSign(
            NomenDubiumWoodTypes.COALDEN,
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_SIGN)
                .overrideLootTable(Optional.of(ResourceKey.create(Registries.LOOT_TABLE, NomenDubium.of("blocks/coalden_sign"))))
                .setId(key)
        ));

}
