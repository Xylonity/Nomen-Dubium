package dev.xylonity.nomendubium.registry;

import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.common.worldgen.LifeHollowFeature;
import dev.xylonity.nomendubium.common.worldgen.OpenPitFeature;
import dev.xylonity.nomendubium.common.worldgen.structure.ExcavationStructure;
import dev.xylonity.nomendubium.common.worldgen.structure.ExcavationStructurePiece;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

import java.util.function.Supplier;

public final class NomenDubiumWorldgen {

    public static void init() {
        ;;
    }

    public static final Supplier<Feature<NoneFeatureConfiguration>> OPEN_PIT = NomenDubium.PLATFORM.registerFeature("open_pit", () -> new OpenPitFeature(NoneFeatureConfiguration.CODEC));
    public static final Supplier<Feature<NoneFeatureConfiguration>> LIFE_HOLLOW = NomenDubium.PLATFORM.registerFeature("life_hollow", () -> new LifeHollowFeature(NoneFeatureConfiguration.CODEC));

    public static final Supplier<StructurePieceType> EXCAVATION_PIECE = NomenDubium.PLATFORM.registerStructurePiece("excavation", (_, tag) -> new ExcavationStructurePiece(tag));
    public static final Supplier<StructureType<ExcavationStructure>> OPEN_PIT_STRUCTURE_TYPE = NomenDubium.PLATFORM.registerStructureType("open_pit", () -> () -> ExcavationStructure.OPEN_PIT_CODEC);

    public static final Supplier<StructureType<ExcavationStructure>> LIFE_HOLLOW_STRUCTURE_TYPE = NomenDubium.PLATFORM.registerStructureType("life_hollow", () -> () -> ExcavationStructure.LIFE_HOLLOW_CODEC);

}
