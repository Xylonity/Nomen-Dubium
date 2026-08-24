package dev.xylonity.nomendubium.client.util;

import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.client.projectile.model.HuntersArrowModel;
import dev.xylonity.nomendubium.client.projectile.model.PrimitiveArrowModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

import java.util.List;
import java.util.function.Supplier;

/// Doing this to avoid duplicated code on both loaders
/// TODO: refactor into knightlib's event system
public final class ArrowModelLayers {

    public static final ModelLayerLocation HUNTERS_ARROW = layer("projectile/hunters_arrow");
    public static final ModelLayerLocation PRIMITIVE_ARROW = layer("projectile/primitive_arrow");

    private static final List<Entry> ENTRIES = List.of(
        new Entry(HUNTERS_ARROW, HuntersArrowModel::createLayer),
        new Entry(PRIMITIVE_ARROW, PrimitiveArrowModel::createLayer)
    );

    public static List<Entry> entries() {
        return ENTRIES;
    }

    private static ModelLayerLocation layer(String name) {
        return new ModelLayerLocation(NomenDubium.of(name), "main");
    }

    public record Entry(
            ModelLayerLocation location,
            Supplier<LayerDefinition> definition
    ) {
        ;;
    }

}
