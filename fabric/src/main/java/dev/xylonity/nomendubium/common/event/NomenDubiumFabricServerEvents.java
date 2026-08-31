package dev.xylonity.nomendubium.common.event;

import dev.xylonity.nomendubium.common.entity.ChimeraEntity;
import dev.xylonity.nomendubium.common.entity.TreeOfLifeEntity;
import dev.xylonity.nomendubium.common.item.SapOfLifeItem;
import dev.xylonity.nomendubium.registry.NomenDubiumEntities;
import dev.xylonity.nomendubium.registry.NomenDubiumBlocks;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;

public final class NomenDubiumFabricServerEvents {

    public static void init() {
        FabricDefaultAttributeRegistry.register(NomenDubiumEntities.CHIMERA.get(), ChimeraEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(NomenDubiumEntities.TREE_OF_LIFE.get(), TreeOfLifeEntity.createAttributes());
        ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, baseDamageTaken, damageTaken, blocked) -> SapOfLifeItem.stopRegenerationAfterDamage(entity, damageTaken));
        registerCoaldenWood();
    }

    private static void registerCoaldenWood() {
        StrippableBlockRegistry.registerCopyState(NomenDubiumBlocks.COALDEN_LOG.get(), NomenDubiumBlocks.COALDEN_STRIPPED_LOG.get());
        StrippableBlockRegistry.registerCopyState(NomenDubiumBlocks.COALDEN_WOOD.get(), NomenDubiumBlocks.COALDEN_STRIPPED_WOOD.get());

        final FlammableBlockRegistry flammables = FlammableBlockRegistry.getDefaultInstance();
        flammables.add(NomenDubiumBlocks.COALDEN_LOG.get(), 1, 1);
        flammables.add(NomenDubiumBlocks.COALDEN_WOOD.get(), 1, 1);
        flammables.add(NomenDubiumBlocks.COALDEN_STRIPPED_LOG.get(), 1, 1);
        flammables.add(NomenDubiumBlocks.COALDEN_STRIPPED_WOOD.get(), 1, 1);
        flammables.add(NomenDubiumBlocks.COALDEN_PLANKS.get(), 1, 4);
        flammables.add(NomenDubiumBlocks.COALDEN_STAIRS.get(), 1, 4);
        flammables.add(NomenDubiumBlocks.COALDEN_SLAB.get(), 1, 4);
        flammables.add(NomenDubiumBlocks.COALDEN_FENCE.get(), 1, 4);
        flammables.add(NomenDubiumBlocks.COALDEN_FENCE_GATE.get(), 1, 4);
        flammables.add(NomenDubiumBlocks.COALDEN_DOOR.get(), 1, 4);
        flammables.add(NomenDubiumBlocks.COALDEN_TRAPDOOR.get(), 1, 4);
        flammables.add(NomenDubiumBlocks.COALDEN_PRESSURE_PLATE.get(), 1, 4);
        flammables.add(NomenDubiumBlocks.COALDEN_BUTTON.get(), 1, 4);
        flammables.add(NomenDubiumBlocks.COALDEN_SIGN.get(), 1, 4);
        flammables.add(NomenDubiumBlocks.COALDEN_WALL_SIGN.get(), 1, 4);
    }

}
