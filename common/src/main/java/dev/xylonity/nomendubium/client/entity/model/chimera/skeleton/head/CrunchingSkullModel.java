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

public final class CrunchingSkullModel extends EntityModel<SkeletonPartRenderState> {

    public CrunchingSkullModel(ModelPart root) {
        super(root);
    }

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition entire_head = partdefinition.addOrReplaceChild("entire_head", CubeListBuilder.create(), ChimeraModelConnections.alignToConnection(0.0F, -12.5F, 22.0F));

		PartDefinition head = entire_head.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -10.0F, 8.0F));

		PartDefinition Cranium = head.addOrReplaceChild("Cranium", CubeListBuilder.create().texOffs(0, 47).addBox(-3.0F, -19.0F, -29.0F, 6.0F, 5.0F, 30.0F, new CubeDeformation(0.0F))
		.texOffs(0, 82).addBox(-8.0F, -19.0F, 1.0F, 16.0F, 10.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(72, 47).addBox(-3.0F, -14.0F, -29.0F, 6.0F, 5.0F, 30.0F, new CubeDeformation(0.05F)), PartPose.offset(0.0F, 10.0F, -6.0F));

		PartDefinition Jaw = head.addOrReplaceChild("Jaw", CubeListBuilder.create().texOffs(42, 82).addBox(-8.0F, 0.0F, 0.0F, 16.0F, 6.0F, 5.0F, new CubeDeformation(0.05F))
		.texOffs(76, 23).addBox(-3.0F, 4.0F, -20.0F, 6.0F, 2.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(42, 93).addBox(-3.0F, 4.0F, -29.0F, 6.0F, 7.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-3.0F, -1.0F, -32.0F, 6.0F, 15.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -5.0F));

		PartDefinition neck = entire_head.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(84, 82).addBox(-1.0F, -4.0F, -14.0F, 2.0F, 3.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 97).addBox(0.0F, -11.0F, -13.0F, 0.0F, 7.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(76, 0).addBox(-9.0F, -2.0F, -13.0F, 18.0F, 12.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 22.0F));

		PartDefinition body_connection = entire_head.addOrReplaceChild("body_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -12.5F, 22.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

}
