package dev.xylonity.nomendubium.client.projectile.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xylonity.nomendubium.common.entity.FossilisedMawProjectileEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class FossilisedMawRenderer extends EntityRenderer<FossilisedMawProjectileEntity> {

    // Top left collision position (based on the texture's layout 32x32 where only 30x32 pixels are functional)
    private static final float COLLISION_X = -0.171875F;
    private static final float COLLISION_Y = 0.484375F;

    private final ItemRenderer itemRenderer;

    public FossilisedMawRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.shadowRadius = 0.25F;
    }

    @Override
    public void render(FossilisedMawProjectileEntity entity, float entityYaw, float partialTicks, PoseStack poses, MultiBufferSource buffers, int light) {
        poses.pushPose();

        // Ground hit
        if (entity.isEmbedded()) {
            final Direction direction = entity.getImpactDirection();
            final Vector3f normal = vector(direction);
            final float offset = (float) FossilisedMawProjectileEntity.EXTRA_DEPTH - 0.045F;

            // Moving along the face normal first so the top left edge appears buried by the same depth on every face
            poses.translate(normal.x * offset, normal.y * offset, normal.z * offset);
            poses.mulPose(impactRotation(direction, entity.getImpactRoll()));

            // Shake like the arrow
            final float shake = entity.shakeTime - partialTicks;
            if (shake > 0) {
                poses.mulPose(Axis.ZP.rotationDegrees(-Mth.sin(shake * 3.0F) * shake));
            }

            // Placing the texture's contact edge at the entity origin after orienting it against the surface
            poses.scale(1.35F, 1.35F, 1.35F);
            poses.translate(-COLLISION_X, -COLLISION_Y, 0);
        }
        // Same forward rotation like the prehistoric maw
        else {
            poses.scale(1.35F, 1.35F, 1.35F);
            poses.mulPose(PrehistoricMawRenderer.movementRotation(entity.getXRot(), entity.getYRot()));
            poses.mulPose(Axis.XP.rotationDegrees(-(entity.tickCount + partialTicks) * FossilisedMawProjectileEntity.SPIN_DEGREES));
            poses.mulPose(Axis.YP.rotationDegrees(90.0F));
        }

        final int renderLight = entity.isEmbedded() ? LightTexture.FULL_BRIGHT : light;
        this.itemRenderer.renderStatic(entity.getItem(), ItemDisplayContext.NONE, renderLight, OverlayTexture.NO_OVERLAY, poses, buffers, entity.level(), entity.getId());

        poses.popPose();

        super.render(entity, entityYaw, partialTicks, poses, buffers, light);
    }

    private static Quaternionf impactRotation(Direction direction, float roll) {
        final Vector3f outward = vector(direction);
        final Vector3f tangent = direction.getAxis().isVertical() ? new Vector3f(0, 0, 1) : new Vector3f(0, 1, 0);
        final float radians = (float) Math.toRadians(roll);
        final Vector3f rollTangent = new Vector3f(outward).cross(tangent);

        tangent.mul((float) Math.cos(radians)).fma((float) Math.sin(radians), rollTangent).normalize();
        float bodyX = 0.0516F - COLLISION_X;
        float bodyY = 0.06F - COLLISION_Y;

        final float inverse = 1.0F / (float) Math.sqrt(bodyX * bodyX + bodyY * bodyY);
        bodyX *= inverse;
        bodyY *= inverse;

        final float tangentX = -bodyY;
        final float tangentY = bodyX;

        final Vector3f modelX = new Vector3f(outward).mul(bodyX).fma(tangentX, tangent);
        final Vector3f modelY = new Vector3f(outward).mul(bodyY).fma(tangentY, tangent);
        final Vector3f modelZ = new Vector3f(outward).cross(tangent).normalize();
        return new Quaternionf().setFromNormalized(new Matrix3f().setColumn(0, modelX).setColumn(1, modelY).setColumn(2, modelZ));
    }

    private static Vector3f vector(Direction direction) {
        return new Vector3f(direction.getStepX(), direction.getStepY(), direction.getStepZ());
    }

    @Override
    public ResourceLocation getTextureLocation(FossilisedMawProjectileEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

}