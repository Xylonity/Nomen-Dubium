package dev.xylonity.nomendubium.client.event;

import dev.xylonity.nomendubium.client.util.ChimeraModelLayers;
import dev.xylonity.nomendubium.client.util.ArrowModelLayers;
import dev.xylonity.nomendubium.client.util.SkeletonPartModelLayers;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;

public final class NomenDubiumFabricClientEvents {

    public static void init() {
        ChimeraModelLayers.entries().forEach(entry -> EntityModelLayerRegistry.registerModelLayer(entry.location(), entry.definition()::get));
        SkeletonPartModelLayers.entries().forEach(entry -> EntityModelLayerRegistry.registerModelLayer(entry.location(), entry.definition()::get));
        ArrowModelLayers.entries().forEach(entry -> EntityModelLayerRegistry.registerModelLayer(entry.location(), entry.definition()::get));
    }

}
