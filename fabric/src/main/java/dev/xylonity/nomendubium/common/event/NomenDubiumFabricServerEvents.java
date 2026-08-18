package dev.xylonity.nomendubium.common.event;

import dev.xylonity.nomendubium.common.entity.ChimeraEntity;
import dev.xylonity.nomendubium.registry.NomenDubiumEntities;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public final class NomenDubiumFabricServerEvents {

    public static void init() {
        FabricDefaultAttributeRegistry.register(NomenDubiumEntities.CHIMERA.get(), ChimeraEntity.createAttributes());
    }

}
