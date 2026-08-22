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

public final class LankySkeletonModel extends EntityModel<SkeletonPartRenderState> {

    public LankySkeletonModel(ModelPart root) {
        super(root);
    }

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition torso = body.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 0).addBox(-11.0F, -14.0F, -17.0F, 22.0F, 11.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(0, 43).addBox(-1.0F, -16.0F, -18.0F, 2.0F, 3.0F, 47.0F, new CubeDeformation(0.0F))
		.texOffs(0, 93).addBox(0.0F, -26.0F, -17.0F, 0.0F, 10.0F, 44.0F, new CubeDeformation(0.0F))
		.texOffs(98, 52).addBox(-6.0F, -14.0F, 20.0F, 12.0F, 14.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -45.0F, 0.0F));

		PartDefinition L_Front_leg = body.addOrReplaceChild("L_Front_leg", CubeListBuilder.create().texOffs(88, 93).mirror().addBox(-3.4F, -1.0F, -3.6F, 7.0F, 45.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(116, 79).addBox(-3.5F, -15.0F, -12.6F, 0.0F, 14.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.6F, -44.0F, -10.4F));

		PartDefinition L_back_leg = body.addOrReplaceChild("L_back_leg", CubeListBuilder.create().texOffs(88, 93).mirror().addBox(-3.4F, -1.0F, -3.6F, 7.0F, 45.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-7.6F, -44.0F, 23.6F));

		PartDefinition R_back_leg = body.addOrReplaceChild("R_back_leg", CubeListBuilder.create().texOffs(88, 93).addBox(-3.6F, -1.0F, -3.6F, 7.0F, 45.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(7.6F, -44.0F, 23.6F));

		PartDefinition R_Front_leg = body.addOrReplaceChild("R_Front_leg", CubeListBuilder.create().texOffs(88, 93).addBox(-3.6F, -1.0F, -3.6F, 7.0F, 45.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(116, 79).addBox(3.5F, -15.0F, -12.6F, 0.0F, 14.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(7.6F, -44.0F, -10.4F));

		PartDefinition head_connection = body.addOrReplaceChild("head_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -59.5F, -18.0F));

		PartDefinition tail_connection = body.addOrReplaceChild("tail_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -59.5F, 29.0F));

		PartDefinition extra_connection = body.addOrReplaceChild("extra_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -67.0F, 2.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

}
