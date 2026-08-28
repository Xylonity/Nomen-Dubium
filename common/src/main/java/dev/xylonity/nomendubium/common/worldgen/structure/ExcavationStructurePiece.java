package dev.xylonity.nomendubium.common.worldgen.structure;

import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.registry.NomenDubiumWorldgen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import org.jspecify.annotations.NonNull;

public final class ExcavationStructurePiece extends StructurePiece {

    private static final int[][] OPEN_PIT_OFFSETS = {
        {0, 0}, {4, 0}, {-4, 0}, {0, 4}, {0, -4}, {4, 4}, {-4, 4}, {4, -4}, {-4, -4}
    };
    private static final int[][] LIFE_HOLLOW_OFFSETS = {
        {0, 0},
        {4, 0}, {-4, 0}, {0, 4}, {0, -4},
        {4, 4}, {-4, 4}, {4, -4}, {-4, -4},
        {8, 0}, {-8, 0}, {0, 8}, {0, -8},
        {8, 4}, {8, -4}, {-8, 4}, {-8, -4},
        {4, 8}, {-4, 8}, {4, -8}, {-4, -8},
        {8, 8}, {-8, 8}, {8, -8}, {-8, -8}
    };

    private final ExcavationStructure.Variant variant;

    public ExcavationStructurePiece(BlockPos origin, ExcavationStructure.Variant variant) {
        super(NomenDubiumWorldgen.EXCAVATION_PIECE.get(), 0, new BoundingBox(origin));
        this.variant = variant;
    }

    public ExcavationStructurePiece(CompoundTag tag) {
        super(NomenDubiumWorldgen.EXCAVATION_PIECE.get(), tag);
        this.variant = ExcavationStructure.Variant.byName(tag.getStringOr("variant", "OPEN_PIT"));
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putString("variant", this.variant.name());
    }

    @Override
    public void postProcess(@NonNull WorldGenLevel level, @NonNull StructureManager structureManager, @NonNull ChunkGenerator chunkGenerator, @NonNull RandomSource random, @NonNull BoundingBox chunkBounds, @NonNull ChunkPos chunkPos, @NonNull BlockPos pivot) {
        final BlockPos center = this.boundingBox.getCenter();
        final int[][] offsets = this.variant == ExcavationStructure.Variant.LIFE_HOLLOW ? LIFE_HOLLOW_OFFSETS : OPEN_PIT_OFFSETS;
        for (int[] offset : offsets) {
            final BlockPos tryy = center.offset(offset[0], 0, offset[1]);
            final boolean placed = switch (this.variant) {
                case OPEN_PIT -> NomenDubiumWorldgen.OPEN_PIT.get().place(NoneFeatureConfiguration.INSTANCE, level, chunkGenerator, random, tryy);
                case LIFE_HOLLOW -> NomenDubiumWorldgen.LIFE_HOLLOW.get().place(NoneFeatureConfiguration.INSTANCE, level, chunkGenerator, random, tryy);
            };

            if (placed) {
                return;
            }

        }

        NomenDubium.LOGGER.warn("Unable to generate {} excavation near structure start {}", this.variant, center);
    }

}