package dev.xylonity.nomendubium.registry;

import dev.xylonity.knightlib.api.registrar.ResourceDispatcher;
import dev.xylonity.knightlib.api.registrar.ResourceEntry;
import dev.xylonity.knightlib.api.registrar.ResourceRegistry;
import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.common.worldgen.LifeHollowFeature;
import dev.xylonity.nomendubium.common.worldgen.OpenPitFeature;
import dev.xylonity.nomendubium.common.worldgen.structure.ExcavationStructure;
import dev.xylonity.nomendubium.common.worldgen.structure.ExcavationStructurePiece;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.core.registries.BuiltInRegistries;

public final class NomenDubiumWorldgen {

    public static final ResourceRegistry<Feature<?>> FEATURES = ResourceDispatcher.create(BuiltInRegistries.FEATURE, NomenDubium.MOD_ID);
    public static final ResourceRegistry<StructurePieceType> STRUCTURE_PIECES = ResourceDispatcher.create(BuiltInRegistries.STRUCTURE_PIECE, NomenDubium.MOD_ID);
    public static final ResourceRegistry<StructureType<?>> STRUCTURE_TYPES = ResourceDispatcher.create(BuiltInRegistries.STRUCTURE_TYPE, NomenDubium.MOD_ID);

    public static final ResourceEntry<Feature<NoneFeatureConfiguration>> OPEN_PIT = FEATURES.register("open_pit", () -> new OpenPitFeature(NoneFeatureConfiguration.CODEC));
    public static final ResourceEntry<Feature<NoneFeatureConfiguration>> LIFE_HOLLOW = FEATURES.register("life_hollow", () -> new LifeHollowFeature(NoneFeatureConfiguration.CODEC));

    public static final ResourceEntry<StructurePieceType> EXCAVATION_PIECE = STRUCTURE_PIECES.register("excavation", () -> (ignoredContext, tag) -> new ExcavationStructurePiece(tag));
    public static final ResourceEntry<StructureType<ExcavationStructure>> OPEN_PIT_STRUCTURE_TYPE = STRUCTURE_TYPES.register("open_pit", () -> () -> ExcavationStructure.OPEN_PIT_CODEC);

    public static final ResourceEntry<StructureType<ExcavationStructure>> LIFE_HOLLOW_STRUCTURE_TYPE = STRUCTURE_TYPES.register("life_hollow", () -> () -> ExcavationStructure.LIFE_HOLLOW_CODEC);

}