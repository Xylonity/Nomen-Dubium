package dev.xylonity.nomendubium.client.event;

import dev.xylonity.nomendubium.client.util.ChimeraModelLayers;
import dev.xylonity.nomendubium.client.entity.render.chimera.ChimeraRenderer;
import dev.xylonity.nomendubium.client.projectile.renderer.SkeletonPartRenderer;
import dev.xylonity.nomendubium.client.projectile.renderer.FossilisedMawRenderer;
import dev.xylonity.nomendubium.client.projectile.renderer.HuntersArrowRenderer;
import dev.xylonity.nomendubium.client.projectile.renderer.PrehistoricMawRenderer;
import dev.xylonity.nomendubium.client.screen.PaleontologyTableScreenReal;
import dev.xylonity.nomendubium.client.util.SkeletonPartModelLayers;
import dev.xylonity.nomendubium.registry.NomenDubiumEntities;
import dev.xylonity.nomendubium.registry.NomenDubiumMenus;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;

public final class NomenDubiumFabricClientEvents {

    public static void init() {
        ChimeraModelLayers.entries().forEach(entry -> ModelLayerRegistry.registerModelLayer(entry.location(), entry.definition()::get));
        SkeletonPartModelLayers.entries().forEach(entry -> ModelLayerRegistry.registerModelLayer(entry.location(), entry.definition()::get));

        EntityRenderers.register(NomenDubiumEntities.CHIMERA.get(), ChimeraRenderer::new);
        EntityRenderers.register(NomenDubiumEntities.SKELETON_PART.get(), SkeletonPartRenderer::new);
        EntityRenderers.register(NomenDubiumEntities.HUNTERS_ARROW.get(), HuntersArrowRenderer::new);
        EntityRenderers.register(NomenDubiumEntities.PREHISTORIC_MAW.get(), PrehistoricMawRenderer::new);
        EntityRenderers.register(NomenDubiumEntities.FOSSILISED_MAW.get(), FossilisedMawRenderer::new);
        MenuScreens.register(NomenDubiumMenus.PALEONTOLOGY_TABLE.get(), PaleontologyTableScreenReal::new);
    }

}