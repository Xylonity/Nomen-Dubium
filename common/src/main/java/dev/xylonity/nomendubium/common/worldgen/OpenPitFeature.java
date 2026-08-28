package dev.xylonity.nomendubium.common.worldgen;

import com.mojang.serialization.Codec;
import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.registry.NomenDubiumBlocks;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.storage.loot.LootTable;

/// Derived from my own implementation
///
/// All methods do the same thing, 3D block search on a certain volume. TODO: refactor consistent logic into singular methods
///
/// https://github.com/Xylonity/Parallax/tree/v.1.20.1/common/src/main/java/dev/xylonity/parallax/common/worldgen/SereCraterFeature.java
/// https://github.com/Xylonity/Parallax/tree/v.1.20.1/common/src/main/java/dev/xylonity/parallax/common/worldgen/CitadelSpireFeature.java
public final class OpenPitFeature extends Feature<NoneFeatureConfiguration> {

    private static final double SPIRAL_TURNS = 1.35D;
    private static final double SANDY_RIM_RADIUS = 1.30D;

    private static final ResourceKey<LootTable> ARCHAEOLOGY_LOOT = ResourceKey.create(Registries.LOOT_TABLE, NomenDubium.of("archaeology/open_pit"));

    public OpenPitFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        final WorldGenLevel level = context.level();
        final RandomSource random = context.random();
        final int centerX = context.origin().getX();
        final int centerZ = context.origin().getZ();
        final int centerSurface = surfaceY(level, centerX, centerZ);

        final PitShape shape = new PitShape(
            random.nextIntBetweenInclusive(14, 18), random.nextIntBetweenInclusive(12, 16), random.nextIntBetweenInclusive(12, 17), random.nextDouble() * Math.PI,
            random.nextDouble() * Math.PI * 2.0, random.nextDouble() * Math.PI * 2.0, random.nextDouble() * Math.PI * 2.0, random.nextBoolean() ? 1 : -1
        );

        if (centerSurface - shape.depth() <= level.getMinY() + 4) {
            return false;
        }

        reinforceFoundation(level, centerX, centerZ, centerSurface, shape);
        clearVegetation(level, centerX, centerZ, shape);
        carveBowl(level, centerX, centerZ, centerSurface, shape);
        dressExposedStone(level, random, centerX, centerZ, centerSurface, shape);
        dressSandyRim(level, centerX, centerZ, shape);
        clearVegetation(level, centerX, centerZ, shape);
        placeFloorDeposits(level, random, centerX, centerZ, shape);
        placeHazards(level, random, centerX, centerZ, shape);

        return true;
    }

    /// Adds extra blocks below the pit so the whole structure doesn't break when updating a sand block
    private static void reinforceFoundation(WorldGenLevel level, int centerX, int centerZ, int centerSurface, PitShape shape) {
        final BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        final int radiusX = shape.extentX() + 4;
        final int radiusZ = shape.extentZ() + 4;
        for (int dx = -radiusX; dx <= radiusX; dx++) {
            for (int dz = -radiusZ; dz <= radiusZ; dz++) {
                final double distance = shape.normalizedDistance(dx, dz);
                if (distance > SANDY_RIM_RADIUS) {
                    continue;
                }

                final int x = centerX + dx;
                final int z = centerZ + dz;
                final int surface = surfaceY(level, x, z);
                final int supportY = distance < 1.0D && shape.depthAt(dx, dz) > 0 ? shape.floorAt(surface, centerSurface, dx, dz) : surface;
                for (int y = supportY; y >= supportY - 8; y--) {
                    mutableBlockPos.set(x, y, z);

                    final BlockState current = level.getBlockState(mutableBlockPos);
                    if (!current.isCollisionShapeFullBlock(level, mutableBlockPos) || !current.getFluidState().isEmpty() || isLooseSurface(current)) {
                        level.setBlock(mutableBlockPos, Blocks.SANDSTONE.defaultBlockState(), 2);
                    }

                }

            }

        }

    }

    /// Removes flying vegetation
    private static void clearVegetation(WorldGenLevel level, int centerX, int centerZ, PitShape shape) {
        final BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        final int radiusX = shape.extentX() + 6;
        final int radiusZ = shape.extentZ() + 6;
        for (int dx = -radiusX; dx <= radiusX; dx++) {
            for (int dz = -radiusZ; dz <= radiusZ; dz++) {
                final int x = centerX + dx;
                final int z = centerZ + dz;
                final int ground = surfaceY(level, x, z);
                final int top = worldSurfaceY(level, x, z);
                final boolean clearedSurface = shape.normalizedDistance(dx, dz) <= SANDY_RIM_RADIUS;
                for (int y = ground + 1; y <= top; y++) {
                    mutableBlockPos.set(x, y, z);
                    final BlockState state = level.getBlockState(mutableBlockPos);
                    final boolean treeBlock = state.is(BlockTags.LOGS) || state.is(BlockTags.LEAVES) || state.is(BlockTags.BEEHIVES);
                    final boolean surfacePlant = clearedSurface && state.getFluidState().isEmpty() && (state.canBeReplaced() || state.is(BlockTags.REPLACEABLE_BY_TREES));
                    if (treeBlock || surfacePlant) {
                        level.setBlock(mutableBlockPos, Blocks.AIR.defaultBlockState(), 2);
                    }

                }

            }

        }

    }

    /// Places a sandy rim around the edge of the pit
    private static void dressSandyRim(WorldGenLevel level, int centerX, int centerZ, PitShape shape) {
        final BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        final int radiusX = shape.extentX() + 4;
        final int radiusZ = shape.extentZ() + 4;
        for (int dx = -radiusX; dx <= radiusX; dx++) {
            for (int dz = -radiusZ; dz <= radiusZ; dz++) {
                final double distance = shape.normalizedDistance(dx, dz);
                if (distance < 1.0D || distance > SANDY_RIM_RADIUS) {
                    continue;
                }

                final int x = centerX + dx;
                final int z = centerZ + dz;

                mutableBlockPos.set(x, surfaceY(level, x, z), z);

                final BlockState current = level.getBlockState(mutableBlockPos);
                if (!isNaturalStone(current) || !level.getFluidState(mutableBlockPos.above()).isEmpty()) {
                    continue;
                }

                final BlockPos below = mutableBlockPos.below();
                final BlockState rimState = level.getBlockState(below).isFaceSturdy(level, below, Direction.UP) ? Blocks.SAND.defaultBlockState() : Blocks.SANDSTONE.defaultBlockState();
                level.setBlock(mutableBlockPos, rimState, 2);
            }

        }

    }

    private static void carveBowl(WorldGenLevel level, int centerX, int centerZ, int centerSurface, PitShape shape) {
        final BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int dx = -shape.extentX(); dx <= shape.extentX(); dx++) {
            for (int dz = -shape.extentZ(); dz <= shape.extentZ(); dz++) {
                final int localDepth = shape.depthAt(dx, dz);
                if (localDepth <= 0) {
                    continue;
                }

                final int x = centerX + dx;
                final int z = centerZ + dz;
                final int top = surfaceY(level, x, z);
                final int floor = shape.floorAt(top, centerSurface, dx, dz);
                for (int y = top; y > floor; y--) {
                    mutableBlockPos.set(x, y, z);

                    if (isCarvable(level.getBlockState(mutableBlockPos))) {
                        level.setBlock(mutableBlockPos, Blocks.AIR.defaultBlockState(), 2);
                    }

                }

            }

        }

    }

    /// Permutes stone blocks into other variants
    private static void dressExposedStone(WorldGenLevel level, RandomSource random, int centerX, int centerZ, int centerSurface, PitShape shape) {
        final BlockPos.MutableBlockPos cmutableBlockPosrsor = new BlockPos.MutableBlockPos();
        final int radiusX = shape.extentX() + 1;
        final int radiusZ = shape.extentZ() + 1;

        for (int dx = -radiusX; dx <= radiusX; dx++) {
            for (int dz = -radiusZ; dz <= radiusZ; dz++) {
                final boolean excavatedColumn = shape.depthAt(dx, dz) > 0;
                if (!excavatedColumn && !shape.touchesExcavation(dx, dz)) {
                    continue;
                }

                final int x = centerX + dx;
                final int z = centerZ + dz;
                final int columnSurface = surfaceY(level, x, z);
                final int lowerY = Math.min(columnSurface, centerSurface) - shape.depth() - 4;
                for (int y = lowerY; y <= columnSurface + 1; y++) {
                    cmutableBlockPosrsor.set(x, y, z);
                    final BlockState current = level.getBlockState(cmutableBlockPosrsor);
                    final boolean exposedByPit = excavatedColumn ? isExposed(level, cmutableBlockPosrsor) : isLaterallyExposed(level, cmutableBlockPosrsor);
                    if (!isNaturalStone(current) || !exposedByPit) {
                        continue;
                    }

                    final BlockPos below = cmutableBlockPosrsor.below();
                    final boolean supported = level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
                    if (!supported && isLooseSurface(current)) {
                        level.setBlock(cmutableBlockPosrsor, Blocks.AIR.defaultBlockState(), 2);
                        continue;
                    }

                    level.setBlock(cmutableBlockPosrsor, randomFor(random, centerSurface - y, supported), 2);
                }

            }

        }

    }

    /// Chooses the replacement stone blocks
    private static BlockState randomFor(RandomSource random, int depth, boolean supported) {
        if (random.nextInt(180) == 0) {
            return NomenDubiumBlocks.FOSSIL_BLOCK.get().defaultBlockState();
        }

        if (depth <= 2) {
            final int surfaceVariant = random.nextInt(10);
            if (supported && surfaceVariant <= 3) {
                return Blocks.SAND.defaultBlockState();
            }
            return switch (surfaceVariant) {
                case 4, 5 -> Blocks.CUT_SANDSTONE.defaultBlockState();
                default -> Blocks.SANDSTONE.defaultBlockState();
            };

        }

        final int yeh = Math.floorMod(depth, 7);
        if (yeh == 0 || yeh == 1) {
            return switch (random.nextInt(10)) {
                case 0, 1 -> Blocks.SMOOTH_SANDSTONE.defaultBlockState();
                case 2, 3, 4, 5, 6, 7 -> Blocks.CUT_SANDSTONE.defaultBlockState();
                default -> Blocks.SANDSTONE.defaultBlockState();
            };

        }

        return switch (random.nextInt(12)) {
            case 0, 1 -> Blocks.SMOOTH_SANDSTONE.defaultBlockState();
            case 2, 3, 4 -> Blocks.CUT_SANDSTONE.defaultBlockState();
            default -> Blocks.SANDSTONE.defaultBlockState();
        };

    }

    /// Places sand, suspicious sand, etc. on the floor of the pit itself
    private static void placeFloorDeposits(WorldGenLevel level, RandomSource random, int centerX, int centerZ, PitShape shape) {
        int suspiciousSand = 0;
        final DepositPatch[] patches = createDepositPatches(random, shape);
        final List<BlockPos> floorCand = new ArrayList<>();
        final double sedimentPhase = random.nextDouble() * Math.PI * 2.0;
        final BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

        for (int dx = -shape.extentX(); dx <= shape.extentX(); dx++) {
            for (int dz = -shape.extentZ(); dz <= shape.extentZ(); dz++) {
                final int localDepth = shape.depthAt(dx, dz);
                if (localDepth < 2 || shape.isRamp(dx, dz)) {
                    continue;
                }

                double patchInfluence = 0.0D;
                for (final DepositPatch patch : patches) {
                    patchInfluence = Math.max(patchInfluence, patch.at(dx, dz));
                }
                if (patchInfluence <= 0.0D) {
                    continue;
                }

                final double noise = (Math.sin((centerX + dx) * 0.22D + sedimentPhase) + Math.cos((centerZ + dz) * 0.19D - sedimentPhase)) * 0.28D;
                final double depositHeight = patchInfluence + noise;
                if (depositHeight < 0.22D) {
                    continue;
                }

                final int x = centerX + dx;
                final int z = centerZ + dz;
                final int y = surfaceY(level, x, z);

                mutableBlockPos.set(x, y, z);

                if (!level.getBlockState(mutableBlockPos.above()).isAir() || !level.getBlockState(mutableBlockPos).isFaceSturdy(level, mutableBlockPos, Direction.UP)) {
                    continue;
                }

                level.setBlock(mutableBlockPos, Blocks.SAND.defaultBlockState(), 2);

                floorCand.add(new BlockPos(x, y, z));

                final double suspiciousChance = 0.085D + Mth.clamp(depositHeight / 8.0D, 0.0D, 1.0D) * 0.160D;
                if (suspiciousSand < 40 && random.nextDouble() < suspiciousChance) {
                    placeSuspiciousSand(level, mutableBlockPos, random.nextLong());
                    suspiciousSand++;
                    continue;
                }

                if (depositHeight < 0.45D) {
                    continue;
                }

                final int layers = Mth.clamp(Mth.floor(depositHeight + 1.0D), 1, 8);
                final BlockState sediment = NomenDubiumBlocks.SEDIMENT.get().defaultBlockState().setValue(SnowLayerBlock.LAYERS, layers);
                level.setBlock(mutableBlockPos.above(), sediment, 2);
            }

        }

        while (suspiciousSand < 28 && !floorCand.isEmpty()) {
            final BlockPos candidate = floorCand.remove(random.nextInt(floorCand.size()));
            if (level.getBlockState(candidate).is(Blocks.SUSPICIOUS_SAND)) {
                continue;
            }

            final BlockPos above = candidate.above();
            if (level.getBlockState(above).is(NomenDubiumBlocks.SEDIMENT.get())) {
                level.setBlock(above, Blocks.AIR.defaultBlockState(), 2);
            }

            placeSuspiciousSand(level, candidate, random.nextLong());
            suspiciousSand++;
        }

    }

    private static DepositPatch[] createDepositPatches(RandomSource random, PitShape shape) {
        final int patchCount = random.nextIntBetweenInclusive(12, 16);
        final DepositPatch[] patches = new DepositPatch[patchCount];
        for (int index = 0; index < patchCount; index++) {
            int patchX = 0;
            int patchZ = 0;
            for (int attempt = 0; attempt < 24; attempt++) {
                final int x = random.nextInt(shape.extentX() * 2 + 1) - shape.extentX();
                final int z = random.nextInt(shape.extentZ() * 2 + 1) - shape.extentZ();
                if (shape.depthAt(x, z) >= 4 && shape.normalizedDistance(x, z) <= 0.76D && !shape.isRamp(x, z)) {
                    patchX = x;
                    patchZ = z;
                    break;
                }

            }

            patches[index] = new DepositPatch(patchX, patchZ, 5.2D + random.nextDouble() * 3.4D, random.nextIntBetweenInclusive(7, 8));
        }

        return patches;
    }

    private static void placeHazards(WorldGenLevel level, RandomSource random, int centerX, int centerZ, PitShape shape) {
        final BlockPos[] spawners = new BlockPos[2];
        spawners[0] = placeSpawner(level, random, centerX, centerZ, shape, null);
        spawners[1] = placeSpawner(level, random, centerX, centerZ, shape, spawners[0]);
        placeCobwebs(level, random, centerX, centerZ, shape, spawners);
    }

    private static BlockPos placeSpawner(WorldGenLevel level, RandomSource random, int centerX, int centerZ, PitShape shape, BlockPos otherSpawner) {
        for (int attempt = 0; attempt < 256; attempt++) {
            final int dx = random.nextInt(shape.extentX() * 2 + 1) - shape.extentX();
            final int dz = random.nextInt(shape.extentZ() * 2 + 1) - shape.extentZ();
            final BlockPos spawner = tryPlaceSpawnerAt(level, random, centerX, centerZ, shape, otherSpawner, dx, dz);
            if (spawner != null) {
                return spawner;
            }

        }

        for (int dz = -shape.extentZ(); dz <= shape.extentZ(); dz++) {
            for (int dx = -shape.extentX(); dx <= shape.extentX(); dx++) {
                final BlockPos spawner = tryPlaceSpawnerAt(level, random, centerX, centerZ, shape, otherSpawner, dx, dz);
                if (spawner != null) {
                    return spawner;
                }

            }

        }

        return null;
    }

    /// Validates a specific position for a spawner and places it if it's a correct position
    private static BlockPos tryPlaceSpawnerAt(WorldGenLevel level, RandomSource random, int centerX, int centerZ, PitShape shape, BlockPos otherSpawner, int dx, int dz) {
        if (shape.depthAt(dx, dz) < shape.depth() - 3 || shape.isRamp(dx, dz)) {
            return null;
        }

        final int x = centerX + dx;
        final int z = centerZ + dz;
        final int floorY = structuralFloorY(level, x, z);
        final BlockPos spawner = new BlockPos(x, floorY, z);
        final BlockState currentFloor = level.getBlockState(spawner);
        final BlockState aboveSpawner = level.getBlockState(spawner.above());
        if (otherSpawner != null) {
            final int separationX = spawner.getX() - otherSpawner.getX();
            final int separationZ = spawner.getZ() - otherSpawner.getZ();
            if (separationX * separationX + separationZ * separationZ < 36) {
                return null;
            }

        }

        if (!currentFloor.isFaceSturdy(level, spawner, Direction.UP) || currentFloor.is(Blocks.SUSPICIOUS_SAND) || (!aboveSpawner.isAir() && !aboveSpawner.is(NomenDubiumBlocks.SEDIMENT.get())) || !level.getBlockState(spawner.above(2)).isAir()) {
            return null;
        }

        if (aboveSpawner.is(NomenDubiumBlocks.SEDIMENT.get())) {
            level.setBlock(spawner.above(), Blocks.AIR.defaultBlockState(), 2);
        }

        level.setBlock(spawner, Blocks.SPAWNER.defaultBlockState(), 2);
        level.getBlockEntity(spawner, BlockEntityType.MOB_SPAWNER).ifPresent(blockEntity -> blockEntity.setEntityId(randomSpawnerMob(random), random));

        return spawner;
    }

    private static EntityType<?> randomSpawnerMob(RandomSource random) {
        return switch (random.nextInt(10)) {
            case 0, 1 -> EntityType.PARCHED;
            case 2, 3, 4 -> EntityType.HUSK;
            default -> EntityType.CAVE_SPIDER;
        };

    }

    private static void placeCobwebs(WorldGenLevel level, RandomSource random, int centerX, int centerZ, PitShape shape, BlockPos[] spawners) {
        int patches = 0;
        final int target = random.nextIntBetweenInclusive(4, 8);
        for (final BlockPos spawner : spawners) {
            if (spawner != null && placeCobwebPatch(level, random, spawner.above(), false)) {
                patches++;
            }

        }

        for (int attempt = 0; attempt < target * 24 && patches < target; attempt++) {
            final int dx = random.nextInt(shape.extentX() * 2 + 1) - shape.extentX();
            final int dz = random.nextInt(shape.extentZ() * 2 + 1) - shape.extentZ();
            if (shape.depthAt(dx, dz) < 4 || shape.isRamp(dx, dz)) {
                continue;
            }

            final int x = centerX + dx;
            final int z = centerZ + dz;
            final int floorY = structuralFloorY(level, x, z);
            final BlockPos candidate = new BlockPos(x, floorY + random.nextIntBetweenInclusive(1, 3), z);
            if (placeCobwebPatch(level, random, candidate, true)) {
                patches++;
            }

        }

    }

    private static boolean placeCobwebPatch(WorldGenLevel level, RandomSource random, BlockPos origin, boolean requireWall) {
        if (!tryPlaceCobweb(level, origin, requireWall)) {
            return false;
        }

        final int targetSize = random.nextIntBetweenInclusive(1, 4);
        final List<BlockPos> patch = new ArrayList<>();

        patch.add(origin);

        for (int attempt = 0; attempt < targetSize * 12 && patch.size() < targetSize; attempt++) {
            final BlockPos source = patch.get(random.nextInt(patch.size()));
            final Direction direction = Direction.values()[random.nextInt(Direction.values().length)];
            final BlockPos candidate = source.relative(direction);
            if (tryPlaceCobweb(level, candidate, false)) {
                patch.add(candidate);
            }

        }

        return true;
    }

    private static boolean tryPlaceCobweb(WorldGenLevel level, BlockPos pos, boolean requireWall) {
        if (!level.getBlockState(pos).isAir() || level.getBlockState(pos.below()).is(Blocks.SUSPICIOUS_SAND)) {
            return false;
        }

        if (requireWall) {
            boolean wall = false;
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                final BlockPos neighbor = pos.relative(direction);
                if (level.getBlockState(neighbor).isFaceSturdy(level, neighbor, direction.getOpposite())) {
                    wall = true;
                    break;
                }

            }
            if (!wall) {
                return false;
            }

        }

        level.setBlock(pos, Blocks.COBWEB.defaultBlockState(), 2);

        return true;
    }

    private static int structuralFloorY(WorldGenLevel level, int x, int z) {
        final BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(x, surfaceY(level, x, z), z);
        if (level.getBlockState(mutableBlockPos).is(NomenDubiumBlocks.SEDIMENT.get())) {
            mutableBlockPos.move(Direction.DOWN);
        }

        return mutableBlockPos.getY();
    }

    private static void placeSuspiciousSand(WorldGenLevel level, BlockPos pos, long lootSeed) {
        level.setBlock(pos, Blocks.SUSPICIOUS_SAND.defaultBlockState(), 3);
        level.getBlockEntity(pos, BlockEntityType.BRUSHABLE_BLOCK)
            .ifPresent(blockEntity -> blockEntity.setLootTable(ARCHAEOLOGY_LOOT, lootSeed));
    }

    private static boolean isCarvable(BlockState state) {
        return !state.is(BlockTags.FEATURES_CANNOT_REPLACE);
    }

    private static boolean isNaturalStone(BlockState state) {
        return state.is(BlockTags.OVERWORLD_CARVER_REPLACEABLES) || state.is(BlockTags.GRASS_BLOCKS) || state.is(BlockTags.DIRT) || state.is(BlockTags.SAND) || state.is(BlockTags.TERRACOTTA)
            || state.is(BlockTags.SNOW) || state.is(Blocks.GRAVEL) || state.is(Blocks.PACKED_MUD) || state.is(Blocks.MUD);
    }

    private static boolean isLooseSurface(BlockState state) {
        return state.is(BlockTags.GRASS_BLOCKS) || state.is(BlockTags.DIRT) || state.is(BlockTags.SAND) || state.is(BlockTags.SNOW)
                || state.is(Blocks.GRAVEL) || state.is(Blocks.PACKED_MUD) || state.is(Blocks.MUD);
    }

    private static boolean isExposed(WorldGenLevel level, BlockPos pos) {
        for (final Direction direction : new Direction[]{ Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST }) {
            if (level.getBlockState(pos.relative(direction)).isAir()) {
                return true;
            }

        }

        return false;
    }

    private static boolean isLaterallyExposed(WorldGenLevel level, BlockPos pos) {
        for (final Direction direction : Direction.Plane.HORIZONTAL) {
            if (level.getBlockState(pos.relative(direction)).isAir()) {
                return true;
            }

        }

        return false;
    }

    private static int surfaceY(WorldGenLevel level, int x, int z) {
        final BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z) - 1;
        while (y > level.getMinY()) {
            mutableBlockPos.set(x, y, z);

            final BlockState state = level.getBlockState(mutableBlockPos);
            final boolean treeBlock = state.is(BlockTags.LOGS) || state.is(BlockTags.LEAVES);
            final boolean surfacePlant = state.getFluidState().isEmpty() && state.canBeReplaced();
            if (!treeBlock && !surfacePlant) {
                break;
            }

            y--;
        }

        return y;
    }

    private static int worldSurfaceY(WorldGenLevel level, int x, int z) {
        return level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) - 1;
    }

    private static double smoothstep(double progress) {
        return progress * progress * (3.0D - 2.0D * progress);
    }

    private record DepositPatch(
            double x,
            double z,
            double radius,
            int peakLayers
    ) {

        double at(int x, int z) {
            final double distance = Math.sqrt(Mth.square(x - this.x) + Mth.square(z - this.z));
            if (distance >= this.radius) {
                return 0;
            }

            final double progress = 1 - distance / this.radius;
            return smoothstep(progress) * this.peakLayers;
        }

    }

    private record PitShape(
        int radiusX,
        int radiusZ,
        int depth,
        double rotation,
        double edgePhaseA,
        double edgePhaseB,
        double spiralStart,
        int spiralDirection
    ) {

        private static final double TWO_PI = Math.PI * 2.0D;
        private static final double MAX_EDGE_SCALE = 1.12D;

        int extentX() {
            final double cos = Math.cos(this.rotation);
            final double sin = Math.sin(this.rotation);
            return Mth.ceil(Math.sqrt(this.radiusX * this.radiusX * cos * cos + this.radiusZ * this.radiusZ * sin * sin) * MAX_EDGE_SCALE) + 1;
        }

        int extentZ() {
            final double cos = Math.cos(this.rotation);
            final double sin = Math.sin(this.rotation);
            return Mth.ceil(Math.sqrt(this.radiusX * this.radiusX * sin * sin + this.radiusZ * this.radiusZ * cos * cos) * MAX_EDGE_SCALE) + 1;
        }

        double normalizedDistance(int dx, int dz) {
            final double localX = localX(dx, dz);
            final double localZ = localZ(dx, dz);
            final double normalizedX = localX / this.radiusX;
            final double normalizedZ = localZ / this.radiusZ;
            final double radius = Math.sqrt(normalizedX * normalizedX + normalizedZ * normalizedZ);
            final double angle = Math.atan2(normalizedZ, normalizedX);
            final double edgeScale = 1.0D + Math.sin(angle * 3.0D + this.edgePhaseA) * 0.060D + Math.sin(angle * 5.0D + this.edgePhaseB) * 0.035D
                + Math.sin(localX * 0.31D + localZ * 0.23D + this.edgePhaseA) * 0.018D;
            return radius / edgeScale;
        }

        int depthAt(int dx, int dz) {
            final int benchDepth = benchDepthAt(dx, dz);
            if (benchDepth <= 0) {
                return 0;
            }

            return Math.min(benchDepth, rampDepthAt(dx, dz));
        }

        int floorAt(int localSurface, int centerSurface, int dx, int dz) {
            final int localDepth = depthAt(dx, dz);
            if (localDepth <= 0) {
                return localSurface;
            }

            final double blendProgress = Mth.clamp((1.0D - normalizedDistance(dx, dz)) / 0.50D, 0.0D, 1.0D);
            final double terrainBlend = smoothstep(blendProgress);
            final int blendedSurface = Mth.floor(Mth.lerp(terrainBlend, localSurface, centerSurface) + 0.5D);
            return Math.min(localSurface - 1, blendedSurface - localDepth);
        }

        boolean isRamp(int dx, int dz) {
            final int benchDepth = benchDepthAt(dx, dz);
            return benchDepth > 0 && rampDepthAt(dx, dz) < benchDepth;
        }

        boolean touchesExcavation(int dx, int dz) {
            return depthAt(dx + 1, dz) > 0 || depthAt(dx - 1, dz) > 0 || depthAt(dx, dz + 1) > 0 || depthAt(dx, dz - 1) > 0;
        }

        private int benchDepthAt(int dx, int dz) {
            final double distance = normalizedDistance(dx, dz);
            if (distance >= 1.0D) {
                return 0;
            }

            final double descent = Mth.clamp((1 - distance) / 0.75D, 0.0D, 1.0D);
            final int smoothDepth = Math.max(1, Mth.floor(this.depth * Math.pow(descent, 0.78D)));
            if (descent >= 0.995D) {
                return this.depth;
            }
            if (smoothDepth <= 2) {
                return smoothDepth;
            }

            return 2 + (smoothDepth - 2) / 3 * 3;
        }

        private int rampDepthAt(int dx, int dz) {
            final double localX = localX(dx, dz);
            final double localZ = localZ(dx, dz);
            final double normalizedX = localX / this.radiusX;
            final double normalizedZ = localZ / this.radiusZ;
            final double radius = Math.sqrt(normalizedX * normalizedX + normalizedZ * normalizedZ);
            final double angle = Math.atan2(normalizedZ, normalizedX);
            final double relativeAngle = positiveAngle(this.spiralDirection * (angle - this.spiralStart));
            final double radiusScale = Math.sqrt(this.radiusX * this.radiusX * Math.cos(angle) * Math.cos(angle) + this.radiusZ * this.radiusZ * Math.sin(angle) * Math.sin(angle));

            double closestDistance = Double.MAX_VALUE;
            double closestProgress = 0;
            final int passes = Mth.ceil(SPIRAL_TURNS);
            for (int pass = 0; pass <= passes; pass++) {
                final double travelledAngle = relativeAngle + pass * TWO_PI;
                final double progress = travelledAngle / (SPIRAL_TURNS * TWO_PI);
                if (progress > 1) {
                    continue;
                }

                final double pathRadius = Mth.lerp(progress, 0.88, 0.2);
                final double distanceToPath = Math.abs(radius - pathRadius) * radiusScale;
                if (distanceToPath < closestDistance) {
                    closestDistance = distanceToPath;
                    closestProgress = progress;
                }

            }

            if (closestDistance > 1.65) {
                return Integer.MAX_VALUE;
            }

            return Mth.clamp(1 + Mth.floor(closestProgress * (this.depth - 1)), 1, this.depth);
        }

        private double localX(int dx, int dz) {
            return dx * Math.cos(this.rotation) + dz * Math.sin(this.rotation);
        }

        private double localZ(int dx, int dz) {
            return -dx * Math.sin(this.rotation) + dz * Math.cos(this.rotation);
        }

        private static double positiveAngle(double angle) {
            final double wrapped = angle % TWO_PI;
            return wrapped < 0 ? wrapped + TWO_PI : wrapped;
        }

    }

}