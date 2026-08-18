package dev.xylonity.nomendubium.common.event;

import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.common.entity.ChimeraEntity;
import dev.xylonity.nomendubium.registry.NomenDubiumEntities;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = NomenDubium.MOD_ID)
public final class NomenDubiumNeoForgeEvents {

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(NomenDubiumEntities.CHIMERA.get(), ChimeraEntity.createAttributes().build());
    }

}
