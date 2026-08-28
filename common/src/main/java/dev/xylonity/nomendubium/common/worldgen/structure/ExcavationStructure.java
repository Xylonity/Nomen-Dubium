package dev.xylonity.nomendubium.common.worldgen.structure;

import com.mojang.serialization.MapCodec;
import dev.xylonity.nomendubium.registry.NomenDubiumWorldgen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public final class ExcavationStructure extends Structure {

    public static final MapCodec<ExcavationStructure> OPEN_PIT_CODEC = simpleCodec(settings -> new ExcavationStructure(settings, Variant.OPEN_PIT));
    public static final MapCodec<ExcavationStructure> LIFE_HOLLOW_CODEC = simpleCodec(settings -> new ExcavationStructure(settings, Variant.LIFE_HOLLOW));

    private final Variant variant;

    private ExcavationStructure(StructureSettings settings, Variant variant) {
        super(settings);
        this.variant = variant;
    }

    @Override
    protected @NonNull Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        final int x = context.chunkPos().getMiddleBlockX();
        final int z = context.chunkPos().getMiddleBlockZ();
        final int y = context.chunkGenerator().getBaseHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
        final BlockPos origin = new BlockPos(x, y, z);
        return Optional.of(new GenerationStub(origin, builder -> builder.addPiece(new ExcavationStructurePiece(origin, this.variant))));
    }

    @Override
    public StructureType<?> type() {
        return switch (this.variant) {
            case OPEN_PIT -> NomenDubiumWorldgen.OPEN_PIT_STRUCTURE_TYPE.get();
            case LIFE_HOLLOW -> NomenDubiumWorldgen.LIFE_HOLLOW_STRUCTURE_TYPE.get();
        };

    }

    public enum Variant {
        OPEN_PIT,
        LIFE_HOLLOW;

        static Variant byName(String name) {
            try {
                return valueOf(name);
            }
            catch (Exception _) {
                return OPEN_PIT;
            }

        }

    }

}
