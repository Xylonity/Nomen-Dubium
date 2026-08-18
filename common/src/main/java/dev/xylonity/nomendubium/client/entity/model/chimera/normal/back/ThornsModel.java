package dev.xylonity.nomendubium.client.entity.model.chimera.normal.back;

import dev.xylonity.nomendubium.client.entity.model.chimera.ChimeraModelConnections;
import dev.xylonity.nomendubium.client.entity.render.chimera.ChimeraRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class ThornsModel extends EntityModel<ChimeraRenderState> {

	public ThornsModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition thorns = partdefinition.addOrReplaceChild("thorns", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -7.0F, -18.0F, 0.0F, 7.0F, 37.0F, new CubeDeformation(0.0F))
		.texOffs(0, 44).addBox(5.0F, -4.0F, -16.0F, 0.0F, 4.0F, 34.0F, new CubeDeformation(0.0F))
		.texOffs(0, 44).mirror().addBox(-5.0F, -4.0F, -16.0F, 0.0F, 4.0F, 34.0F, new CubeDeformation(0.0F)).mirror(false), ChimeraModelConnections.alignToConnection(0.0F, 0.0F, 0.0F));

		PartDefinition body_connection = thorns.addOrReplaceChild("body_connection", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

}
