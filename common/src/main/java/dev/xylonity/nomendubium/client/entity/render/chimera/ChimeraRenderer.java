package dev.xylonity.nomendubium.client.entity.render.chimera;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.client.entity.model.chimera.normal.back.BoneyPlatesModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.normal.back.DorsalScalesModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.normal.back.SpikesModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.normal.back.SpineSailModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.normal.back.ThornsModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.normal.body.AvianBodyModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.normal.body.HulkingBodyModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.normal.body.LankyBodyModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.normal.body.ChimeraBodyModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.normal.body.PuffyBodyModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.normal.body.ShelledBodyModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.normal.head.BeakedHeadModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.normal.head.CrunchingHeadModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.normal.head.ShieldedHeadModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.normal.head.SnarledHeadModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.normal.head.SnortingHeadModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.normal.tail.ClubbedTailModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.normal.tail.FanTailModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.normal.tail.SpearedTailModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.normal.tail.SpikedTailModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.normal.tail.StubbyTailModel;
import dev.xylonity.nomendubium.client.util.ChimeraModelLayers;
import dev.xylonity.nomendubium.common.entity.ChimeraEntity;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraBackVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraBodyVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraHeadVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraPaletteVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraTailVariant;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;

public final class ChimeraRenderer extends EntityRenderer<ChimeraEntity, ChimeraRenderState> {

    private final Map<ChimeraBodyVariant, RenderedBody> bodies = new EnumMap<>(ChimeraBodyVariant.class);
    private final Map<ChimeraHeadVariant, RenderedPart> heads = new EnumMap<>(ChimeraHeadVariant.class);
    private final Map<ChimeraTailVariant, RenderedPart> tails = new EnumMap<>(ChimeraTailVariant.class);
    private final Map<ChimeraBackVariant, RenderedPart> backs = new EnumMap<>(ChimeraBackVariant.class);

    private final SpriteGetter sprites;

    public ChimeraRenderer(EntityRendererProvider.Context context) {
        super(context);
        shadowRadius = 1;
        sprites = context.getSprites();

        bodies.put(ChimeraBodyVariant.HULKING, body(new HulkingBodyModel(context.bakeLayer(ChimeraModelLayers.HULKING_BODY)), "hulkingbody"));
        bodies.put(ChimeraBodyVariant.SHELLED, body(new ShelledBodyModel(context.bakeLayer(ChimeraModelLayers.SHELLED_BODY)), "shelledbody"));
        bodies.put(ChimeraBodyVariant.AVIAN, body(new AvianBodyModel(context.bakeLayer(ChimeraModelLayers.AVIAN_BODY)), "avianbody"));
        bodies.put(ChimeraBodyVariant.LANKY, body(new LankyBodyModel(context.bakeLayer(ChimeraModelLayers.LANKY_BODY)), "lankybody"));
        bodies.put(ChimeraBodyVariant.PUFFY, body(new PuffyBodyModel(context.bakeLayer(ChimeraModelLayers.PUFFY_BODY)), "puffybody"));
        heads.put(ChimeraHeadVariant.CRUNCHING, part(new CrunchingHeadModel(context.bakeLayer(ChimeraModelLayers.CRUNCHING_HEAD)), "crunchinghead"));
        heads.put(ChimeraHeadVariant.SHIELDED, part(new ShieldedHeadModel(context.bakeLayer(ChimeraModelLayers.SHIELDED_HEAD)), "shieldedhead"));
        heads.put(ChimeraHeadVariant.SNARLED, part(new SnarledHeadModel(context.bakeLayer(ChimeraModelLayers.SNARLED_HEAD)), "snarledhead"));
        heads.put(ChimeraHeadVariant.BEAKED, part(new BeakedHeadModel(context.bakeLayer(ChimeraModelLayers.BEAKED_HEAD)), "beakedhead"));
        heads.put(ChimeraHeadVariant.SNORTING, part(new SnortingHeadModel(context.bakeLayer(ChimeraModelLayers.SNORTING_HEAD)), "snortinghead"));
        tails.put(ChimeraTailVariant.SPIKED, part(new SpikedTailModel(context.bakeLayer(ChimeraModelLayers.SPIKED_TAIL)), "spikedtail"));
        tails.put(ChimeraTailVariant.STUBBY, part(new StubbyTailModel(context.bakeLayer(ChimeraModelLayers.STUBBY_TAIL)), "stubbytail"));
        tails.put(ChimeraTailVariant.CLUBBED, part(new ClubbedTailModel(context.bakeLayer(ChimeraModelLayers.CLUBBED_TAIL)), "clubbedtail"));
        tails.put(ChimeraTailVariant.FAN, part(new FanTailModel(context.bakeLayer(ChimeraModelLayers.FAN_TAIL)), "fantail"));
        tails.put(ChimeraTailVariant.SPEARED, part(new SpearedTailModel(context.bakeLayer(ChimeraModelLayers.SPEARED_TAIL)), "spearedtail"));
        backs.put(ChimeraBackVariant.BONEY_PLATES, part(new BoneyPlatesModel(context.bakeLayer(ChimeraModelLayers.BONEY_PLATES)), "boneyplates"));
        backs.put(ChimeraBackVariant.DORSAL_SCALES, part(new DorsalScalesModel(context.bakeLayer(ChimeraModelLayers.DORSAL_SCALES)), "dorsalscales"));
        backs.put(ChimeraBackVariant.SPIKES, part(new SpikesModel(context.bakeLayer(ChimeraModelLayers.SPIKES)), "spikes"));
        backs.put(ChimeraBackVariant.SPINE_SAIL, part(new SpineSailModel(context.bakeLayer(ChimeraModelLayers.SPINE_SAIL)), "spinesail"));
        backs.put(ChimeraBackVariant.THORNS, part(new ThornsModel(context.bakeLayer(ChimeraModelLayers.THORNS)), "thorns"));
    }

    @Override
    public void submit(ChimeraRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - state.yRot));
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0F, -1.501F, 0.0F);

        final RenderedBody body = bodies.get(state.body);
        submitPart(body.part(), state, poseStack, submitNodeCollector);
        submitAttachedPart(heads.get(state.head), body.model(), Attachment.HEAD, state, poseStack, submitNodeCollector);
        submitAttachedPart(tails.get(state.tail), body.model(), Attachment.TAIL, state, poseStack, submitNodeCollector);
        submitAttachedPart(backs.get(state.back), body.model(), Attachment.BACK, state, poseStack, submitNodeCollector);

        poseStack.popPose();

        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    @Override
    public ChimeraRenderState createRenderState() {
        return new ChimeraRenderState();
    }

    @Override
    public void extractRenderState(ChimeraEntity entity, ChimeraRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.yRot = entity.getYRot(partialTicks);
        state.body = entity.getBodyVariant();
        state.head = entity.getHeadVariant();
        state.tail = entity.getTailVariant();
        state.back = entity.getBackVariant();
        state.palette = entity.getPaletteVariant();
    }

    private static RenderedPart part(EntityModel<ChimeraRenderState> model, String texture) {
        final Identifier baseTexture = NomenDubium.of("textures/entity/chimera/" + texture + ".png");
        final Map<ChimeraPaletteVariant, SpriteId> paletteSprites = new EnumMap<>(ChimeraPaletteVariant.class);
        for (final ChimeraPaletteVariant palette : ChimeraPaletteVariant.values()) {
            if (palette != ChimeraPaletteVariant.NORMAL) {
                paletteSprites.put(palette, sprite(texture, palette.parsedName()));
            }

        }

        return new RenderedPart(model, baseTexture, paletteSprites);
    }

    private static RenderedBody body(ChimeraBodyModel model, String texture) {
        return new RenderedBody(model, part(model, texture));
    }

    private static SpriteId sprite(String texture, String palette) {
        return new SpriteId(Sheets.ARMOR_TRIMS_SHEET, NomenDubium.of("entity/chimera/" + texture + "_" + palette));
    }

    private void submitPart(RenderedPart part, ChimeraRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
        if (part == null) {
            return;
        }

        if (state.palette == ChimeraPaletteVariant.NORMAL) {
            submitNodeCollector.submitModel(
                    part.model(),
                    state,
                    poseStack,
                    part.baseTexture(),
                    state.lightCoords,
                    OverlayTexture.NO_OVERLAY,
                    state.outlineColor,
                    null
            );

            return;
        }

        submitNodeCollector.submitModel(
            part.model(),
            state,
            poseStack,
            state.lightCoords,
            OverlayTexture.NO_OVERLAY,
            -1,
            part.paletteSprites().get(state.palette),
            sprites,
            state.outlineColor,
            null
        );

    }

    private void submitAttachedPart(RenderedPart part, ChimeraBodyModel body, Attachment attachment, ChimeraRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
        if (part == null) {
            return;
        }

        poseStack.pushPose();
        
        switch (attachment) {
            case HEAD -> body.moveToHead(poseStack);
            case TAIL -> body.moveToTail(poseStack);
            case BACK -> body.moveToBack(poseStack);
        }

        submitPart(part, state, poseStack, submitNodeCollector);

        poseStack.popPose();
    }

    private enum Attachment {
        HEAD,
        TAIL,
        BACK
    }

    private record RenderedBody(
            ChimeraBodyModel model,
            RenderedPart part
    ) {
        ;;
    }

    private record RenderedPart(
        EntityModel<ChimeraRenderState> model,
        Identifier baseTexture,
        Map<ChimeraPaletteVariant, SpriteId> paletteSprites
    ) {
        ;;
    }

}
