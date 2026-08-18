package dev.xylonity.nomendubium.client.event;

import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.client.util.ChimeraModelLayers;
import dev.xylonity.nomendubium.client.entity.render.chimera.ChimeraRenderer;
import dev.xylonity.nomendubium.client.screen.GeologistTableScreen;
import dev.xylonity.nomendubium.registry.NomenDubiumEntities;
import dev.xylonity.nomendubium.registry.NomenDubiumMenus;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = NomenDubium.MOD_ID, value = Dist.CLIENT)
public final class NomenDubiumNeoForgeClientEvents {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        ChimeraModelLayers.entries().forEach(entry ->
            event.registerLayerDefinition(entry.location(), entry.definition())
        );
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(NomenDubiumEntities.CHIMERA.get(), ChimeraRenderer::new);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(NomenDubiumMenus.GEOLOGIST_TABLE.get(), GeologistTableScreen::new);
    }
}
