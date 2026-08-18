package dev.xylonity.nomendubium.registry;

import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.common.block.GeologistTableBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

public final class NomenDubiumBlocks {

    public static void init() {
        ;;
    }

    public static final Supplier<GeologistTableBlock> GEOLOGIST_TABLE = NomenDubium.PLATFORM.registerBlock("geologist_table",
        key -> new GeologistTableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRAFTING_TABLE).setId(key))
    );

}
