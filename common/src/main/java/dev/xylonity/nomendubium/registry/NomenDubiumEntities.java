package dev.xylonity.nomendubium.registry;

import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.common.entity.ChimeraEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public final class NomenDubiumEntities {

    public static void init() {
        ;;
    }

    public static final Supplier<EntityType<ChimeraEntity>> CHIMERA = NomenDubium.PLATFORM.registerEntity("chimera",
            key -> EntityType.Builder.of(ChimeraEntity::new, MobCategory.CREATURE).sized(3.0F, 3.0F).build(key));

}
