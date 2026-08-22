package dev.xylonity.nomendubium.client.entity.model.chimera.skeleton.back;

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

public final class BoneyPlatesFossilModel extends EntityModel<SkeletonPartRenderState> {

    public BoneyPlatesFossilModel(ModelPart root) {
        super(root);
    }

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition boney_plates = partdefinition.addOrReplaceChild("boney_plates", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -15.0F, -20.0F, 0.0F, 15.0F, 41.0F, new CubeDeformation(0.0F)), ChimeraModelConnections.alignToConnection(0.0F, 0.0F, 0.0F));

		PartDefinition body_connection = boney_plates.addOrReplaceChild("body_connection", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

}
