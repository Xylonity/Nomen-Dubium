package dev.xylonity.nomendubium.registry;

import dev.xylonity.knightlib.api.registrar.ResourceDispatcher;
import dev.xylonity.knightlib.api.registrar.ResourceEntry;
import dev.xylonity.knightlib.api.registrar.ResourceRegistry;
import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.common.blockentity.PaleontologyTableBlockEntity;
import dev.xylonity.nomendubium.common.blockentity.CoaldenSignBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class NomenDubiumBlockEntities {

    public static final ResourceRegistry<BlockEntityType<?>> BLOCK_ENTITIES = ResourceDispatcher.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, NomenDubium.MOD_ID);

    public static final ResourceEntry<BlockEntityType<PaleontologyTableBlockEntity>> PALEONTOLOGY_TABLE = BLOCK_ENTITIES.registerBlockEntity(
        "paleontology_table",
        PaleontologyTableBlockEntity::new,
        () -> NomenDubiumBlocks.PALEONTOLOGY_TABLE.get()
    );

    public static final ResourceEntry<BlockEntityType<CoaldenSignBlockEntity>> COALDEN_SIGN = BLOCK_ENTITIES.registerBlockEntity(
        "coalden_sign",
        CoaldenSignBlockEntity::new,
        () -> NomenDubiumBlocks.COALDEN_SIGN.get(),
        () -> NomenDubiumBlocks.COALDEN_WALL_SIGN.get()
    );

}
