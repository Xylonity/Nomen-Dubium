package dev.xylonity.nomendubium.client.event;

import dev.xylonity.nomendubium.client.model.ModularDinoModelLayers;
import dev.xylonity.nomendubium.client.render.ModularCreatureRenderer;
import dev.xylonity.nomendubium.client.screen.FossilTableScreen;
import dev.xylonity.nomendubium.registry.NomenDubiumEntities;
import dev.xylonity.nomendubium.registry.NomenDubiumMenus;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;

public final class NomenDubiumFabricClientEvents {

    public static void init() {
        ModularDinoModelLayers.entries().forEach(entry ->
            ModelLayerRegistry.registerModelLayer(entry.location(), entry.definition()::get)
        );
        
    }

}
