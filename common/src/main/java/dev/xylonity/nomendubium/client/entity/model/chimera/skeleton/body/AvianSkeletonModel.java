package dev.xylonity.nomendubium.client.entity.model.chimera.skeleton.body;

import dev.xylonity.nomendubium.client.entity.render.skeleton.SkeletonPartRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class AvianSkeletonModel extends EntityModel<SkeletonPartRenderState> {

    public AvianSkeletonModel(ModelPart root) {
        super(root);
    }

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition torso = body.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 0).addBox(-10.0F, -18.0F, -18.0F, 20.0F, 18.0F, 36.0F, new CubeDeformation(0.0F))
		.texOffs(76, 91).addBox(-10.0F, -21.0F, 6.0F, 20.0F, 10.0F, 12.0F, new CubeDeformation(0.05F))
		.texOffs(42, 93).addBox(0.0F, -21.0F, 4.0F, 0.0F, 18.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 54).addBox(-1.0F, -20.0F, -18.0F, 2.0F, 3.0F, 36.0F, new CubeDeformation(0.0F))
		.texOffs(76, 54).addBox(0.0F, -22.0F, -17.0F, 0.0F, 2.0F, 35.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -22.0F, 0.0F));

		PartDefinition R_arm = body.addOrReplaceChild("R_arm", CubeListBuilder.create().texOffs(92, 113).addBox(1.0F, -2.0F, 2.5F, 3.0F, 14.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(74, 113).addBox(0.0F, -3.0F, -2.5F, 4.0F, 15.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(106, 113).addBox(0.0F, 8.0F, -6.5F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(112, 37).addBox(0.0F, 12.0F, -6.5F, 4.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(10.1F, -27.0F, -11.5F));

		PartDefinition R_leg = body.addOrReplaceChild("R_leg", CubeListBuilder.create().texOffs(0, 93).addBox(-5.0F, -4.0F, -6.0F, 9.0F, 20.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(10.0F, -30.0F, 8.0F));

		PartDefinition R_calf = R_leg.addOrReplaceChild("R_calf", CubeListBuilder.create().texOffs(112, 0).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 20.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 14.0F, 5.0F));

		PartDefinition R_foot = R_calf.addOrReplaceChild("R_foot", CubeListBuilder.create().texOffs(112, 26).addBox(-5.0F, 0.0F, -6.0F, 10.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(112, 50).addBox(-5.0F, 0.1F, -8.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.9F, -2.0F));

		PartDefinition extra_connection = body.addOrReplaceChild("extra_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -44.0F, -4.0F));

		PartDefinition tail_connection = body.addOrReplaceChild("tail_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -40.5F, 18.0F));

		PartDefinition head_connection = body.addOrReplaceChild("head_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -40.5F, -18.0F));

		PartDefinition L_leg = body.addOrReplaceChild("L_leg", CubeListBuilder.create().texOffs(0, 93).mirror().addBox(-4.0F, -4.0F, -6.0F, 9.0F, 20.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-10.0F, -30.0F, 8.0F));

		PartDefinition L_calf = L_leg.addOrReplaceChild("L_calf", CubeListBuilder.create().texOffs(112, 0).mirror().addBox(-3.0F, -4.0F, -3.0F, 6.0F, 20.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.0F, 14.0F, 5.0F));

		PartDefinition L_foot = L_calf.addOrReplaceChild("L_foot", CubeListBuilder.create().texOffs(112, 26).mirror().addBox(-5.0F, 0.0F, -6.0F, 10.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(112, 50).mirror().addBox(-5.0F, 0.1F, -8.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 13.9F, -2.0F));

		PartDefinition L_arm = body.addOrReplaceChild("L_arm", CubeListBuilder.create().texOffs(92, 113).mirror().addBox(-4.0F, -2.0F, 2.5F, 3.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(74, 113).mirror().addBox(-4.0F, -3.0F, -2.5F, 4.0F, 15.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(106, 113).mirror().addBox(-4.0F, 8.0F, -6.5F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(112, 37).mirror().addBox(-4.0F, 12.0F, -6.5F, 4.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-10.1F, -27.0F, -11.5F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

}
