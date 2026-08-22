package dev.xylonity.nomendubium.client.event;

import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.client.screen.PaleontologyTableScreen;
import dev.xylonity.nomendubium.client.util.ChimeraModelLayers;
import dev.xylonity.nomendubium.client.entity.render.chimera.ChimeraRenderer;
import dev.xylonity.nomendubium.client.projectile.renderer.SkeletonPartRenderer;
import dev.xylonity.nomendubium.client.projectile.renderer.FossilisedMawRenderer;
import dev.xylonity.nomendubium.client.projectile.renderer.HuntersArrowRenderer;
import dev.xylonity.nomendubium.client.projectile.renderer.PrehistoricMawRenderer;
import dev.xylonity.nomendubium.registry.NomenDubiumEntities;
import dev.xylonity.nomendubium.registry.NomenDubiumMenus;
import dev.xylonity.nomendubium.client.util.SkeletonPartModelLayers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = NomenDubium.MOD_ID, value = Dist.CLIENT)
public final class NomenDubiumNeoForgeClientEvents {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        ChimeraModelLayers.entries().forEach(entry -> event.registerLayerDefinition(entry.location(), entry.definition()));
        SkeletonPartModelLayers.entries().forEach(entry -> event.registerLayerDefinition(entry.location(), entry.definition()));
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(NomenDubiumEntities.CHIMERA.get(), ChimeraRenderer::new);
        event.registerEntityRenderer(NomenDubiumEntities.SKELETON_PART.get(), SkeletonPartRenderer::new);
        event.registerEntityRenderer(NomenDubiumEntities.HUNTERS_ARROW.get(), HuntersArrowRenderer::new);
        event.registerEntityRenderer(NomenDubiumEntities.PREHISTORIC_MAW.get(), PrehistoricMawRenderer::new);
        event.registerEntityRenderer(NomenDubiumEntities.FOSSILISED_MAW.get(), FossilisedMawRenderer::new);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(NomenDubiumMenus.PALEONTOLOGY_TABLE.get(), PaleontologyTableScreen::new);
    }

}