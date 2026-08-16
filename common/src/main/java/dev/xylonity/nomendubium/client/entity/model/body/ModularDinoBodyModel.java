package dev.xylonity.nomendubium.client.entity.model.body;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xylonity.nomendubium.client.render.ModularDinoRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import org.joml.Matrix4fc;

public abstract class ModularDinoBodyModel extends EntityModel<ModularDinoRenderState> {

    private final ModelPart body;
    private final ModelPart headConnection;
    private final ModelPart tailConnection;
    private final ModelPart backConnection;

    protected ModularDinoBodyModel(ModelPart root) {
        super(root);
        this.body = root.getChild("body");
        this.headConnection = this.body.getChild("head_connection");
        this.tailConnection = this.body.getChild("tail_connection");
        this.backConnection = this.body.getChild("extra_connection");
    }

    public void moveToHead(PoseStack poseStack) {
        this.moveTo(this.headConnection, poseStack);
    }

    public void moveToTail(PoseStack poseStack) {
        this.moveTo(this.tailConnection, poseStack);
    }

    public void moveToBack(PoseStack poseStack) {
        this.moveTo(this.backConnection, poseStack);
    }

    private void moveTo(ModelPart connection, PoseStack poseStack) {
        final PoseStack connectionPose = new PoseStack();
        this.body.translateAndRotate(connectionPose);
        connection.translateAndRotate(connectionPose);
        final Matrix4fc transform = connectionPose.last().pose();
        poseStack.translate(transform.m30(), transform.m31(), transform.m32());
    }

}