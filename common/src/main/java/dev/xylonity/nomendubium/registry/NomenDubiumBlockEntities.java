package dev.xylonity.nomendubium.registry;

import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.common.blockentity.GeologistTableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public final class NomenDubiumBlockEntities {

    public static void init() {
        ;;
    }

    public static final Supplier<BlockEntityType<GeologistTableBlockEntity>> GEOLOGIST_TABLE = NomenDubium.PLATFORM.registerBlockEntity("geologist_table", GeologistTableBlockEntity::new, NomenDubiumBlocks.GEOLOGIST_TABLE);

}