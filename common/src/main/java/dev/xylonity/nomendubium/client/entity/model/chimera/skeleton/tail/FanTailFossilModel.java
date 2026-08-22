package dev.xylonity.nomendubium.client.entity.model.chimera.skeleton.tail;

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

public final class FanTailFossilModel extends EntityModel<SkeletonPartRenderState> {

    public FanTailFossilModel(ModelPart root) {
        super(root);
    }

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition tail = partdefinition.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(18, 29).addBox(-2.0F, -3.0F, -1.0F, 4.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 29).addBox(0.0F, -6.0F, -1.0F, 0.0F, 5.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(36, 29).addBox(2.0F, -2.0F, 4.0F, 0.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(36, 29).addBox(-2.0F, -2.0F, 4.0F, 0.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), ChimeraModelConnections.alignToConnection(0.0F, -2.0F, -1.0F));

		PartDefinition fan = tail.addOrReplaceChild("fan", CubeListBuilder.create().texOffs(0, 0).addBox(-14.0F, 0.0F, -3.0F, 28.0F, 0.0F, 29.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 4.0F));

		PartDefinition body_connection = tail.addOrReplaceChild("body_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, -1.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

}
