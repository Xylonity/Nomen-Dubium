package dev.xylonity.nomendubium.registry;

import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.common.blockentity.PaleontologyTableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public final class NomenDubiumBlockEntities {

    public static void init() {
        ;;
    }

    public static final Supplier<BlockEntityType<PaleontologyTableBlockEntity>> PALEONTOLOGY_TABLE = NomenDubium.PLATFORM.registerBlockEntity("paleontology_table", PaleontologyTableBlockEntity::new, NomenDubiumBlocks.PALEONTOLOGY_TABLE);

}