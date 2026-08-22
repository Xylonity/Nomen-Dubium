package dev.xylonity.nomendubium.client.projectile.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xylonity.nomendubium.common.entity.FossilisedMawProjectileEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Matrix3f;
import org.joml.Vector3f;

public final class FossilisedMawRenderer extends EntityRenderer<FossilisedMawProjectileEntity, FossilisedMawRenderState> {

    // Top left collision position (based on the texture's layout 32x32 where only 30x32 pixels are functional)
    private static final float COLLISION_X = -0.171875F;
    private static final float COLLISION_Y = 0.484375F;

    private final ItemModelResolver itemModelResolver;

    public FossilisedMawRenderer(EntityRendererProvider.Context context) {
        super(context);
        itemModelResolver = context.getItemModelResolver();
        shadowRadius = 0.25F;
    }

    @Override
    public void submit(FossilisedMawRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();

        final float scale = 1.35F;

        // Ground hit
        if (state.embedded) {
            final Vector3f normal = new Vector3f(state.impactDirection.getUnitVec3f());
            float surfaceOffset = (float) FossilisedMawProjectileEntity.EXTRA_DEPTH - 0.045f;

            // Moving along the face normal first so the top left edge appears buried by the same depth on every face
            poseStack.translate(normal.x * surfaceOffset, normal.y * surfaceOffset, normal.z * surfaceOffset);
            poseStack.mulPose(state.rotation);

            // Shake like the arrow
            if (state.shake > 0.0F) {
                final float shakeRotation = -Mth.sin(state.shake * 3.0F) * state.shake;
                poseStack.mulPose(Axis.ZP.rotationDegrees(shakeRotation));
            }

            poseStack.scale(scale, scale, scale);

            // Placing the texture's contact edge at the entity origin after orienting it against the surface
            poseStack.translate(-COLLISION_X, -COLLISION_Y, 0.0F);
        }
        // Same forward rotation like the prehistoric maw
        else {
            poseStack.scale(scale, scale, scale);
            poseStack.mulPose(state.rotation);
            poseStack.mulPose(Axis.XP.rotationDegrees(-state.spin));
            poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        }

        state.item.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);

        poseStack.popPose();

        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    @Override
    public FossilisedMawRenderState createRenderState() {
        return new FossilisedMawRenderState();
    }

    @Override
    public void extractRenderState(FossilisedMawProjectileEntity entity, FossilisedMawRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);

        itemModelResolver.updateForNonLiving(state.item, entity.getItem(), ItemDisplayContext.NONE, entity);

        state.embedded = entity.isEmbedded();
        state.impactDirection = entity.getImpactDirection();
        state.impactRoll = entity.getImpactRoll();
        state.shake = Math.max(entity.shakeTime - partialTicks, 0.0F);
        state.spin = (entity.tickCount + partialTicks) * FossilisedMawProjectileEntity.SPIN_DEGREES;

        if (state.embedded) {
            updateImpactRotation(state, state.impactDirection, state.impactRoll);
        }
        else {
            updateMovementRotation(state, entity.getXRot(partialTicks), entity.getYRot(partialTicks));
        }

    }

    private static void updateImpactRotation(FossilisedMawRenderState state, Direction direction, float impactRoll) {
        final Vector3f outward = new Vector3f(direction.getUnitVec3f());

        // Picking a stable tangent for both horizontal and vertical faces before applying the random impact roll
        final Vector3f surfaceTangent = direction.getAxis().isVertical() ? new Vector3f(0, 0, 1) : new Vector3f(0, 1, 0);

        final float rollRadians = (float) Math.toRadians(impactRoll);
        final Vector3f rollTangent = new Vector3f(outward).cross(surfaceTangent);

        surfaceTangent.mul((float) Math.cos(rollRadians)).fma((float) Math.sin(rollRadians), rollTangent).normalize();

        float bodyX = 0.0516F - COLLISION_X;
        float bodyY = 0.06F - COLLISION_Y;

        // This normalized vector points from the contact edge towards the body of the sprite
        final float inverseLength = 1F / (float) Math.sqrt(bodyX * bodyX + bodyY * bodyY);
        bodyX *= inverseLength;
        bodyY *= inverseLength;

        // The perpendicular is mapped to the tangent of the face, computed for the top left edge of the texture (where it should collide)
        final float tangentX = -bodyY;
        final float tangentY = bodyX;
        final Vector3f modelX = new Vector3f(outward).mul(bodyX).fma(tangentX, surfaceTangent);
        final Vector3f modelY = new Vector3f(outward).mul(bodyY).fma(tangentY, surfaceTangent);
        final Vector3f modelZ = new Vector3f(outward).cross(surfaceTangent).normalize();

        final Matrix3f impactFrame = new Matrix3f().setColumn(0, modelX).setColumn(1, modelY).setColumn(2, modelZ);
        state.rotation.setFromNormalized(impactFrame);
    }

    private static void updateMovementRotation(FossilisedMawRenderState state, float xRot, float yRot) {
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
