package dev.xylonity.nomendubium.common.worldgen;

import com.mojang.serialization.Codec;
import dev.xylonity.nomendubium.common.entity.TreeOfLifeEntity;
import dev.xylonity.nomendubium.registry.NomenDubiumBlocks;
import dev.xylonity.nomendubium.registry.NomenDubiumEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.phys.AABB;

/// Derived from my own implementation
///
/// All methods do the same thing, 3D block search on a certain volume. TODO: refactor consistent logic into singular methods
///
/// https://github.com/Xylonity/Parallax/tree/v.1.20.1/common/src/main/java/dev/xylonity/parallax/common/worldgen/SereCraterFeature.java
public final class LifeHollowFeature extends Feature<NoneFeatureConfiguration> {

    public LifeHollowFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        final WorldGenLevel level = context.level();
        final RandomSource random = context.random();
        final int centerX = context.origin().getX();
        final int centerZ = context.origin().getZ();
        final int surfaceY = surfaceY(level, centerX, centerZ);
        final BlockState surface = level.getBlockState(new BlockPos(centerX, surfaceY, centerZ));

        // Structure only generated on plane and grassy surfaces
        if (!isGrassySurface(surface)) {
            return false;
        }

        // random horizontal dimensions
        final int radiusX = random.nextIntBetweenInclusive(12, 15);
        final int radiusZ = random.nextIntBetweenInclusive(11, 14);
        final int lowestSurfaceY = lowestSurfaceY(level, centerX, centerZ, radiusX, radiusZ);
        // Enough cover
        final int roofY = Math.min(surfaceY - random.nextIntBetweenInclusive(3, 4), lowestSurfaceY - 3);
        // Irregular shape
        final HollowShape shape = HollowShape.random(random, radiusX, radiusZ, roofY - random.nextIntBetweenInclusive(10, 12), roofY);
        if (shape.minFloorY() <= level.getMinY() + 6) {
            return false;
        }

        // Random position for the entrance (within the area of the structure itself)
        final double angle = random.nextDouble() * Math.PI * 2.0;
        final int entryX = centerX + (int) Math.round(Math.cos(angle) * random.nextIntBetweenInclusive(5, 7));
        final int entryZ = centerZ + (int) Math.round(Math.sin(angle) * random.nextIntBetweenInclusive(5, 7));
        final int entrySurfaceY = surfaceY(level, entryX, entryZ);
        final EntrancePath entrance = new EntrancePath(entryX, entryZ, entrySurfaceY, shape.bottomAt(entryX - centerX, entryZ - centerZ) + 1, random.nextDouble() * Math.PI * 2.0);

        if (!hasStableRoof(level, centerX, centerZ, surfaceY, shape) || !isGrassyGround(level.getBlockState(new BlockPos(entryX, entrySurfaceY, entryZ))) || !canCarveChamber(level, centerX, centerZ, shape) || !canCarveEntrance(level, entrance)) {
            return false;
        }

        final AABB hollowBounds = new AABB(
            centerX - radiusX, shape.minFloorY(), centerZ - radiusZ,
            centerX + radiusX + 1, shape.maxCeilingY() + 1, centerZ + radiusZ + 1
        );
        if (!level.getEntitiesOfClass(TreeOfLifeEntity.class, hollowBounds).isEmpty()) {
            return false;
        }

        // Carves the hollow
        carveChamber(level, random, centerX, centerZ, shape);
        decorateChamber(level, random, centerX, centerZ, shape);
        // Carves the entrance
        carveEntranceAndLanding(level, centerX, centerZ, entrance, shape);

        // Extra decoration
        placeVegetation(level, random, centerX, centerZ, entryX, entryZ, shape);

        // Tree of life in the middle
        return spawnTreeOfLife(level, centerX, centerZ, shape.bottomAt(0, 0));
    }

    private static boolean hasStableRoof(WorldGenLevel level, int centerX, int centerZ, int centerSurface, HollowShape shape) {
        int lowest = centerSurface;

        // Sampling surface height at several points across the area
        for (int dx = -shape.radiusX(); dx <= shape.radiusX(); dx += 4) {
            for (int dz = -shape.radiusZ(); dz <= shape.radiusZ(); dz += 4) {
                if (!shape.containsColumn(dx, dz)) {
                    continue;
                }

                final int y = surfaceY(level, centerX + dx, centerZ + dz);
                final BlockPos surface = new BlockPos(centerX + dx, y, centerZ + dz);
                if (!level.getFluidState(surface).isEmpty() || !level.getFluidState(surface.above()).isEmpty()) {
                    return false;
                }

                lowest = Math.min(lowest, y);
            }

        }

        // Requires at least 2 blocks above the highest part of the ceiling
        return lowest - shape.maxCeilingY() >= 2;
    }

    private static int lowestSurfaceY(WorldGenLevel level, int centerX, int centerZ, int radiusX, int radiusZ) {
        int lowest = surfaceY(level, centerX, centerZ);
        for (int dx = -radiusX; dx <= radiusX; dx += 3) {
            for (int dz = -radiusZ; dz <= radiusZ; dz += 3) {
                final double x = dx / (double) radiusX;
                final double z = dz / (double) radiusZ;
                if (x * x + z * z <= 1.05) {
                    lowest = Math.min(lowest, surfaceY(level, centerX + dx, centerZ + dz));
                }

            }

        }

        return lowest;
    }

    /// Validates whether the entire volume can be safely carved, preserving protected blocks and avoiding lava
    private static boolean canCarveChamber(WorldGenLevel level, int centerX, int centerZ, HollowShape shape) {
        final BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int dx = -shape.radiusX(); dx <= shape.radiusX(); dx++) {
            for (int dz = -shape.radiusZ(); dz <= shape.radiusZ(); dz++) {
                if (!shape.containsColumn(dx, dz)) {
                    continue;
                }

                final int bottom = shape.bottomAt(dx, dz);
                final int ceiling = shape.ceilingAt(dx, dz);
                for (int y = bottom; y <= ceiling; y++) {
                    mutableBlockPos.set(centerX + dx, y, centerZ + dz);
                    final BlockState state = level.getBlockState(mutableBlockPos);
                    if (level.getBlockEntity(mutableBlockPos) != null || state.getFluidState().is(FluidTags.LAVA)) {
                        return false;
                    }
                    if (state.is(BlockTags.FEATURES_CANNOT_REPLACE)) {
                        return false;
                    }

                }

            }

        }

        return true;
    }

    /// Checks whether the entrance can be carved without additional blocks interfering
    private static boolean canCarveEntrance(WorldGenLevel level, EntrancePath entrance) {
        final BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int y = entrance.topY(); y >= entrance.landingY(); y--) {
            final int pathX = entrance.xAt(y);
            final int pathZ = entrance.zAt(y);
            for (int ox = -1; ox <= 2; ox++) {
                for (int oz = -1; oz <= 2; oz++) {
                    if (!entrance.carvesOffset(y, ox, oz)) {
                        continue;
                    }

                    mutableBlockPos.set(pathX + ox, y, pathZ + oz);

                    final BlockState state = level.getBlockState(mutableBlockPos);
                    if (level.getBlockEntity(mutableBlockPos) != null || state.getFluidState().is(FluidTags.LAVA)) {
                        return false;
                    }

                }

            }


        }

        return true;
    }

    /// Carves the actual hollow underground
    private static void carveChamber(WorldGenLevel level, RandomSource random, int centerX, int centerZ, HollowShape shape) {
        final BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int dx = -shape.radiusX(); dx <= shape.radiusX(); dx++) {
            for (int dz = -shape.radiusZ(); dz <= shape.radiusZ(); dz++) {
                if (!shape.containsColumn(dx, dz)) {
                    continue;
                }

                final int bottom = shape.bottomAt(dx, dz);
                final int ceiling = shape.ceilingAt(dx, dz);
                mutableBlockPos.set(centerX + dx, bottom, centerZ + dz);
                level.setBlock(mutableBlockPos, lushFloorState(random), 2);

                for (int y = bottom + 1; y < ceiling; y++) {
                    mutableBlockPos.set(centerX + dx, y, centerZ + dz);
                    level.setBlock(mutableBlockPos, Blocks.AIR.defaultBlockState(), 2);
                }

            }

        }

    }

    private static BlockState lushFloorState(RandomSource random) {
        return switch (random.nextInt(12)) {
            case 0, 1 -> Blocks.CLAY.defaultBlockState();
            case 2 -> Blocks.MUD.defaultBlockState();
            // case 3 -> Blocks.TUFF.defaultBlockState();
            // default -> Blocks.MOSS_BLOCK.defaultBlockState();
            case 3 -> Blocks.MOSS_BLOCK.defaultBlockState();
            default -> Blocks.GRASS_BLOCK.defaultBlockState();
        };

    }

    private static void decorateChamber(WorldGenLevel level, RandomSource random, int centerX, int centerZ, HollowShape shape) {
        final BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int dx = -shape.radiusX() - 1; dx <= shape.radiusX() + 1; dx++) {
            for (int dz = -shape.radiusZ() - 1; dz <= shape.radiusZ() + 1; dz++) {
                for (int y = shape.minFloorY() - 1; y <= shape.maxCeilingY() + 1; y++) {
                    mutableBlockPos.set(centerX + dx, y, centerZ + dz);

                    final BlockState current = level.getBlockState(mutableBlockPos);
                    if (current.getFluidState().is(FluidTags.WATER) && isExposed(level, mutableBlockPos)) {
                        level.setBlock(mutableBlockPos, Blocks.MOSS_BLOCK.defaultBlockState(), 2);
                        continue;
                    }
                    if (!isNaturalStone(current) || !isExposed(level, mutableBlockPos)) {
                        continue;
                    }

                    final int n = random.nextInt(14);
                    if (n <= 7) {
                        level.setBlock(mutableBlockPos, Blocks.MOSS_BLOCK.defaultBlockState(), 2);
                    }
                    else if (n == 8) {
                        level.setBlock(mutableBlockPos, Blocks.CLAY.defaultBlockState(), 2);
                    }
                    else if (n == 9) {
                        level.setBlock(mutableBlockPos, Blocks.MUD.defaultBlockState(), 2);
                    }

                }

            }

        }

    }

    private static void carveEntranceAndLanding(WorldGenLevel level, int centerX, int centerZ, EntrancePath entrance, HollowShape shape) {
        final BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int y = entrance.topY() + 1; y >= entrance.landingY(); y--) {
            final int pathX = entrance.xAt(Math.min(y, entrance.topY()));
            final int pathZ = entrance.zAt(Math.min(y, entrance.topY()));
            for (int ox = -1; ox <= 2; ox++) {
                for (int oz = -1; oz <= 2; oz++) {
                    if (!entrance.carvesOffset(Math.min(y, entrance.topY()), ox, oz)) {
                        continue;
                    }

                    mutableBlockPos.set(pathX + ox, y, pathZ + oz);
                    level.setBlock(mutableBlockPos, Blocks.AIR.defaultBlockState(), 2);
                }

            }

        }

        carveLandingPond(level, centerX, centerZ, entrance, shape);
    }

    /// Places a random poll of water in the landing area after going through the entrance
    private static void carveLandingPond(WorldGenLevel level, int centerX, int centerZ, EntrancePath entrance, HollowShape shape) {
        final int landingX = entrance.xAt(entrance.landingY());
        final int landingZ = entrance.zAt(entrance.landingY());
        final int waterY = entrance.landingY();
        final BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

        for (int ox = -4; ox <= 4; ox++) {
            for (int oz = -4; oz <= 4; oz++) {
                final boolean pond = entrance.isCell(ox, oz);
                final boolean shore = !pond && entrance.isNext(ox, oz);
                if (!pond && !shore) {
                    continue;
                }

                final int worldX = landingX + ox;
                final int worldZ = landingZ + oz;
                final int naturalFloor = shape.bottomAt(worldX - centerX, worldZ - centerZ);
                final int topSupport = pond ? waterY - 1 : waterY;
                for (int y = Math.min(naturalFloor, topSupport); y <= topSupport; y++) {
                    mutableBlockPos.set(worldX, y, worldZ);
                    level.setBlock(mutableBlockPos, pond && y == topSupport ? Blocks.CLAY.defaultBlockState() : Blocks.MOSS_BLOCK.defaultBlockState(), 2);
                }

                if (pond) {
                    for (int y = waterY + 1; y <= naturalFloor + 1; y++) {
                        mutableBlockPos.set(worldX, y, worldZ);
                        level.setBlock(mutableBlockPos, Blocks.AIR.defaultBlockState(), 2);
                    }

                    mutableBlockPos.set(worldX, waterY, worldZ);
                    level.setBlock(mutableBlockPos, Blocks.WATER.defaultBlockState(), 2);
                }

            }

        }

    }

    private static void placeVegetation(WorldGenLevel level, RandomSource random, int centerX, int centerZ, int entryX, int entryZ, HollowShape shape) {
        final BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

        placeRootsOfLife(level, random, centerX, centerZ, entryX, entryZ, shape);

        for (int dx = -shape.radiusX() + 1; dx <= shape.radiusX() - 1; dx++) {
            for (int dz = -shape.radiusZ() + 1; dz <= shape.radiusZ() - 1; dz++) {
                if (!shape.containsColumn(dx, dz) || dx * dx + dz * dz < 9 || distanceSquared(centerX + dx, centerZ + dz, entryX, entryZ) < 18 || random.nextFloat() > 0.74F) {
                    continue;
                }

                final int floorY = shape.bottomAt(dx, dz);

                mutableBlockPos.set(centerX + dx, floorY + 1, centerZ + dz);

                if (!level.getBlockState(mutableBlockPos).isAir()) {
                    continue;
                }

                if (shape.ceilingAt(dx, dz) - floorY >= 5 && random.nextInt(9) == 0) {
                    final BlockState tallPlant = random.nextBoolean() ? Blocks.TALL_GRASS.defaultBlockState() : Blocks.LARGE_FERN.defaultBlockState();
                    if (level.getBlockState(mutableBlockPos.above()).isAir() && tallPlant.canSurvive(level, mutableBlockPos)) {
                        DoublePlantBlock.placeAt(level, tallPlant, mutableBlockPos, 2);
                        continue;
                    }

                }

                final BlockState vegetation = switch (random.nextInt(24)) {
                    case 0, 1 -> Blocks.OXEYE_DAISY.defaultBlockState();
                    case 2, 3, 4 -> Blocks.LILY_OF_THE_VALLEY.defaultBlockState();
                    case 5, 6, 7 -> Blocks.WHITE_TULIP.defaultBlockState();
                    case 8, 9, 10 -> Blocks.FERN.defaultBlockState();
                    case 11, 12, 13, 14, 15, 16, 17, 18, 19 -> Blocks.SHORT_GRASS.defaultBlockState();
                    default -> Blocks.MOSS_CARPET.defaultBlockState();
                };

                if (vegetation.canSurvive(level, mutableBlockPos)) {
                    level.setBlock(mutableBlockPos, vegetation, 2);
                }

            }

        }

        for (int dx = -shape.radiusX() + 2; dx <= shape.radiusX() - 2; dx++) {
            for (int dz = -shape.radiusZ() + 2; dz <= shape.radiusZ() - 2; dz++) {
                if (!shape.containsColumn(dx, dz) || dx * dx + dz * dz < 16 || random.nextFloat() > 0.27F) {
                    continue;
                }

                final int ceiling = shape.ceilingAt(dx, dz);

                mutableBlockPos.set(centerX + dx, ceiling - 1, centerZ + dz);

                if (!level.getBlockState(mutableBlockPos).isAir()) {
                    continue;
                }

                if (random.nextInt(8) == 0) {
                    final BlockState blossom = Blocks.SPORE_BLOSSOM.defaultBlockState();
                    if (blossom.canSurvive(level, mutableBlockPos)) {
                        level.setBlock(mutableBlockPos, blossom, 2);
                    }

                    continue;
                }

                final int maxLength = Math.min(
                    random.nextIntBetweenInclusive(2, 6), ceiling - shape.bottomAt(dx, dz) - 3
                );

                for (int segment = 0; segment < maxLength; segment++) {
                    mutableBlockPos.set(centerX + dx, ceiling - 1 - segment, centerZ + dz);

                    if (!level.getBlockState(mutableBlockPos).isAir()) {
                        break;
                    }

                    final boolean tip = segment == maxLength - 1;
                    final BlockState vine = tip ? Blocks.CAVE_VINES.defaultBlockState().setValue(BlockStateProperties.BERRIES, random.nextInt(3) == 0) : Blocks.CAVE_VINES_PLANT.defaultBlockState();
                    if (vine.canSurvive(level, mutableBlockPos)) {
                        level.setBlock(mutableBlockPos, vine, 2);
                    }

                }

            }

        }

    }

    private static void placeRootsOfLife(WorldGenLevel level, RandomSource random, int centerX, int centerZ, int entryX, int entryZ, HollowShape shape) {
        final int target = random.nextIntBetweenInclusive(4, 9);
        final BlockState root = NomenDubiumBlocks.ROOT_OF_LIFE.get().defaultBlockState();
        final BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int placed = 0;

        for (int attempt = 0; attempt < 360 && placed < target; attempt++) {
            final int dx = random.nextIntBetweenInclusive(-shape.radiusX() + 2, shape.radiusX() - 2);
            final int dz = random.nextIntBetweenInclusive(-shape.radiusZ() + 2, shape.radiusZ() - 2);
            if (!shape.containsColumn(dx, dz) || dx * dx + dz * dz < 25 || distanceSquared(centerX + dx, centerZ + dz, entryX, entryZ) < 24) {
                continue;
            }

            mutableBlockPos.set(centerX + dx, shape.bottomAt(dx, dz) + 1, centerZ + dz);

            if (level.getBlockState(mutableBlockPos).isAir() && root.canSurvive(level, mutableBlockPos)) {
                level.setBlock(mutableBlockPos, root, 2);
                placed++;
            }

        }

        for (int dx = -shape.radiusX() + 2; dx <= shape.radiusX() - 2 && placed < target; dx++) {
            for (int dz = -shape.radiusZ() + 2; dz <= shape.radiusZ() - 2 && placed < target; dz++) {
                if (!shape.containsColumn(dx, dz) || dx * dx + dz * dz < 25 || distanceSquared(centerX + dx, centerZ + dz, entryX, entryZ) < 24) {
                    continue;
                }

                mutableBlockPos.set(centerX + dx, shape.bottomAt(dx, dz) + 1, centerZ + dz);
                if (level.getBlockState(mutableBlockPos).isAir() && root.canSurvive(level, mutableBlockPos)) {
                    level.setBlock(mutableBlockPos, root, 2);
                    placed++;
                }

            }

        }

    }

    private static boolean spawnTreeOfLife(WorldGenLevel level, int centerX, int centerZ, int floorY) {
        final TreeOfLifeEntity tree = NomenDubiumEntities.TREE_OF_LIFE.get().create(level.getLevel(), EntitySpawnReason.STRUCTURE);
        if (tree == null) {
            return false;
        }

        tree.snapTo(centerX + 0.5, floorY + 1.0, centerZ + 0.5, 90, 0);
        tree.setYBodyRot(90);
        tree.setYHeadRot(90);
        tree.yRotO = 90;
        tree.yBodyRotO = 90;
        tree.yHeadRotO = 90;
        tree.setNoGravity(true);
        tree.addTag("nomendubium:naturally_generated");

        return level.addFreshEntity(tree);
    }

    private static boolean isGrassySurface(BlockState state) {
        return state.is(BlockTags.GRASS_BLOCKS) || state.is(BlockTags.DIRT) || state.is(BlockTags.MOSS_BLOCKS);
    }

    private static boolean isGrassyGround(BlockState state) {
        return state.is(BlockTags.GRASS_BLOCKS) || state.is(BlockTags.DIRT) || state.is(BlockTags.MOSS_BLOCKS);
    }

    private static boolean isNaturalStone(BlockState state) {
        return state.is(BlockTags.OVERWORLD_CARVER_REPLACEABLES) || state.is(BlockTags.DIRT) || state.is(BlockTags.SAND) || state.is(BlockTags.TERRACOTTA) || state.is(Blocks.GRAVEL) || state.is(Blocks.MUD) || state.is(Blocks.PACKED_MUD);
    }

    private static boolean isExposed(WorldGenLevel level, BlockPos pos) {
        for (final Direction direction : Direction.values()) {
            if (level.getBlockState(pos.relative(direction)).isAir()) {
                return true;
            }

        }

        return false;
    }

    private static int surfaceY(WorldGenLevel level, int x, int z) {
        final int top = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) - 1;
        final int lowestScan = Math.max(level.getMinY(), top - 24);
        final BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int y = top; y >= lowestScan; y--) {
            mutableBlockPos.set(x, y, z);
            final BlockState state = level.getBlockState(mutableBlockPos);
            if (
                state.is(BlockTags.GRASS_BLOCKS) || state.is(BlockTags.DIRT) || state.is(BlockTags.MUD) || state.is(BlockTags.MOSS_BLOCKS)
                || state.is(BlockTags.SAND) || state.is(BlockTags.TERRACOTTA) || state.is(BlockTags.BASE_STONE_OVERWORLD) || state.is(Blocks.GRAVEL)
            ) {
                return y;
            }

        }

        return top;
    }

    private static int distanceSquared(int x1, int z1, int x2, int z2) {
        final int dx = x1 - x2;
        final int dz = z1 - z2;
        return dx * dx + dz * dz;
    }

    private record HollowShape(
            int radiusX,
            int radiusZ,
            int baseFloorY,
            int baseCeilingY,
            double edgePhase,
            double detailPhase,
            double lobeAngle
    ) {

        static HollowShape random(RandomSource random, int radiusX, int radiusZ, int floorY, int ceilingY) {
            return new HollowShape(radiusX, radiusZ, floorY, ceilingY, random.nextDouble() * Math.PI * 2.0, random.nextDouble() * Math.PI * 2.0, random.nextDouble() * Math.PI * 2.0);
        }

        int minFloorY() {
            return this.baseFloorY - 2;
        }

        int maxCeilingY() {
            return this.baseCeilingY + 1;
        }

        boolean containsColumn(int dx, int dz) {
            return edgeDistance(dx, dz) < 1.0;
        }

        int bottomAt(int dx, int dz) {
            if (dx * dx + dz * dz <= 16) {
                return this.baseFloorY;
            }

            final double edge = edgeDistance(dx, dz);
            final int rimRise = edge > 0.68 ? (int) Math.round((edge - 0.68) * 9.0) : 0;
            final int floorNoise = (int) Math.round(Math.sin(dx * 0.43 + this.detailPhase) * 0.9 + Math.cos(dz * 0.37 - this.edgePhase) * 0.7 + Math.sin((dx - dz) * 0.21 + this.lobeAngle) * 0.55);
            return this.baseFloorY + rimRise + floorNoise;
        }

        int ceilingAt(int dx, int dz) {
            if (dx * dx + dz * dz <= 16) {
                return this.baseCeilingY;
            }

            final double edge = edgeDistance(dx, dz);
            final int rimDrop = edge > 0.72 ? (int) Math.round((edge - 0.72) * 8.0) : 0;
            final int ceilingNoise = Math.max(-2, Math.min(1, (int) Math.round(Math.cos(dx * 0.31 - this.edgePhase) * 1.05
                    + Math.sin(dz * 0.47 + this.detailPhase) * 0.85 + Math.cos((dx + dz) * 0.24 - this.lobeAngle) * 0.6)));
            return Math.max(bottomAt(dx, dz) + 4, this.baseCeilingY - rimDrop + ceilingNoise);
        }

        /// Computes a normalized distance value for the given column. Values lower than 1 are inside the hollow, and the shape
        /// is constructed as an ellipse with extra sin perturbations
        private double edgeDistance(int dx, int dz) {
            final double normalizedX = dx / (double) this.radiusX;
            final double normalizedZ = dz / (double) this.radiusZ;
            final double angle = Math.atan2(normalizedZ, normalizedX);
            final double boundary = 0.88 + Math.sin(angle * 3.0 + this.edgePhase) * 0.13 + Math.cos(angle * 5.0 - this.detailPhase) * 0.08
                    + Math.sin(dx * 0.34 + this.detailPhase) * Math.cos(dz * 0.29 - this.edgePhase) * 0.05;
            final double mainDistance = Math.sqrt(normalizedX * normalizedX + normalizedZ * normalizedZ) / boundary;

            final double lobeX = Math.cos(this.lobeAngle) * this.radiusX * 0.3;
            final double lobeZ = Math.sin(this.lobeAngle) * this.radiusZ * 0.3;
            final double firstLobe = distance(dx - lobeX, dz - lobeZ, this.radiusX * 0.54, this.radiusZ * 0.48);
            final double secondLobe = distance(dx + lobeZ * 0.58, dz - lobeX * 0.46, this.radiusX * 0.43, this.radiusZ * 0.5);
            return Math.min(mainDistance, Math.min(firstLobe, secondLobe));
        }

        private static double distance(double dx, double dz, double radiusX, double radiusZ) {
            final double x = dx / radiusX;
            final double z = dz / radiusZ;
            return Math.sqrt(x * x + z * z);
        }

    }

    private record EntrancePath(
            int topX,
            int topZ,
            int topY,
            int landingY,
            double phase
    ) {

        int xAt(int y) {
            final double progress = progress(y);
            final double envelope = Math.sin(progress * Math.PI);
            return this.topX + (int) Math.round(envelope * Math.sin(this.phase + progress * Math.PI * 1.5) * 1.25);
        }

        int zAt(int y) {
            final double progress = progress(y);
            final double envelope = Math.sin(progress * Math.PI);
            return this.topZ + (int) Math.round(envelope * Math.cos(this.phase + progress * Math.PI * 1.25) * 1.25);
        }

        /// Determines whether a block at a given Y level and offset should be carved out. The central 2x2 area is always
        /// carved, while the outer area is carved based on irregular elliptical shapes
        boolean carvesOffset(int y, int offsetX, int offsetZ) {
            if (offsetX >= 0 && offsetX <= 1 && offsetZ >= 0 && offsetZ <= 1) {
                return true;
            }
            if (y >= this.topY - 1) {
                return false;
            }

            final double progress = progress(y);
            final double radiusX = 1.28 + Math.sin(this.phase + progress * Math.PI * 3.0) * 0.2;
            final double radiusZ = 1.28 + Math.cos(this.phase - progress * Math.PI * 2.6) * 0.2;
            final double x = (offsetX - 0.5) / radiusX;
            final double z = (offsetZ - 0.5) / radiusZ;
            final double irregularEdge = 1.0 + Math.sin(offsetX * 1.7 + offsetZ * 2.3 + y * 0.41 + this.phase) * 0.12;
            return Math.sqrt(x * x + z * z) < irregularEdge;
        }

        boolean isCell(int offsetX, int offsetZ) {
            final double angle = Math.atan2(offsetZ, offsetX);
            final double radius = 2.7 + Math.sin(angle * 3.0 + this.phase) * 0.45 + Math.cos(angle * 5.0 - this.phase) * 0.25;
            return Math.sqrt(offsetX * offsetX + offsetZ * offsetZ) <= radius;
        }

        boolean isNext(int offsetX, int offsetZ) {
            return isCell(offsetX + 1, offsetZ) || isCell(offsetX - 1, offsetZ) || isCell(offsetX, offsetZ + 1) || isCell(offsetX, offsetZ - 1);
        }

        private double progress(int y) {
            if (this.topY == this.landingY) {
                return 1.0;
            }

            return Math.max(0, Math.min(1, (this.topY - y) / (double) (this.topY - this.landingY)));
        }

    }

}
