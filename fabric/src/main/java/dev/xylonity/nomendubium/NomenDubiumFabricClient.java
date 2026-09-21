package dev.xylonity.nomendubium;

import dev.xylonity.knightlib.api.event.KnightLibEvents;
import dev.xylonity.nomendubium.client.event.NomenDubiumClientEvents;
import dev.xylonity.nomendubium.client.event.NomenDubiumFabricClientEvents;
import dev.xylonity.nomendubium.common.entity.skeleton.SkeletonPartType;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraPaletteVariant;
import dev.xylonity.nomendubium.common.item.FruitOfLifeItem;
import dev.xylonity.nomendubium.common.item.fossil.FossilItem;
import dev.xylonity.nomendubium.registry.NomenDubiumItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;

public class NomenDubiumFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FabricModelPredicateProviderRegistry.register(NomenDubiumItems.FOSSIL.get(), NomenDubium.of("fossil_part"),
            (stack, level, entity, seed) -> {
                final SkeletonPartType part = SkeletonPartType.byFossilPart(FossilItem.getPart(stack));
                return part == null ? 0.0F : part.modelPredicateValue();
            });
        FabricModelPredicateProviderRegistry.register(NomenDubiumItems.FRUIT_OF_LIFE.get(), NomenDubium.of("chimera_palette"),
            (stack, level, entity, seed) -> {
                final ChimeraPaletteVariant palette = FruitOfLifeItem.getPalette(stack);
                return palette == null ? 0.0F : palette.modelPredicateValue();
            });

        KnightLibEvents.CLIENT.register(NomenDubiumClientEvents.class);
        NomenDubiumFabricClientEvents.init();
    }

}
