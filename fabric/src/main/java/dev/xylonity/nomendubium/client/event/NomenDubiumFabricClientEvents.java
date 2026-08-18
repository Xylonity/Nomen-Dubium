package dev.xylonity.nomendubium.client.event;

import dev.xylonity.nomendubium.client.util.ChimeraModelLayers;
import dev.xylonity.nomendubium.client.entity.render.chimera.ChimeraRenderer;
import dev.xylonity.nomendubium.client.screen.GeologistTableScreen;
import dev.xylonity.nomendubium.registry.NomenDubiumEntities;
import dev.xylonity.nomendubium.registry.NomenDubiumMenus;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;

public final class NomenDubiumFabricClientEvents {

    public static void init() {
        ChimeraModelLayers.entries().forEach(entry ->
            ModelLayerRegistry.registerModelLayer(entry.location(), entry.definition()::get)
        );

        EntityRenderers.register(NomenDubiumEntities.CHIMERA.get(), ChimeraRenderer::new);
        MenuScreens.register(NomenDubiumMenus.GEOLOGIST_TABLE.get(), GeologistTableScreen::new);
    }

}
