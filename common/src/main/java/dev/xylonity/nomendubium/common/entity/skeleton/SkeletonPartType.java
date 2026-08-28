package dev.xylonity.nomendubium.common.entity.skeleton;

import dev.xylonity.nomendubium.common.entity.variant.ChimeraBackVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraBodyVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraHeadVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraPartCategory;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraPartVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraTailVariant;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

/// Defines individual skeleton parts, with specific connection sections (the model and pivots differ between the chimera's actual body parts and their skeleton variant) and
/// custom bbox portions to easily swap body parts on the skeleton entity
public enum SkeletonPartType {
    HULKING_BODY(ChimeraBodyVariant.HULKING, "hulking_skeleton", connections(-44.5F, -23F, -44.5F, 24F, -48F, -3F), bounds(-1.3125D, 0.001D, -1.5063D, 1.3125D, 3.0635D, 1.4375D)),
    SHELLED_BODY(ChimeraBodyVariant.SHELLED, "shelled_skeleton", connections(-9F, -25F, -8F, 22F, -39F, -2F), bounds(-1.3125D, 0.001D, -1.5D, 1.3125D, 2.4385D, 1.875D)),
    AVIAN_BODY(ChimeraBodyVariant.AVIAN, "avian_skeleton", connections(-40.5F, -18F, -40.5F, 18F, -44F, -4F), bounds(-0.8813D, 0.001D, -1.25D, 0.8813D, 2.751D, 1.125D)),
    LANKY_BODY(ChimeraBodyVariant.LANKY, "lanky_skeleton", connections(-59.5F, -18F, -59.5F, 29F, -67F, 2F), bounds(-0.6938D, 0.001D, -2.0625D, 0.6938D, 4.4385D, 1.4375D)),
    PUFFY_BODY(ChimeraBodyVariant.PUFFY, "puffy_skeleton", connections(-31.5F, -20F, -31.5F, 20F, -35F, 0F), bounds(-0.6875D, 0.001D, -1.25D, 0.6875D, 2.1885D, 1.25D)),

    CRUNCHING_HEAD(ChimeraHeadVariant.CRUNCHING, "crunching_skull", null, bounds(-0.5625D, -1.0938D, 0D, 0.5625D, 0.5313D, 3.1875D)),
    SHIELDED_HEAD(ChimeraHeadVariant.SHIELDED, "shielded_skull", null, bounds(-0.875D, -1.0938D, 0D, 0.875D, 0.7813D, 1.6875D)),
    SNARLED_HEAD(ChimeraHeadVariant.SNARLED, "snarled_skull", null, bounds(-0.5313D, -0.1875D, 0D, 0.5313D, 3D, 1.625D)),
    BEAKED_HEAD(ChimeraHeadVariant.BEAKED, "beaked_skull", null, bounds(-0.4688D, -0.2188D, 0D, 0.4688D, 1.4125D, 2.3125D)),
    SNORTING_HEAD(ChimeraHeadVariant.SNORTING, "snorting_skull", null, bounds(-0.4063D, -0.4688D, 0D, 0.4063D, 0.4688D, 1.25D)),

    SPIKED_TAIL(ChimeraTailVariant.SPIKED, "spiked_tail_fossil", null, bounds(-1.0625D, -0.4375D, -3.375D, 1.0625D, 0.3125D, 0D)),
    STUBBY_TAIL(ChimeraTailVariant.STUBBY, "stubby_tail_fossil", null, bounds(-0.25D, -0.3125D, -1.625D, 0.25D, 0.3125D, 0D)),
    CLUBBED_TAIL(ChimeraTailVariant.CLUBBED, "clubbed_tail_fossil", null, bounds(-0.375D, -0.375D, -2.625D, 0.375D, 0.375D, 0D)),
    FAN_TAIL(ChimeraTailVariant.FAN, "fan_tail_fossil", null, bounds(-0.875D, -0.25D, -1.9375D, 0.875D, 0.25D, 0D)),
    SPEARED_TAIL(ChimeraTailVariant.SPEARED, "speared_tail_fossil", null, bounds(-0.25D, -0.2790D, -1.6405D, 0.25D, 2.1740D, 0.3170D)),

    BONEY_PLATES_BACK(ChimeraBackVariant.BONEY_PLATES, "boney_plates_fossil", null, bounds(0D, 0D, -1.3125D, 0D, 0.9375D, 1.25D)),
    DORSAL_SCALES_BACK(ChimeraBackVariant.DORSAL_SCALES, "dorsal_scales_fossil", null, bounds(0D, 0D, -0.3125D, 0D, 2D, 0.8125D)),
    SPIKES_BACK(ChimeraBackVariant.SPIKES, "spikes_fossil", null, bounds(-2.5D, -0.25D, -0.0625D, 2.5D, 1.4375D, 1D)),
    SPINE_SAIL_BACK(ChimeraBackVariant.SPINE_SAIL, "spine_sail_fossil", null, bounds(0D, 0D, -1D, 0D, 1D, 1D)),
    THORNS_BACK(ChimeraBackVariant.THORNS, "thorns_fossil", null, bounds(-0.3125D, 0D, -1.1875D, 0.3125D, 0.4375D, 1.125D));

    private static final SkeletonPartType[] VALUES = values();
    private static final Map<String, SkeletonPartType> BY_FOSSIL_PART = createPartLookup();

    private final ChimeraPartVariant variant;
    private final String texture;
    private final Connections connections;
    private final Bounds bounds;

    SkeletonPartType(ChimeraPartVariant variant, String texture, Connections connections, Bounds bounds) {
        this.variant = variant;
        this.texture = texture;
        this.connections = connections;
        this.bounds = bounds;
    }

    public static SkeletonPartType index(int index) {
        return VALUES[Math.floorMod(index, VALUES.length)];
    }

    public static SkeletonPartType byFossilPart(String fossilPart) {
        return fossilPart == null ? null : BY_FOSSIL_PART.get(fossilPart);
    }

    public String fossilPart() {
        return variant.fossilPart();
    }

    public ChimeraPartVariant variant() {
        return variant;
    }

    public ChimeraPartCategory category() {
        return variant.category();
    }

    public String texture() {
        return texture;
    }

    public int index() {
        return ordinal();
    }

    public boolean isBody() {
        return category() == ChimeraPartCategory.BODY;
    }

    public Vec3 attachmentOffset(ChimeraPartCategory attachment, float yaw) {
        if (connections == null) {
            return Vec3.ZERO;
        }

        final Connection connection = connections.get(attachment);
        if (connection == null) {
            return Vec3.ZERO;
        }

        // horizontal distance based on the body yaw
        final double angle = Math.toRadians(yaw);
        final double distance = -connection.z / 16D;
        return new Vec3(-Math.sin(angle) * distance, -connection.y / 16D, Math.cos(angle) * distance);
    }

    public AABB interactionBox(Vec3 position, float yaw) {
        return bounds.at(position, yaw, isBody());
    }

    public EntityDimensions entityDimensions() {
        return bounds.dimensions(isBody());
    }

    private static Map<String, SkeletonPartType> createPartLookup() {
        final Map<String, SkeletonPartType> lookup = new HashMap<>();
        for (final SkeletonPartType type : VALUES) {
            lookup.put(type.fossilPart(), type);
        }

        return Map.copyOf(lookup);
    }

    private static Connections connections(float headY, float headZ, float tailY, float tailZ, float backY, float backZ) {
        return new Connections(new Connection(headY, headZ), new Connection(tailY, tailZ), new Connection(backY, backZ));
    }

    private static Bounds bounds(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return new Bounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    private record Connections(Connection head, Connection tail, Connection back) {
        private Connection get(ChimeraPartCategory category) {
            return switch (category) {
                case HEAD -> head;
                case TAIL -> tail;
                case BACK -> back;
                case BODY -> null;
            };

        }

    }

    private record Connection(
            float y,
            float z
    ) {
        ;;
    }

    private record Bounds(
            double minX,
            double minY,
            double minZ,
            double maxX,
            double maxY,
            double maxZ
    ) {

        private static final double PADDING = 1 / 16D;

        private AABB at(Vec3 position, float yaw, boolean body) {
            double paddedMinX = minX - PADDING;
            double paddedMaxX = maxX + PADDING;
            if (paddedMaxX - paddedMinX < 0.25D) {
                final double centerX = (paddedMinX + paddedMaxX) * 0.5D;
                paddedMinX = centerX - 0.25D * 0.5D;
                paddedMaxX = centerX + 0.25D * 0.5D;
            }

            final double paddedMinZ = minZ - PADDING;
            final double paddedMaxZ = maxZ + PADDING;

            // Rotating all horizontal corners and using their aabbs so the whole model stays selectable at any yaw
            final double radians = Math.toRadians(yaw);
            final double sin = Math.sin(radians);
            final double cos = Math.cos(radians);
            double rotatedMinX = Double.POSITIVE_INFINITY;
            double rotatedMinZ = Double.POSITIVE_INFINITY;
            double rotatedMaxX = Double.NEGATIVE_INFINITY;
            double rotatedMaxZ = Double.NEGATIVE_INFINITY;

            for (final double x : new double[] {paddedMinX, paddedMaxX}) {
                for (final double z : new double[] {paddedMinZ, paddedMaxZ}) {
                    final double rotatedX = x * cos - z * sin;
                    final double rotatedZ = x * sin + z * cos;
                    rotatedMinX = Math.min(rotatedMinX, rotatedX);
                    rotatedMinZ = Math.min(rotatedMinZ, rotatedZ);
                    rotatedMaxX = Math.max(rotatedMaxX, rotatedX);
                    rotatedMaxZ = Math.max(rotatedMaxZ, rotatedZ);
                }

            }

            final double paddedMinY = body ? Math.max(0D, minY - PADDING) : minY - PADDING;
            return new AABB(
                position.x + rotatedMinX, position.y + paddedMinY, position.z + rotatedMinZ,
                position.x + rotatedMaxX, position.y + maxY + PADDING, position.z + rotatedMaxZ
            );

        }

        private EntityDimensions dimensions(boolean body) {
            final double width = Math.max(maxX - minX, maxZ - minZ) + PADDING * 2.0D;
            final double bottom = body ? Math.max(0.0D, minY - PADDING) : minY - PADDING;
            final double height = maxY + PADDING - bottom;
            return EntityDimensions.fixed((float) width, (float) height);
        }

    }

}
