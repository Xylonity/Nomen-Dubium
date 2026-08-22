package dev.xylonity.nomendubium.client.entity.model.chimera.skeleton.body;

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

public final class PuffySkeletonModel extends EntityModel<SkeletonPartRenderState> {

    public PuffySkeletonModel(ModelPart root) {
        super(root);
    }

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition front_left_leg = body.addOrReplaceChild("front_left_leg", CubeListBuilder.create().texOffs(0, 85).addBox(-3.5F, -1.0F, -4.0F, 7.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(50, 83).addBox(2.1F, -15.0F, -5.0F, 0.0F, 14.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(7.5F, -9.0F, -15.0F));

		PartDefinition middle_left_leg = body.addOrReplaceChild("middle_left_leg", CubeListBuilder.create().texOffs(0, 85).addBox(-3.5F, -1.0F, -4.0F, 7.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(30, 85).addBox(2.1F, -17.0F, -4.0F, 0.0F, 16.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(7.5F, -9.0F, 0.0F));

		PartDefinition back_left_leg = body.addOrReplaceChild("back_left_leg", CubeListBuilder.create().texOffs(0, 85).addBox(-3.5F, -1.0F, -4.0F, 7.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(82, 84).addBox(-6.5F, -24.0F, -6.0F, 7.0F, 23.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(7.5F, -9.0F, 15.0F));

		PartDefinition torso = body.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 0).addBox(-9.5F, -12.0F, -20.0F, 19.0F, 16.0F, 27.0F, new CubeDeformation(0.0F))
		.texOffs(0, 43).addBox(-1.0F, -14.0F, -20.0F, 2.0F, 3.0F, 39.0F, new CubeDeformation(0.0F))
		.texOffs(82, 43).addBox(0.0F, -16.0F, -20.0F, 0.0F, 2.0F, 39.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -19.0F, 0.0F));

		PartDefinition extra_connection = body.addOrReplaceChild("extra_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -35.0F, 0.0F));

		PartDefinition tail_connection = body.addOrReplaceChild("tail_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -31.5F, 20.0F));

		PartDefinition head_connection = body.addOrReplaceChild("head_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -31.5F, -20.0F));

		PartDefinition front_right_leg = body.addOrReplaceChild("front_right_leg", CubeListBuilder.create().texOffs(50, 83).mirror().addBox(-2.1F, -15.0F, -5.0F, 0.0F, 14.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 85).mirror().addBox(-3.5F, -1.0F, -4.0F, 7.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-7.5F, -9.0F, -15.0F));

		PartDefinition middle_right_leg = body.addOrReplaceChild("middle_right_leg", CubeListBuilder.create().texOffs(0, 85).mirror().addBox(-3.5F, -1.0F, -4.0F, 7.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(30, 85).mirror().addBox(-2.1F, -17.0F, -4.0F, 0.0F, 16.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-7.5F, -9.0F, 0.0F));

		PartDefinition back_right_leg = body.addOrReplaceChild("back_right_leg", CubeListBuilder.create().texOffs(0, 85).mirror().addBox(-3.5F, -1.0F, -4.0F, 7.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(82, 84).mirror().addBox(-0.5F, -24.0F, -6.0F, 7.0F, 23.0F, 11.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-7.5F, -9.0F, 15.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

}
