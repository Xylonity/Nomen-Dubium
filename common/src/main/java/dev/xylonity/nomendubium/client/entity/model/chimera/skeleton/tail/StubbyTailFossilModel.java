package dev.xylonity.nomendubium.client.entity.model.chimera.skeleton.tail;

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

public final class StubbyTailFossilModel extends EntityModel<SkeletonPartRenderState> {

    public StubbyTailFossilModel(ModelPart root) {
        super(root);
    }

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition tail = partdefinition.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(26, 43).addBox(0.0F, -8.0F, 7.0F, 0.0F, 6.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(0, 13).addBox(-3.0F, -6.0F, 7.0F, 6.0F, 0.0F, 13.0F, new CubeDeformation(0.0F)), ChimeraModelConnections.alignToConnection(0.0F, -5.0F, -6.0F));

		PartDefinition tail_tip = tail.addOrReplaceChild("tail_tip", CubeListBuilder.create().texOffs(42, 0).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 2.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(26, 26).addBox(-1.0F, -3.0F, -14.0F, 2.0F, 4.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(0, 26).addBox(0.0F, -6.0F, -14.0F, 0.0F, 10.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.0F, -1.0F, -14.0F, 8.0F, 0.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 8.0F));

		PartDefinition body_connection = tail.addOrReplaceChild("body_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -5.0F, -6.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

}
