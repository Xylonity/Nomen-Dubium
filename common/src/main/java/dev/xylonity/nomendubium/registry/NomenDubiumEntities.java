package dev.xylonity.nomendubium.registry;

import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.entity.ModularDinoEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public final class NomenDubiumEntities {

    public static void init() {
        ;;
    }

    public static final Supplier<EntityType<ModularDinoEntity>> MODULAR_DINO = NomenDubium.PLATFORM.registerEntity("modular_dino",
            key -> EntityType.Builder.of(ModularDinoEntity::new, MobCategory.CREATURE).sized(3.0F, 3.0F).build(key));

}
