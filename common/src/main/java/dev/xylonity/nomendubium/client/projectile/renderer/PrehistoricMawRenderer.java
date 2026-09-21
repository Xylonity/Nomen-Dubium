package dev.xylonity.nomendubium.client.projectile.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xylonity.nomendubium.common.entity.PrehistoricMawProjectileEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class PrehistoricMawRenderer extends EntityRenderer<PrehistoricMawProjectileEntity> {

    private final ItemRenderer itemRenderer;

    public PrehistoricMawRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.shadowRadius = 0.25F;
    }

    @Override
    public void render(PrehistoricMawProjectileEntity entity, float entityYaw, float partialTicks, PoseStack poses, MultiBufferSource buffers, int light) {
        poses.pushPose();

        poses.scale(1.35F, 1.35F, 1.35F);

        poses.mulPose(movementRotation(entity.getXRot(), entity.getYRot()));
        poses.mulPose(Axis.ZP.rotationDegrees(entity.getBankAngle(partialTicks)));
        poses.mulPose(Axis.XP.rotationDegrees(-(entity.tickCount + partialTicks) * 36.0F));
        poses.mulPose(Axis.YP.rotationDegrees(90.0F));

        this.itemRenderer.renderStatic(entity.getItem(), ItemDisplayContext.NONE, light, OverlayTexture.NO_OVERLAY, poses, buffers, entity.level(), entity.getId());

        poses.popPose();

        super.render(entity, entityYaw, partialTicks, poses, buffers, light);
    }

    static Quaternionf movementRotation(float xRot, float yRot) {
        final float pitch = (float) Math.toRadians(xRot);
        final float yaw = (float) Math.toRadians(yRot);
        final float horizontal = (float) Math.cos(pitch);

        final Vector3f forward = new Vector3f((float) Math.sin(yaw) * horizontal, (float) Math.sin(pitch), (float) Math.cos(yaw) * horizontal).normalize();
        final Vector3f lateral = new Vector3f(0, 1, 0).cross(forward);
        if (lateral.lengthSquared() < 1.0E-6F) {
            // The vertical trajectory if derived from the lateral axis (based on the yaw)
            lateral.set((float) Math.cos(yaw), 0, (float) -Math.sin(yaw));
        }
        else {
            lateral.normalize();
        }

        // Euler angles are very hard to use to handle roll rotation while keeping the forward spin, so a quaternion is used
        final Vector3f up = new Vector3f(forward).cross(lateral).normalize();
        return new Quaternionf().setFromNormalized(new Matrix3f().setColumn(0, lateral).setColumn(1, up).setColumn(2, forward));
    }

    @Override
    public ResourceLocation getTextureLocation(PrehistoricMawProjectileEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

}
