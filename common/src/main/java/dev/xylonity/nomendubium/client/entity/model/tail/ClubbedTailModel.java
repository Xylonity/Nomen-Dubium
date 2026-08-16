package dev.xylonity.nomendubium.client.entity.model.tail;

import dev.xylonity.nomendubium.client.model.ModelConnections;
import dev.xylonity.nomendubium.client.render.ModularCreatureRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class ClubbedTailModel extends EntityModel<ModularCreatureRenderState> {

	public ClubbedTailModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition tail = partdefinition.addOrReplaceChild(
			"tail",
			CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -5.0F, -2.0F, 10.0F, 10.0F, 17.0F, new CubeDeformation(0.0F)),
			PartPose.offset(0.0F, -7.0F, 26.0F)
		);

		PartDefinition tail_tip = tail.addOrReplaceChild("tail_tip", CubeListBuilder.create().texOffs(0, 51).addBox(-3.0F, -3.0F, -4.0F, 6.0F, 6.0F, 17.0F, new CubeDeformation(0.0F))
		.texOffs(0, 27).addBox(-6.0F, -6.0F, 13.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(48, 27).addBox(-6.0F, -6.0F, 13.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -1.0F, 15.0F));

		PartDefinition body_connection = partdefinition.addOrReplaceChild("body_connection", CubeListBuilder.create(), PartPose.offset(0.0F, 20.0F, -12.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

}
