package dev.xylonity.nomendubium.common.event;

import dev.xylonity.knightlib.api.event.RegisterEvent;
import dev.xylonity.knightlib.api.event.impl.server.EntityAttributeRegistrationEvent;
import dev.xylonity.knightlib.api.event.impl.server.LivingHurtEvent;
import dev.xylonity.nomendubium.common.entity.ChimeraEntity;
import dev.xylonity.nomendubium.common.entity.TreeOfLifeEntity;
import dev.xylonity.nomendubium.common.item.SapOfLifeItem;
import dev.xylonity.nomendubium.registry.NomenDubiumEntities;

public final class NomenDubiumServerEvents {

    @RegisterEvent
    public static void registerEntityAttributes(EntityAttributeRegistrationEvent event) {
        event.register(NomenDubiumEntities.CHIMERA, ChimeraEntity::createAttributes);
        event.register(NomenDubiumEntities.TREE_OF_LIFE, TreeOfLifeEntity::createAttributes);
    }

    @RegisterEvent
    public static void afterLivingDamage(LivingHurtEvent event) {
        SapOfLifeItem.stopRegenerationAfterDamage(event.getEntity(), event.getAmount());
    }

}