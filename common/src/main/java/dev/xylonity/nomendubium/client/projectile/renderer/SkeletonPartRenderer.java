package dev.xylonity.nomendubium.client.projectile.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.client.entity.model.NomenDubiumEntityModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.skeleton.back.*;
import dev.xylonity.nomendubium.client.entity.model.chimera.skeleton.body.*;
import dev.xylonity.nomendubium.client.entity.model.chimera.skeleton.head.*;
import dev.xylonity.nomendubium.client.entity.model.chimera.skeleton.tail.*;
import dev.xylonity.nomendubium.client.util.SkeletonPartModelLayers;
import dev.xylonity.nomendubium.common.entity.SkeletonPartEntity;
import dev.xylonity.nomendubium.common.entity.skeleton.SkeletonPartType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.EnumMap;
import java.util.Map;

public final class SkeletonPartRenderer extends EntityRenderer<SkeletonPartEntity> {

    private final Map<SkeletonPartType, RenderedPart> parts = new EnumMap<>(SkeletonPartType.class);

    public SkeletonPartRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.0F;

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
    public void render(SkeletonPartEntity entity, float entityYaw, float partialTicks, PoseStack poses, MultiBufferSource buffers, int packedLight) {
        final RenderedPart part = this.parts.get(entity.getPartType());
        if (part == null) {
            return;
        }

        poses.pushPose();

        applyRevivalAnimation(entity, partialTicks, poses);

        poses.mulPose(Axis.YP.rotationDegrees(180.0F - Mth.lerp(partialTicks, entity.yRotO, entity.getYRot())));
        poses.scale(-1.0F, -1.0F, 1.0F);
        if (entity.getPartType().isBody()) {
            poses.translate(0.0F, -1.501F, 0.0F);
        }

        part.model().resetPose();
        part.model().setupAnim(entity, 0, 0, entity.tickCount + partialTicks, 0, 0);

        final VertexConsumer consumer = buffers.getBuffer(part.model().renderType(part.texture()));
        part.model().renderToBuffer(poses, consumer, packedLight, OverlayTexture.NO_OVERLAY, -1);

        poses.popPose();

        super.render(entity, entityYaw, partialTicks, poses, buffers, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(SkeletonPartEntity entity) {
        final RenderedPart part = this.parts.get(entity.getPartType());
        return part == null ? NomenDubium.of("textures/entity/skeleton/hulking_body.png") : part.texture();
    }

    private static void applyRevivalAnimation(SkeletonPartEntity entity, float partialTicks, PoseStack poses) {
        final int ticks = entity.getRevivalTicks();
        if (ticks <= 0) {
            return;
        }

        final float revivalTicks = ticks + partialTicks;
        final float progress = Mth.clamp(revivalTicks / SkeletonPartEntity.REVIVAL_DURATION, 0, 1);
        final float intensity = Mth.sin(progress * Mth.PI);
        final float lift = intensity * 0.7F;
        final float spin = progress * 720.0F + Mth.sin(revivalTicks * 0.45F) * intensity * 12.0F;
        final float tilt = Mth.sin(revivalTicks * 0.32F) * intensity * 7.0F;
        final float pulse = 1.0F + Mth.sin(revivalTicks * 0.7F) * intensity * 0.06F;
        final Vec3 pivot = entity.getRevivalPivotOffset();

        poses.translate(pivot.x, pivot.y + lift, pivot.z);
        poses.mulPose(Axis.YP.rotationDegrees(spin));
        poses.mulPose(Axis.ZP.rotationDegrees(tilt));
        poses.scale(pulse, pulse, pulse);
        poses.translate(-pivot.x, -pivot.y, -pivot.z);
    }

    private void put(SkeletonPartType type, NomenDubiumEntityModel<SkeletonPartEntity> model) {
        this.parts.put(type, new RenderedPart(model, NomenDubium.of("textures/entity/skeleton/" + type.texture() + ".png")));
    }

    private record RenderedPart(
            NomenDubiumEntityModel<SkeletonPartEntity> model,
            ResourceLocation texture
    ) {
        ;;
    }

}
