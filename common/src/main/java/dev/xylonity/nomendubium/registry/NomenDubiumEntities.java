package dev.xylonity.nomendubium.registry;

import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.common.entity.ChimeraEntity;
import dev.xylonity.nomendubium.common.entity.FossilisedMawProjectileEntity;
import dev.xylonity.nomendubium.common.entity.HuntersArrowEntity;
import dev.xylonity.nomendubium.common.entity.PrehistoricMawProjectileEntity;
import dev.xylonity.nomendubium.common.entity.PrimitiveArrowEntity;
import dev.xylonity.nomendubium.common.entity.SkeletonPartEntity;
import dev.xylonity.nomendubium.common.entity.TreeOfLifeEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public final class NomenDubiumEntities {

    public static void init() {
        ;;
    }

    public static final Supplier<EntityType<ChimeraEntity>> CHIMERA = NomenDubium.PLATFORM.registerEntity("chimera",
            key -> EntityType.Builder.of(ChimeraEntity::new, MobCategory.CREATURE).sized(3.0F, 3.0F).build(key));

    public static final Supplier<EntityType<TreeOfLifeEntity>> TREE_OF_LIFE = NomenDubium.PLATFORM.registerEntity("tree_of_life",
            key -> EntityType.Builder.of(TreeOfLifeEntity::new, MobCategory.MISC).sized(3.625F, 8.0F).clientTrackingRange(12).build(key));

    public static final Supplier<EntityType<SkeletonPartEntity>> SKELETON_PART = NomenDubium.PLATFORM.registerEntity("skeleton_part",
            key -> EntityType.Builder.<SkeletonPartEntity>of(SkeletonPartEntity::new, MobCategory.MISC).sized(1.75F, 1.75F).clientTrackingRange(10).updateInterval(20).build(key));

    public static final Supplier<EntityType<HuntersArrowEntity>> HUNTERS_ARROW = NomenDubium.PLATFORM.registerEntity("hunters_arrow",
            key -> EntityType.Builder.<HuntersArrowEntity>of(HuntersArrowEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build(key));
    public static final Supplier<EntityType<PrimitiveArrowEntity>> PRIMITIVE_ARROW = NomenDubium.PLATFORM.registerEntity("primitive_arrow",
            key -> EntityType.Builder.<PrimitiveArrowEntity>of(PrimitiveArrowEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build(key));

    public static final Supplier<EntityType<PrehistoricMawProjectileEntity>> PREHISTORIC_MAW = NomenDubium.PLATFORM.registerEntity("prehistoric_maw",
            key -> EntityType.Builder.<PrehistoricMawProjectileEntity>of(PrehistoricMawProjectileEntity::new, MobCategory.MISC).sized(0.65F, 0.25F).clientTrackingRange(8).updateInterval(10).build(key));
    public static final Supplier<EntityType<FossilisedMawProjectileEntity>> FOSSILISED_MAW = NomenDubium.PLATFORM.registerEntity("fossilised_maw",
            key -> EntityType.Builder.<FossilisedMawProjectileEntity>of(FossilisedMawProjectileEntity::new, MobCategory.MISC).sized(0.65F, 0.25F).clientTrackingRange(8).updateInterval(10).build(key));

}
