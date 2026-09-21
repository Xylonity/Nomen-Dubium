package dev.xylonity.nomendubium.registry;

import dev.xylonity.knightlib.api.registrar.ResourceDispatcher;
import dev.xylonity.knightlib.api.registrar.ResourceEntry;
import dev.xylonity.knightlib.api.registrar.ResourceRegistry;
import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.common.entity.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.List;

public final class NomenDubiumEntities {

    public static final ResourceRegistry<EntityType<?>> ENTITIES = ResourceDispatcher.create(BuiltInRegistries.ENTITY_TYPE, NomenDubium.MOD_ID);

    public static final ResourceEntry<EntityType<ChimeraEntity>> CHIMERA = ENTITIES.registerEntity("chimera", ChimeraEntity::new, MobCategory.CREATURE, 3.0F, 3.0F);
    public static final ResourceEntry<EntityType<TreeOfLifeEntity>> TREE_OF_LIFE = ENTITIES.registerEntity("tree_of_life", TreeOfLifeEntity::new, MobCategory.MISC, 3.625F, 8.0F, List.of(builder -> builder.clientTrackingRange(12)));
    public static final ResourceEntry<EntityType<SkeletonPartEntity>> SKELETON_PART = ENTITIES.registerEntity("skeleton_part", SkeletonPartEntity::new, MobCategory.MISC, 1.75F, 1.75F, List.of(builder -> builder.clientTrackingRange(10).updateInterval(20)));
    public static final ResourceEntry<EntityType<HuntersArrowEntity>> HUNTERS_ARROW = ENTITIES.registerEntity("hunters_arrow", HuntersArrowEntity::new, MobCategory.MISC, 0.5F, 0.5F, List.of(builder -> builder.clientTrackingRange(4).updateInterval(20)));
    public static final ResourceEntry<EntityType<PrimitiveArrowEntity>> PRIMITIVE_ARROW = ENTITIES.registerEntity("primitive_arrow", PrimitiveArrowEntity::new, MobCategory.MISC, 0.5F, 0.5F, List.of(builder -> builder.clientTrackingRange(4).updateInterval(20)));
    public static final ResourceEntry<EntityType<PrehistoricMawProjectileEntity>> PREHISTORIC_MAW = ENTITIES.registerEntity("prehistoric_maw", PrehistoricMawProjectileEntity::new, MobCategory.MISC, 0.65F, 0.25F, List.of(builder -> builder.clientTrackingRange(8).updateInterval(10)));
    public static final ResourceEntry<EntityType<FossilisedMawProjectileEntity>> FOSSILISED_MAW = ENTITIES.registerEntity("fossilised_maw", FossilisedMawProjectileEntity::new, MobCategory.MISC, 0.65F, 0.25F, List.of(builder -> builder.clientTrackingRange(8).updateInterval(10)));

}