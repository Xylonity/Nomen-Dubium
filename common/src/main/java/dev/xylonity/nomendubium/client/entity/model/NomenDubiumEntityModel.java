package dev.xylonity.nomendubium.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.function.Function;

public abstract class NomenDubiumEntityModel<T extends Entity> extends EntityModel<T> {

    protected final ModelPart root;

    protected NomenDubiumEntityModel(ModelPart root) {
        this(root, RenderType::entityCutoutNoCull);
    }

    protected NomenDubiumEntityModel(ModelPart root, Function<ResourceLocation, RenderType> renderType) {
        super(renderType);
        this.root = root;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int packedColor) {
        this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, packedColor);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        ;;
    }

    public void resetPose() {
        this.root.getAllParts().forEach(ModelPart::resetPose);
    }

}
