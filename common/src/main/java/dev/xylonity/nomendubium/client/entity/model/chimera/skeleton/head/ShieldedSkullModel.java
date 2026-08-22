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

public final class ShieldedSkullModel extends EntityModel<SkeletonPartRenderState> {

    public ShieldedSkullModel(ModelPart root) {
        super(root);
    }

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition entire_head = partdefinition.addOrReplaceChild("entire_head", CubeListBuilder.create(), ChimeraModelConnections.alignToConnection(0.0F, -24.5F, 16.0F));

		PartDefinition Neck = entire_head.addOrReplaceChild("Neck", CubeListBuilder.create().texOffs(0, 63).addBox(-1.0F, -26.0F, 3.0F, 2.0F, 3.0F, 13.0F, new CubeDeformation(0.05F))
		.texOffs(0, 79).addBox(-7.0F, -24.0F, 4.0F, 14.0F, 17.0F, 11.0F, new CubeDeformation(0.05F))
		.texOffs(32, 51).addBox(0.0F, -28.0F, 3.0F, 0.0F, 2.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Head = entire_head.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -23.0F, -13.0F, 16.0F, 9.0F, 14.0F, new CubeDeformation(0.05F))
		.texOffs(50, 40).addBox(5.0F, -14.0F, -13.0F, 3.0F, 8.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(24, 51).addBox(5.0F, -14.0F, -3.0F, 3.0F, 8.0F, 4.0F, new CubeDeformation(0.05F))
		.texOffs(60, 0).addBox(5.0F, -14.0F, -7.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(50, 40).mirror().addBox(-8.0F, -14.0F, -13.0F, 3.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(60, 0).mirror().addBox(-8.0F, -14.0F, -7.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(24, 51).mirror().addBox(-8.0F, -14.0F, -3.0F, 3.0F, 8.0F, 4.0F, new CubeDeformation(0.05F)).mirror(false), PartPose.offset(0.0F, -7.0F, 2.0F));

		PartDefinition Jaw = Head.addOrReplaceChild("Jaw", CubeListBuilder.create().texOffs(0, 23).addBox(-8.0F, 0.0F, -14.0F, 16.0F, 3.0F, 14.0F, new CubeDeformation(0.05F))
		.texOffs(38, 54).mirror().addBox(-8.0F, -6.0F, -8.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(24, 40).addBox(-5.0F, -8.0F, -14.0F, 10.0F, 8.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(38, 54).addBox(5.0F, -6.0F, -8.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 1.0F));

		PartDefinition L_plate = Head.addOrReplaceChild("L_plate", CubeListBuilder.create().texOffs(0, 40).mirror().addBox(-12.0F, -23.0F, -2.0F, 9.0F, 20.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(60, 6).mirror().addBox(-13.0F, -23.0F, -0.6F, 1.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(52, 54).mirror().addBox(-7.0F, -27.0F, -2.0F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.0F, -3.0F, 0.0F));

		PartDefinition R_plate = Head.addOrReplaceChild("R_plate", CubeListBuilder.create().texOffs(0, 40).addBox(4.0F, -23.0F, -2.0F, 9.0F, 20.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(60, 6).addBox(13.0F, -23.0F, -0.6F, 1.0F, 11.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(52, 54).addBox(4.0F, -27.0F, -2.0F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 0.0F));

		PartDefinition body_connection = entire_head.addOrReplaceChild("body_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -24.5F, 16.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

}
