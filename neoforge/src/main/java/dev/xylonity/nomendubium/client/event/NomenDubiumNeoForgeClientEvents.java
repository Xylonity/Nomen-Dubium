package dev.xylonity.nomendubium.client.event;

import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.client.model.ModularDinoModelLayers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = NomenDubium.MOD_ID, value = Dist.CLIENT)
public final class NomenDubiumNeoForgeClientEvents {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        ModularDinoModelLayers.entries().forEach(entry ->
            event.registerLayerDefinition(entry.location(), entry.definition())
        );

    }


}