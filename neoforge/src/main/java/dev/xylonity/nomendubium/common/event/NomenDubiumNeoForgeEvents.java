package dev.xylonity.nomendubium.common.event;

import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.common.entity.ChimeraEntity;
import dev.xylonity.nomendubium.common.entity.TreeOfLifeEntity;
import dev.xylonity.nomendubium.common.item.SapOfLifeItem;
import dev.xylonity.nomendubium.registry.NomenDubiumEntities;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber(modid = NomenDubium.MOD_ID)
public final class NomenDubiumNeoForgeEvents {

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(NomenDubiumEntities.CHIMERA.get(), ChimeraEntity.createAttributes().build());
        event.put(NomenDubiumEntities.TREE_OF_LIFE.get(), TreeOfLifeEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void afterLivingDamage(LivingDamageEvent.Post event) {
        SapOfLifeItem.stopRegenerationAfterDamage(event.getEntity(), event.getHealthDamage());
    }

}
