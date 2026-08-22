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

public final class SnarledSkullModel extends EntityModel<SkeletonPartRenderState> {

    public SnarledSkullModel(ModelPart root) {
        super(root);
    }

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition entire_head = partdefinition.addOrReplaceChild("entire_head", CubeListBuilder.create(), ChimeraModelConnections.alignToConnection(0.0F, -3.0F, 8.0F));

		PartDefinition Head = entire_head.addOrReplaceChild("Head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Cranium = Head.addOrReplaceChild("Cranium", CubeListBuilder.create().texOffs(0, 26).addBox(-9.0F, -5.0F, -20.0F, 17.0F, 5.0F, 20.0F, new CubeDeformation(0.0F))
		.texOffs(0, 51).addBox(-9.0F, 0.0F, -20.0F, 17.0F, 3.0F, 20.0F, new CubeDeformation(0.05F)), PartPose.offset(0.5F, -46.0F, 2.0F));

		PartDefinition Jaw = Head.addOrReplaceChild("Jaw", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -4.0F, -20.0F, 17.0F, 6.0F, 20.0F, new CubeDeformation(0.05F))
		.texOffs(0, 74).addBox(-9.0F, 2.0F, -20.0F, 17.0F, 2.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -46.0F, 2.0F));

		PartDefinition Neck = entire_head.addOrReplaceChild("Neck", CubeListBuilder.create().texOffs(86, 64).addBox(-2.0F, -5.0F, -1.0F, 4.0F, 4.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(74, 0).addBox(-2.0F, -42.0F, -5.0F, 4.0F, 41.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(74, 45).addBox(0.0F, -41.0F, -6.0F, 0.0F, 35.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(86, 45).addBox(0.0F, -6.0F, -6.0F, 0.0F, 6.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body_connection = entire_head.addOrReplaceChild("body_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -3.0F, 8.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

}
