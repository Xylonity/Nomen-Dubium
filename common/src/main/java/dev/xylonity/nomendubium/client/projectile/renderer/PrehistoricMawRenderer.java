package dev.xylonity.nomendubium.client.projectile.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xylonity.nomendubium.common.entity.PrehistoricMawProjectileEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Matrix3f;
import org.joml.Vector3f;

public final class PrehistoricMawRenderer extends EntityRenderer<PrehistoricMawProjectileEntity, PrehistoricMawRenderState> {

    private final ItemModelResolver itemModelResolver;

    public PrehistoricMawRenderer(EntityRendererProvider.Context context) {
        super(context);
        itemModelResolver = context.getItemModelResolver();
        shadowRadius = 0.25F;
    }

    @Override
    public void submit(PrehistoricMawRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {

        final float scale = 1.35F;

        poseStack.pushPose();

        poseStack.scale(scale, scale, scale);

        poseStack.mulPose(state.rotation);
        poseStack.mulPose(Axis.ZP.rotationDegrees(state.bank));

        poseStack.mulPose(Axis.XP.rotationDegrees(-state.spin));
        poseStack.mulPose(Axis.YP.rotationDegrees(90));

        state.item.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);

        poseStack.popPose();

        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    @Override
    public PrehistoricMawRenderState createRenderState() {
        return new PrehistoricMawRenderState();
    }

    @Override
    public void extractRenderState(PrehistoricMawProjectileEntity entity, PrehistoricMawRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        itemModelResolver.updateForNonLiving(state.item, entity.getItem(), ItemDisplayContext.NONE, entity);
        updateMovementRotation(state, entity.getXRot(partialTicks), entity.getYRot(partialTicks));
        state.bank = entity.getBankAngle(partialTicks);
        state.spin = (entity.tickCount + partialTicks) * 36;
    }

    /// Copy of {@link FossilisedMawRenderer} with a few changes
    /// TODO: refactor into a utility method
    private static void updateMovementRotation(PrehistoricMawRenderState state, float xRot, float yRot) {
        final float pitch = (float) Math.toRadians(xRot);
        final float yaw = (float) Math.toRadians(yRot);
        final float horizontal = (float) Math.cos(pitch);

        final Vector3f forward = new Vector3f((float) Math.sin(yaw) * horizontal, (float) Math.sin(pitch), (float) Math.cos(yaw) * horizontal).normalize();

        final Vector3f lateral = new Vector3f(0.0F, 1.0F, 0.0F).cross(forward);
        if (lateral.lengthSquared() < 1.0E-6F) {
            // The vertical trajectory if derived from the lateral axis (based on the yaw)
            lateral.set((float) Math.cos(yaw), 0.0F, (float) -Math.sin(yaw));
        }
        else {
            lateral.normalize();
        }

        final Vector3f up = new Vector3f(forward).cross(lateral).normalize();

        final Matrix3f movementFrame = new Matrix3f().setColumn(0, lateral).setColumn(1, up).setColumn(2, forward);

        // Euler angles are very hard to use to handle roll rotation while keeping the forward spin, so a quaternion is used
        state.rotation.setFromNormalized(movementFrame);
    }

}