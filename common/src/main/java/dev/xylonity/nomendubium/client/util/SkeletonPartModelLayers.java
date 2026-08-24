package dev.xylonity.nomendubium.client.util;

import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.client.entity.model.chimera.skeleton.back.BoneyPlatesFossilModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.skeleton.back.DorsalScalesFossilModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.skeleton.back.SpikesFossilModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.skeleton.back.SpineSailFossilModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.skeleton.back.ThornsFossilModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.skeleton.body.AvianSkeletonModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.skeleton.body.HulkingSkeletonModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.skeleton.body.LankySkeletonModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.skeleton.body.PuffySkeletonModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.skeleton.body.ShelledSkeletonModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.skeleton.head.BeakedSkullModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.skeleton.head.CrunchingSkullModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.skeleton.head.ShieldedSkullModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.skeleton.head.SnarledSkullModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.skeleton.head.SnortingSkullModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.skeleton.tail.ClubbedTailFossilModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.skeleton.tail.FanTailFossilModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.skeleton.tail.SpearedTailFossilModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.skeleton.tail.SpikedTailFossilModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.skeleton.tail.StubbyTailFossilModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

import java.util.List;
import java.util.function.Supplier;

/// Doing this to avoid duplicated code on both loaders
/// TODO: refactor into knightlib's event system
public final class SkeletonPartModelLayers {

    public static final ModelLayerLocation HULKING_BODY = layer("skeleton/hulking_body");
    public static final ModelLayerLocation SHELLED_BODY = layer("skeleton/shelled_body");
    public static final ModelLayerLocation AVIAN_BODY = layer("skeleton/avian_body");
    public static final ModelLayerLocation LANKY_BODY = layer("skeleton/lanky_body");
    public static final ModelLayerLocation PUFFY_BODY = layer("skeleton/puffy_body");
    public static final ModelLayerLocation CRUNCHING_HEAD = layer("skeleton/crunching_head");
    public static final ModelLayerLocation SHIELDED_HEAD = layer("skeleton/shielded_head");
    public static final ModelLayerLocation SNARLED_HEAD = layer("skeleton/snarled_head");
    public static final ModelLayerLocation BEAKED_HEAD = layer("skeleton/beaked_head");
    public static final ModelLayerLocation SNORTING_HEAD = layer("skeleton/snorting_head");
    public static final ModelLayerLocation SPIKED_TAIL = layer("skeleton/spiked_tail");
    public static final ModelLayerLocation STUBBY_TAIL = layer("skeleton/stubby_tail");
    public static final ModelLayerLocation CLUBBED_TAIL = layer("skeleton/clubbed_tail");
    public static final ModelLayerLocation FAN_TAIL = layer("skeleton/fan_tail");
    public static final ModelLayerLocation SPEARED_TAIL = layer("skeleton/speared_tail");
    public static final ModelLayerLocation BONEY_PLATES_BACK = layer("skeleton/boney_plates_back");
    public static final ModelLayerLocation DORSAL_SCALES_BACK = layer("skeleton/dorsal_scales_back");
    public static final ModelLayerLocation SPIKES_BACK = layer("skeleton/spikes_back");
    public static final ModelLayerLocation SPINE_SAIL_BACK = layer("skeleton/spine_sail_back");
    public static final ModelLayerLocation THORNS_BACK = layer("skeleton/thorns_back");

    private static final List<Entry> ENTRIES = List.of(
        new Entry(HULKING_BODY, HulkingSkeletonModel::createLayer),
        new Entry(SHELLED_BODY, ShelledSkeletonModel::createLayer),
        new Entry(AVIAN_BODY, AvianSkeletonModel::createLayer),
        new Entry(LANKY_BODY, LankySkeletonModel::createLayer),
        new Entry(PUFFY_BODY, PuffySkeletonModel::createLayer),
        new Entry(CRUNCHING_HEAD, CrunchingSkullModel::createLayer),
        new Entry(SHIELDED_HEAD, ShieldedSkullModel::createLayer),
        new Entry(SNARLED_HEAD, SnarledSkullModel::createLayer),
        new Entry(BEAKED_HEAD, BeakedSkullModel::createLayer),
        new Entry(SNORTING_HEAD, SnortingSkullModel::createLayer),
        new Entry(SPIKED_TAIL, SpikedTailFossilModel::createLayer),
        new Entry(STUBBY_TAIL, StubbyTailFossilModel::createLayer),
        new Entry(CLUBBED_TAIL, ClubbedTailFossilModel::createLayer),
        new Entry(FAN_TAIL, FanTailFossilModel::createLayer),
        new Entry(SPEARED_TAIL, SpearedTailFossilModel::createLayer),
        new Entry(BONEY_PLATES_BACK, BoneyPlatesFossilModel::createLayer),
        new Entry(DORSAL_SCALES_BACK, DorsalScalesFossilModel::createLayer),
        new Entry(SPIKES_BACK, SpikesFossilModel::createLayer),
        new Entry(SPINE_SAIL_BACK, SpineSailFossilModel::createLayer),
        new Entry(THORNS_BACK, ThornsFossilModel::createLayer)
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
