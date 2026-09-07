package dev.xylonity.nomendubium.common.event;

import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.common.entity.ChimeraEntity;
import dev.xylonity.nomendubium.common.entity.TreeOfLifeEntity;
import dev.xylonity.nomendubium.common.item.SapOfLifeItem;
import dev.xylonity.nomendubium.registry.NomenDubiumEntities;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = NomenDubium.MOD_ID)
public final class NomenDubiumForgeEvents {

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(NomenDubiumEntities.CHIMERA.get(), ChimeraEntity.createAttributes().build());
        event.put(NomenDubiumEntities.TREE_OF_LIFE.get(), TreeOfLifeEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void afterLivingDamage(LivingDamageEvent event) {
        SapOfLifeItem.stopRegenerationAfterDamage(event.getEntity(), event.getAmount());
    }

}
