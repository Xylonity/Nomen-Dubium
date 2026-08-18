package dev.xylonity.nomendubium.client.model;

import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.client.entity.model.back.BoneyPlatesModel;
import dev.xylonity.nomendubium.client.entity.model.back.DorsalScalesModel;
import dev.xylonity.nomendubium.client.entity.model.back.SpikesModel;
import dev.xylonity.nomendubium.client.entity.model.back.SpineSailModel;
import dev.xylonity.nomendubium.client.entity.model.back.ThornsModel;
import dev.xylonity.nomendubium.client.entity.model.body.AvianBodyModel;
import dev.xylonity.nomendubium.client.entity.model.body.LankyBodyModel;
import dev.xylonity.nomendubium.client.entity.model.body.PuffyBodyModel;
import dev.xylonity.nomendubium.client.entity.model.body.ShelledBodyModel;
import java.util.List;
import java.util.function.Supplier;

import dev.xylonity.nomendubium.client.entity.model.body.HulkingBodyModel;
import dev.xylonity.nomendubium.client.entity.model.head.BeakedHeadModel;
import dev.xylonity.nomendubium.client.entity.model.head.CrunchingHeadModel;
import dev.xylonity.nomendubium.client.entity.model.head.ShieldedHeadModel;
import dev.xylonity.nomendubium.client.entity.model.head.SnarledHeadModel;
import dev.xylonity.nomendubium.client.entity.model.head.SnortingHeadModel;
import dev.xylonity.nomendubium.client.entity.model.tail.ClubbedTailModel;
import dev.xylonity.nomendubium.client.entity.model.tail.FanTailModel;
import dev.xylonity.nomendubium.client.entity.model.tail.SpearedTailModel;
import dev.xylonity.nomendubium.client.entity.model.tail.SpikedTailModel;
import dev.xylonity.nomendubium.client.entity.model.tail.StubbyTailModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

public final class ChimeraModelLayers {

    public static final ModelLayerLocation HULKING_BODY = layer("hulkingbody");
    public static final ModelLayerLocation SHELLED_BODY = layer("shelledbody");
    public static final ModelLayerLocation AVIAN_BODY = layer("avianbody");
    public static final ModelLayerLocation LANKY_BODY = layer("lankybody");
    public static final ModelLayerLocation PUFFY_BODY = layer("puffybody");
    public static final ModelLayerLocation CRUNCHING_HEAD = layer("crunchinghead");
    public static final ModelLayerLocation SHIELDED_HEAD = layer("shieldedhead");
    public static final ModelLayerLocation SNARLED_HEAD = layer("snarledhead");
    public static final ModelLayerLocation BEAKED_HEAD = layer("beakedhead");
    public static final ModelLayerLocation SNORTING_HEAD = layer("snortinghead");
    public static final ModelLayerLocation SPIKED_TAIL = layer("spikedtail");
    public static final ModelLayerLocation STUBBY_TAIL = layer("stubbytail");
    public static final ModelLayerLocation CLUBBED_TAIL = layer("clubbedtail");
    public static final ModelLayerLocation FAN_TAIL = layer("fantail");
    public static final ModelLayerLocation SPEARED_TAIL = layer("spearedtail");
    public static final ModelLayerLocation BONEY_PLATES = layer("boneyplates");
    public static final ModelLayerLocation DORSAL_SCALES = layer("dorsalscales");
    public static final ModelLayerLocation SPIKES = layer("spikes");
    public static final ModelLayerLocation SPINE_SAIL = layer("spinesail");
    public static final ModelLayerLocation THORNS = layer("thorns");

    private static final List<Entry> ENTRIES = List.of(
        new Entry(HULKING_BODY, HulkingBodyModel::createLayer),
        new Entry(SHELLED_BODY, ShelledBodyModel::createLayer),
        new Entry(AVIAN_BODY, AvianBodyModel::createLayer),
        new Entry(LANKY_BODY, LankyBodyModel::createLayer),
        new Entry(PUFFY_BODY, PuffyBodyModel::createLayer),
        new Entry(CRUNCHING_HEAD, CrunchingHeadModel::createLayer),
        new Entry(SHIELDED_HEAD, ShieldedHeadModel::createLayer),
        new Entry(SNARLED_HEAD, SnarledHeadModel::createLayer),
        new Entry(BEAKED_HEAD, BeakedHeadModel::createLayer),
        new Entry(SNORTING_HEAD, SnortingHeadModel::createLayer),
        new Entry(SPIKED_TAIL, SpikedTailModel::createLayer),
        new Entry(STUBBY_TAIL, StubbyTailModel::createLayer),
        new Entry(CLUBBED_TAIL, ClubbedTailModel::createLayer),
        new Entry(FAN_TAIL, FanTailModel::createLayer),
        new Entry(SPEARED_TAIL, SpearedTailModel::createLayer),
        new Entry(BONEY_PLATES, BoneyPlatesModel::createLayer),
        new Entry(DORSAL_SCALES, DorsalScalesModel::createLayer),
        new Entry(SPIKES, SpikesModel::createLayer),
        new Entry(SPINE_SAIL, SpineSailModel::createLayer),
        new Entry(THORNS, ThornsModel::createLayer)
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