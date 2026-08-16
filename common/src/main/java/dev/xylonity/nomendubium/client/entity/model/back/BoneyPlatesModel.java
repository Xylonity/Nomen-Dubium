package dev.xylonity.nomendubium.client.entity.model.back;

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

public final class BoneyPlatesModel extends EntityModel<ModularCreatureRenderState> {

	public BoneyPlatesModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition boney_plates = partdefinition.addOrReplaceChild("boney_plates", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -15.0F, -20.0F, 0.0F, 15.0F, 41.0F, new CubeDeformation(0.0F)), ModelConnections.alignExtra(0.0F, 0.0F, 0.0F));

		PartDefinition body_connection = boney_plates.addOrReplaceChild("body_connection", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

}
