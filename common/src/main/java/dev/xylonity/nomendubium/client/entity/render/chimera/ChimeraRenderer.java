package dev.xylonity.nomendubium.client.entity.render.chimera;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.client.entity.model.NomenDubiumEntityModel;
import dev.xylonity.nomendubium.client.entity.model.chimera.normal.back.*;
import dev.xylonity.nomendubium.client.entity.model.chimera.normal.body.*;
import dev.xylonity.nomendubium.client.entity.model.chimera.normal.head.*;
import dev.xylonity.nomendubium.client.entity.model.chimera.normal.tail.*;
import dev.xylonity.nomendubium.client.util.ChimeraModelLayers;
import dev.xylonity.nomendubium.common.entity.ChimeraEntity;
import dev.xylonity.nomendubium.common.entity.variant.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.EnumMap;
import java.util.Map;

public final class ChimeraRenderer extends EntityRenderer<ChimeraEntity> {

    private final Map<ChimeraBodyVariant, RenderedBody> bodies = new EnumMap<>(ChimeraBodyVariant.class);
    private final Map<ChimeraHeadVariant, RenderedPart> heads = new EnumMap<>(ChimeraHeadVariant.class);
    private final Map<ChimeraTailVariant, RenderedPart> tails = new EnumMap<>(ChimeraTailVariant.class);
    private final Map<ChimeraBackVariant, RenderedPart> backs = new EnumMap<>(ChimeraBackVariant.class);

    public ChimeraRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 1.0F;
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
    public void render(ChimeraEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffers, int packedLight) {
        final RenderedBody body = this.bodies.get(entity.getBodyVariant());
        if (body == null) {
            super.render(entity, entityYaw, partialTicks, poseStack, buffers, packedLight);
            return;
        }

        final float age = entity.tickCount + partialTicks;
        final float limbSwing = entity.walkAnimation.position(partialTicks);
        final float limbAmount = entity.walkAnimation.speed(partialTicks);
        final float bodyYaw = Mth.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot);
        final float headYaw = Mth.wrapDegrees(Mth.rotLerp(partialTicks, entity.yHeadRotO, entity.yHeadRot) - bodyYaw);
        final float headPitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());

        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - bodyYaw));

        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0F, -1.501F, 0.0F);

        renderPart(body.part(), entity, limbSwing, limbAmount, age, headYaw, headPitch, poseStack, buffers, packedLight);
        renderAttached(heads.get(entity.getHeadVariant()), body.model(), Attachment.HEAD, entity, limbSwing, limbAmount, age, headYaw, headPitch, poseStack, buffers, packedLight);
        renderAttached(tails.get(entity.getTailVariant()), body.model(), Attachment.TAIL, entity, limbSwing, limbAmount, age, headYaw, headPitch, poseStack, buffers, packedLight);
        renderAttached(backs.get(entity.getBackVariant()), body.model(), Attachment.BACK, entity, limbSwing, limbAmount, age, headYaw, headPitch, poseStack, buffers, packedLight);
        renderRider(entity, body.model(), partialTicks, poseStack, buffers, packedLight);

        poseStack.popPose();

        super.render(entity, entityYaw, partialTicks, poseStack, buffers, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ChimeraEntity entity) {
        final RenderedBody body = this.bodies.get(entity.getBodyVariant());
        return body == null ? NomenDubium.of("textures/entity/chimera/hulkingbody.png") : body.part().baseTexture();
    }

    private void renderAttached(RenderedPart part, ChimeraBodyModel body, Attachment attachment, ChimeraEntity entity, float limbSwing, float limbAmount, float age, float headYaw, float headPitch, PoseStack poses, MultiBufferSource buffers, int light) {
        if (part == null) {
            return;
        }

        poses.pushPose();

        switch (attachment) {
            case HEAD -> body.moveToHead(poses);
            case TAIL -> body.moveToTail(poses);
            case BACK -> body.moveToBack(poses);
        }

        renderPart(part, entity, limbSwing, limbAmount, age, headYaw, headPitch, poses, buffers, light);

        poses.popPose();
    }

    private void renderPart(RenderedPart part, ChimeraEntity entity, float limbSwing, float limbAmount, float age, float headYaw, float headPitch, PoseStack poses, MultiBufferSource buffers, int light) {
        if (part == null) {
            return;
        }

        part.model().resetPose();
        part.model().setupAnim(entity, limbSwing, limbAmount, age, headYaw, headPitch);

        final VertexConsumer consumer;
        if (entity.getPaletteVariant() == ChimeraPaletteVariant.NORMAL) {
            consumer = buffers.getBuffer(part.model().renderType(part.baseTexture()));
        }
        else {
            final TextureAtlasSprite sprite = Minecraft.getInstance().getModelManager().getAtlas(Sheets.ARMOR_TRIMS_SHEET).getSprite(part.paletteSprites().get(entity.getPaletteVariant()));
            consumer = sprite.wrap(buffers.getBuffer(part.model().renderType(Sheets.ARMOR_TRIMS_SHEET)));
        }

        final int overlay = LivingEntityRenderer.getOverlayCoords(entity, 0.0F);
        part.model().renderToBuffer(poses, consumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderRider(ChimeraEntity chimera, ChimeraBodyModel body, float partialTicks, PoseStack poseStack, MultiBufferSource buffers, int packedLight) {
        if (!(chimera.getFirstPassenger() instanceof AbstractClientPlayer rider)) {
            return;
        }

        final Minecraft minecraft = Minecraft.getInstance();
        if (rider == minecraft.player && minecraft.options.getCameraType().isFirstPerson()) {
            return;
        }

        final EntityRenderer<? super AbstractClientPlayer> renderer = minecraft.getEntityRenderDispatcher().getRenderer(rider);
        poseStack.pushPose();

        body.moveToRider(poseStack);

        poseStack.scale(-1, -1, 1);
        poseStack.translate(0, -0.57F, 0);

        final float riderBodyYaw = getPassengerBodyYaw(rider, chimera, partialTicks);
        poseStack.mulPose(Axis.YP.rotationDegrees(riderBodyYaw - 180.0F));
        renderer.render(rider, 0.0F, partialTicks, poseStack, buffers, packedLight);

        poseStack.popPose();
    }

    private static float getPassengerBodyYaw(AbstractClientPlayer rider, ChimeraEntity chimera, float partialTicks) {
        final float vehicleYaw = Mth.rotLerp(partialTicks, chimera.yBodyRotO, chimera.yBodyRot);
        final float headYaw = Mth.rotLerp(partialTicks, rider.yHeadRotO, rider.yHeadRot);
        final float headOffset = Mth.clamp(Mth.wrapDegrees(headYaw - vehicleYaw), -85.0F, 85.0F);
        float bodyYaw = headYaw - headOffset;
        if (headOffset * headOffset > 2500.0F) {
            bodyYaw += headOffset * 0.2F;
        }

        return bodyYaw;
    }

    private static RenderedPart part(NomenDubiumEntityModel<ChimeraEntity> model, String texture) {
        final Map<ChimeraPaletteVariant, ResourceLocation> sprites = new EnumMap<>(ChimeraPaletteVariant.class);
        for (final ChimeraPaletteVariant palette : ChimeraPaletteVariant.values()) {
            if (palette != ChimeraPaletteVariant.NORMAL) {
                sprites.put(palette, NomenDubium.of("entity/chimera/" + texture + "_" + palette.parsedName()));
            }

        }

        return new RenderedPart(model, NomenDubium.of("textures/entity/chimera/" + texture + ".png"), sprites);
    }

    private static RenderedBody body(ChimeraBodyModel model, String texture) {
        return new RenderedBody(model, part(model, texture));
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
            NomenDubiumEntityModel<ChimeraEntity> model,
            ResourceLocation baseTexture,
            Map<ChimeraPaletteVariant, ResourceLocation> paletteSprites
    ) {
        ;;
    }

}