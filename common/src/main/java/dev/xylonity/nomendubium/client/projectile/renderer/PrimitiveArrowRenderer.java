package dev.xylonity.nomendubium.client.projectile.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.client.projectile.model.PrimitiveArrowModel;
import dev.xylonity.nomendubium.client.util.ArrowModelLayers;
import dev.xylonity.nomendubium.common.entity.PrimitiveArrowEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public final class PrimitiveArrowRenderer extends EntityRenderer<PrimitiveArrowEntity> {

    private static final ResourceLocation TEXTURE = NomenDubium.of("textures/entity/projectile/primitive_arrow.png");
    private final PrimitiveArrowModel model;

    public PrimitiveArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new PrimitiveArrowModel(context.bakeLayer(ArrowModelLayers.PRIMITIVE_ARROW));
        this.shadowRadius = 0.0F;
    }

    @Override
    public void render(PrimitiveArrowEntity entity, float entityYaw, float partialTicks, PoseStack poses, MultiBufferSource buffers, int packedLight) {
        poses.pushPose();

        poses.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
        poses.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));

        final float shake = entity.shakeTime - partialTicks;
        if (shake > 0.0F) {
            poses.mulPose(Axis.XP.rotationDegrees(-Mth.sin(shake * 3.0F) * shake));
        }

        poses.mulPose(Axis.XP.rotationDegrees(180.0F));
        poses.scale(0.85F, 0.85F, 0.85F);
        poses.translate(-0.2, 0, 0);

        this.model.resetPose();

        this.model.renderToBuffer(poses, buffers.getBuffer(this.model.renderType(TEXTURE)), packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

        poses.popPose();

        super.render(entity, entityYaw, partialTicks, poses, buffers, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(PrimitiveArrowEntity entity) {
        return TEXTURE;
    }

}
