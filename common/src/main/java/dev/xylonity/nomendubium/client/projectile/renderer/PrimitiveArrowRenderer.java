package dev.xylonity.nomendubium.client.projectile.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.client.projectile.model.PrimitiveArrowModel;
import dev.xylonity.nomendubium.client.util.ArrowModelLayers;
import dev.xylonity.nomendubium.common.entity.PrimitiveArrowEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public final class PrimitiveArrowRenderer extends EntityRenderer<PrimitiveArrowEntity, PrimitiveArrowRenderState> {

    private static final Identifier TEXTURE = NomenDubium.of("textures/entity/projectile/primitive_arrow.png");

    private final PrimitiveArrowModel model;

    public PrimitiveArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
        model = new PrimitiveArrowModel(context.bakeLayer(ArrowModelLayers.PRIMITIVE_ARROW));
        shadowRadius = 0F;
    }

    @Override
    public void submit(PrimitiveArrowRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(state.xRot));

        poseStack.mulPose(Axis.XP.rotationDegrees(180));

        poseStack.scale(0.85f, 0.85f, 0.85f);
        poseStack.translate(-0.2, 0, 0);

        submitNodeCollector.submitModel(model, state, poseStack, TEXTURE, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);

        poseStack.popPose();

        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    @Override
    public PrimitiveArrowRenderState createRenderState() {
        return new PrimitiveArrowRenderState();
    }

    @Override
    public void extractRenderState(PrimitiveArrowEntity entity, PrimitiveArrowRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.xRot = entity.getXRot(partialTicks);
        state.yRot = entity.getYRot(partialTicks);
        state.shake = entity.shakeTime - partialTicks;
    }

}
