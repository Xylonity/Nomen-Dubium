package dev.xylonity.nomendubium.client.entity.model.chimera.skeleton.head;

import dev.xylonity.nomendubium.client.entity.model.chimera.ChimeraModelConnections;
import dev.xylonity.nomendubium.client.entity.render.skeleton.SkeletonPartRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class SnortingSkullModel extends EntityModel<SkeletonPartRenderState> {

    public SnortingSkullModel(ModelPart root) {
        super(root);
    }

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition entire_head = partdefinition.addOrReplaceChild("entire_head", CubeListBuilder.create(), ChimeraModelConnections.alignToConnection(0.0F, -12.5F, -20.0F));

		PartDefinition right_ear = entire_head.addOrReplaceChild("right_ear", CubeListBuilder.create(), PartPose.offset(-6.51F, -20.0F, -24.0F));

		PartDefinition left_ear = entire_head.addOrReplaceChild("left_ear", CubeListBuilder.create(), PartPose.offset(6.51F, -20.0F, -24.0F));

		PartDefinition nose = entire_head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(44, 26).addBox(-6.5F, -2.0F, -9.0F, 13.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -17.0F, -31.0F));

		PartDefinition lower_beak = entire_head.addOrReplaceChild("lower_beak", CubeListBuilder.create().texOffs(0, 26).addBox(-6.5F, -7.0F, -8.0F, 13.0F, 12.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, -32.0F));

		PartDefinition head = entire_head.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-6.5F, -7.5F, -11.5F, 13.0F, 15.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.5F, -19.5F));

		PartDefinition body_connection = entire_head.addOrReplaceChild("body_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -12.5F, -20.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

}
