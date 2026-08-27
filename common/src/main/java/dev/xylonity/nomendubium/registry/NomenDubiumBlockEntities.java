package dev.xylonity.nomendubium.registry;

import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.common.blockentity.PaleontologyTableBlockEntity;
import dev.xylonity.nomendubium.common.blockentity.CoaldenSignBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.List;
import java.util.function.Supplier;

public final class NomenDubiumBlockEntities {

    public static void init() {
        ;;
    }

    public static final Supplier<BlockEntityType<PaleontologyTableBlockEntity>> PALEONTOLOGY_TABLE = NomenDubium.PLATFORM.registerBlockEntity(
        "paleontology_table",
        PaleontologyTableBlockEntity::new,
        List.of(NomenDubiumBlocks.PALEONTOLOGY_TABLE)
    );

    public static final Supplier<BlockEntityType<CoaldenSignBlockEntity>> COALDEN_SIGN = NomenDubium.PLATFORM.registerBlockEntity(
        "coalden_sign",
        CoaldenSignBlockEntity::new,
        List.of(NomenDubiumBlocks.COALDEN_SIGN, NomenDubiumBlocks.COALDEN_WALL_SIGN)
    );

}
