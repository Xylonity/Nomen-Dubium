package dev.xylonity.nomendubium.client.event;

import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.client.util.ChimeraModelLayers;
import dev.xylonity.nomendubium.client.util.ArrowModelLayers;
import dev.xylonity.nomendubium.common.entity.skeleton.SkeletonPartType;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraPaletteVariant;
import dev.xylonity.nomendubium.common.item.FruitOfLifeItem;
import dev.xylonity.nomendubium.common.item.fossil.FossilItem;
import dev.xylonity.nomendubium.client.util.SkeletonPartModelLayers;
import dev.xylonity.nomendubium.registry.NomenDubiumItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = NomenDubium.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class NomenDubiumForgeClientEvents {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(NomenDubiumForgeClientEvents::registerItemProperties);
    }

    private static void registerItemProperties() {
        ItemProperties.register(NomenDubiumItems.FOSSIL.get(), NomenDubium.of("fossil_part"),
            (stack, level, entity, seed) -> {
                final SkeletonPartType part = SkeletonPartType.byFossilPart(FossilItem.getPart(stack));
                return part == null ? 0.0F : part.modelPredicateValue();
            });

        ItemProperties.register(NomenDubiumItems.FRUIT_OF_LIFE.get(), NomenDubium.of("chimera_palette"),
            (stack, level, entity, seed) -> {
                final ChimeraPaletteVariant palette = FruitOfLifeItem.getPalette(stack);
                return palette == null ? 0.0F : palette.modelPredicateValue();
            });

    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        ChimeraModelLayers.entries().forEach(entry -> event.registerLayerDefinition(entry.location(), entry.definition()));
        SkeletonPartModelLayers.entries().forEach(entry -> event.registerLayerDefinition(entry.location(), entry.definition()));
        ArrowModelLayers.entries().forEach(entry -> event.registerLayerDefinition(entry.location(), entry.definition()));
    }

}
