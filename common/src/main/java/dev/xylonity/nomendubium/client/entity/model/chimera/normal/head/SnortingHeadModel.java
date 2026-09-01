package dev.xylonity.nomendubium.client.entity.model.chimera.normal.head;

import dev.xylonity.nomendubium.client.entity.model.chimera.ChimeraModelConnections;
import dev.xylonity.nomendubium.client.entity.render.chimera.ChimeraRenderState;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public final class SnortingHeadModel extends ChimeraHeadModel {

	public SnortingHeadModel(ModelPart root) {
		super(root, "head_control", 1.0F);
	}

	@Override
	public void setupAnim(ChimeraRenderState state) {
		super.setupAnim(state);
		final float progress = Mth.clamp(state.snortingExtractionProgress, 0.0F, 1.0F);
		final float sin = Mth.sin(progress * Mth.PI);
		final float phase = progress * Mth.PI * 15;
		this.head.yRot += Mth.sin(phase) * 0.52F * sin;
		this.head.zRot += Mth.cos(phase * 0.6F) * 0.14F * sin;
		this.head.xRot += Mth.abs(Mth.sin(phase * 0.5F)) * 0.08F * sin;
	}

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition entire_head = partdefinition.addOrReplaceChild(
			"entire_head",
			CubeListBuilder.create(),
			ChimeraModelConnections.alignToConnection(0.0F, -12.0F, -20.0F)
		);

		PartDefinition head_control = entire_head.addOrReplaceChild("head_control", CubeListBuilder.create(), PartPose.offset(0.0F, -12.0F, -20.0F));

		PartDefinition right_ear = head_control.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(0, 50).addBox(-1.0F, 0.0F, -3.0F, 1.0F, 19.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.51F, -8.0F, -4.0F));

		PartDefinition left_ear = head_control.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(48, 0).addBox(0.0F, 0.0F, -3.0F, 1.0F, 19.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(6.51F, -8.0F, -4.0F));

		PartDefinition nose = head_control.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(44, 40).addBox(-6.5F, -2.0F, -9.0F, 13.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, -11.0F));

		PartDefinition lower_beak = head_control.addOrReplaceChild("lower_beak", CubeListBuilder.create().texOffs(0, 29).addBox(-6.5F, -7.0F, -8.0F, 13.0F, 12.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, -12.0F));

		PartDefinition head = head_control.addOrReplaceChild("head", CubeListBuilder.create().texOffs(44, 29).addBox(-6.5F, 7.5F, -11.5F, 13.0F, 0.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.5F, -7.5F, -11.5F, 13.0F, 18.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.5F));

		PartDefinition body_connection = entire_head.addOrReplaceChild("body_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -12.0F, -20.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

}
