package dev.xylonity.nomendubium.client.entity.model.back;

import dev.xylonity.nomendubium.client.entity.model.ChimeraModelConnections;
import dev.xylonity.nomendubium.client.render.ChimeraRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class SpikesModel extends EntityModel<ChimeraRenderState> {

	public SpikesModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition spikes = partdefinition.addOrReplaceChild("spikes", CubeListBuilder.create(), ChimeraModelConnections.alignToConnection(0.0F, 0.0F, 0.0F));

		PartDefinition R_top_spikes = spikes.addOrReplaceChild("R_top_spikes", CubeListBuilder.create().texOffs(16, 23).mirror().addBox(0.0F, -15.0F, -6.0F, 3.0F, 16.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 23).mirror().addBox(0.0F, -22.0F, -13.0F, 3.0F, 23.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(30, 23).mirror().addBox(0.0F, -9.0F, -19.0F, 3.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 0).mirror().addBox(5.0F, 1.001F, -13.0F, 30.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, -1.0F, 6.0F));

		PartDefinition L_top_spikes = spikes.addOrReplaceChild("L_top_spikes", CubeListBuilder.create().texOffs(16, 23).addBox(-3.0F, -15.0F, -6.0F, 3.0F, 16.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 23).addBox(-3.0F, -22.0F, -13.0F, 3.0F, 23.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(30, 23).addBox(-3.0F, -9.0F, -19.0F, 3.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -1.0F, 6.0F));

		PartDefinition L_side_spikes = spikes.addOrReplaceChild("L_side_spikes", CubeListBuilder.create().texOffs(0, 9).addBox(-21.0F, -1.999F, -6.0F, 22.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 16).addBox(-15.0F, -1.999F, 7.0F, 16.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-29.0F, -1.999F, 0.0F, 30.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-11.0F, 2.0F, -7.0F));

		PartDefinition R_side_spikes = spikes.addOrReplaceChild("R_side_spikes", CubeListBuilder.create().texOffs(0, 9).mirror().addBox(-1.0F, -1.999F, -6.0F, 22.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 16).mirror().addBox(-1.0F, -1.999F, 7.0F, 16.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(11.0F, 2.0F, -7.0F));

		PartDefinition body_connection = spikes.addOrReplaceChild("body_connection", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

}
