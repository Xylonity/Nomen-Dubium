package dev.xylonity.nomendubium.client.projectile.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
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
import dev.xylonity.nomendubium.client.util.SkeletonPartModelLayers;
import dev.xylonity.nomendubium.common.entity.SkeletonPartEntity;
import dev.xylonity.nomendubium.common.entity.skeleton.SkeletonPartType;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import java.util.EnumMap;
import java.util.Map;

public final class SkeletonPartRenderer extends EntityRenderer<SkeletonPartEntity, SkeletonPartRenderState> {

    private final Map<SkeletonPartType, RenderedPart> parts = new EnumMap<>(SkeletonPartType.class);

    public SkeletonPartRenderer(EntityRendererProvider.Context context) {
        super(context);
        shadowRadius = 0F;

        put(SkeletonPartType.HULKING_BODY, new HulkingSkeletonModel(context.bakeLayer(SkeletonPartModelLayers.HULKING_BODY)));
        put(SkeletonPartType.SHELLED_BODY, new ShelledSkeletonModel(context.bakeLayer(SkeletonPartModelLayers.SHELLED_BODY)));
        put(SkeletonPartType.AVIAN_BODY, new AvianSkeletonModel(context.bakeLayer(SkeletonPartModelLayers.AVIAN_BODY)));
        put(SkeletonPartType.LANKY_BODY, new LankySkeletonModel(context.bakeLayer(SkeletonPartModelLayers.LANKY_BODY)));
        put(SkeletonPartType.PUFFY_BODY, new PuffySkeletonModel(context.bakeLayer(SkeletonPartModelLayers.PUFFY_BODY)));

        put(SkeletonPartType.CRUNCHING_HEAD, new CrunchingSkullModel(context.bakeLayer(SkeletonPartModelLayers.CRUNCHING_HEAD)));
        put(SkeletonPartType.SHIELDED_HEAD, new ShieldedSkullModel(context.bakeLayer(SkeletonPartModelLayers.SHIELDED_HEAD)));
        put(SkeletonPartType.SNARLED_HEAD, new SnarledSkullModel(context.bakeLayer(SkeletonPartModelLayers.SNARLED_HEAD)));
        put(SkeletonPartType.BEAKED_HEAD, new BeakedSkullModel(context.bakeLayer(SkeletonPartModelLayers.BEAKED_HEAD)));
        put(SkeletonPartType.SNORTING_HEAD, new SnortingSkullModel(context.bakeLayer(SkeletonPartModelLayers.SNORTING_HEAD)));

        put(SkeletonPartType.SPIKED_TAIL, new SpikedTailFossilModel(context.bakeLayer(SkeletonPartModelLayers.SPIKED_TAIL)));
        put(SkeletonPartType.STUBBY_TAIL, new StubbyTailFossilModel(context.bakeLayer(SkeletonPartModelLayers.STUBBY_TAIL)));
        put(SkeletonPartType.CLUBBED_TAIL, new ClubbedTailFossilModel(context.bakeLayer(SkeletonPartModelLayers.CLUBBED_TAIL)));
        put(SkeletonPartType.FAN_TAIL, new FanTailFossilModel(context.bakeLayer(SkeletonPartModelLayers.FAN_TAIL)));
        put(SkeletonPartType.SPEARED_TAIL, new SpearedTailFossilModel(context.bakeLayer(SkeletonPartModelLayers.SPEARED_TAIL)));

        put(SkeletonPartType.BONEY_PLATES_BACK, new BoneyPlatesFossilModel(context.bakeLayer(SkeletonPartModelLayers.BONEY_PLATES_BACK)));
        put(SkeletonPartType.DORSAL_SCALES_BACK, new DorsalScalesFossilModel(context.bakeLayer(SkeletonPartModelLayers.DORSAL_SCALES_BACK)));
        put(SkeletonPartType.SPIKES_BACK, new SpikesFossilModel(context.bakeLayer(SkeletonPartModelLayers.SPIKES_BACK)));
        put(SkeletonPartType.SPINE_SAIL_BACK, new SpineSailFossilModel(context.bakeLayer(SkeletonPartModelLayers.SPINE_SAIL_BACK)));
        put(SkeletonPartType.THORNS_BACK, new ThornsFossilModel(context.bakeLayer(SkeletonPartModelLayers.THORNS_BACK)));
    }

    @Override
    public void submit(SkeletonPartRenderState state, @NonNull PoseStack poseStack, @NonNull SubmitNodeCollector submitNodeCollector, @NonNull CameraRenderState camera) {
        final RenderedPart renderedPart = parts.get(state.partType);
        if (renderedPart == null) {
            return;
        }

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(180F - state.yRot));
        poseStack.scale(-1F, -1F, 1F);
        if (state.partType.isBody()) {
            poseStack.translate(0F, -1.501F, 0F);
        }

        submitNodeCollector.submitModel(
            renderedPart.model,
            state,
            poseStack,
            renderedPart.texture,
            state.lightCoords,
            OverlayTexture.NO_OVERLAY,
            state.outlineColor,
            null
        );

        poseStack.popPose();

        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    @Override
    public SkeletonPartRenderState createRenderState() {
        return new SkeletonPartRenderState();
    }

    @Override
    public void extractRenderState(SkeletonPartEntity entity, SkeletonPartRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.partType = entity.getPartType();
        state.yRot = entity.getYRot(partialTicks);
    }

    private void put(SkeletonPartType type, EntityModel<SkeletonPartRenderState> model) {
        parts.put(type, new RenderedPart(model, NomenDubium.of("textures/entity/skeleton/" + type.texture() + ".png")));
    }

    private record RenderedPart(
            EntityModel<SkeletonPartRenderState> model,
            Identifier texture
    ) {
        ;;
    }

}