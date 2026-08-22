package dev.xylonity.nomendubium.client.entity.model.chimera.skeleton.head;

import dev.xylonity.nomendubium.client.entity.model.chimera.ChimeraModelConnections;
import dev.xylonity.nomendubium.client.projectile.renderer.SkeletonPartRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class BeakedSkullModel extends EntityModel<SkeletonPartRenderState> {

    public BeakedSkullModel(ModelPart root) {
        super(root);
    }

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition entire_head = partdefinition.addOrReplaceChild("entire_head", CubeListBuilder.create(), ChimeraModelConnections.alignToConnection(0.0F, -5.5F, 7.0F));

		PartDefinition neck = entire_head.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(54, 10).addBox(-1.0F, -15.0F, -3.9F, 3.0F, 4.0F, 4.0F, new CubeDeformation(-0.05F))
		.texOffs(32, 64).addBox(-1.0F, -15.0F, 0.0F, 3.0F, 20.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(3, 61).addBox(0.5F, -17.0F, -3.0F, 0.0F, 23.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(32, 52).addBox(-1.0F, 0.0F, 4.0F, 3.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -8.0F, 1.0F));

		PartDefinition head = entire_head.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(-0.5F, -18.1F, -6.0F));

		PartDefinition cranium = head.addOrReplaceChild("cranium", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, 0.0F, -12.0F, 15.0F, 7.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(52, 19).addBox(-7.0F, 7.0F, -5.0F, 15.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 52).addBox(-2.0F, 0.0F, -23.0F, 5.0F, 6.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(46, 64).addBox(-2.0F, 0.0F, -27.0F, 5.0F, 10.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(52, 27).addBox(-4.0F, 6.0F, -22.0F, 2.0F, 0.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(54, 0).addBox(3.0F, 6.0F, -22.0F, 2.0F, 0.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 3.0F));

		PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(0, 37).addBox(-7.0F, -3.0F, -9.0F, 15.0F, 8.0F, 7.0F, new CubeDeformation(0.05F))
		.texOffs(44, 47).addBox(-5.0F, -3.9F, -20.0F, 3.0F, 4.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(0, 19).addBox(-5.0F, 0.0F, -22.0F, 11.0F, 3.0F, 15.0F, new CubeDeformation(0.0F))
		.texOffs(44, 37).addBox(-7.0F, 0.0F, -2.0F, 15.0F, 5.0F, 5.0F, new CubeDeformation(0.05F))
		.texOffs(44, 47).mirror().addBox(3.0F, -3.9F, -20.0F, 3.0F, 4.0F, 13.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body_connection = entire_head.addOrReplaceChild("body_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -5.5F, 7.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

}
