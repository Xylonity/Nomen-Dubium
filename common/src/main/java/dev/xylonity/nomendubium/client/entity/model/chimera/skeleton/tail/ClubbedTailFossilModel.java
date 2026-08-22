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

public final class ClubbedTailFossilModel extends EntityModel<SkeletonPartRenderState> {

    public ClubbedTailFossilModel(ModelPart root) {
        super(root);
    }

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition tail = partdefinition.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 48).addBox(-4.0F, -1.0F, -2.0F, 8.0F, 0.0F, 17.0F, new CubeDeformation(0.0F))
		.texOffs(48, 0).addBox(0.0F, -5.0F, -2.0F, 0.0F, 9.0F, 17.0F, new CubeDeformation(0.0F))
		.texOffs(48, 26).addBox(-1.0F, -3.0F, -2.0F, 2.0F, 4.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 2.0F));

		PartDefinition tail_tip = tail.addOrReplaceChild("tail_tip", CubeListBuilder.create().texOffs(50, 60).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(0, 65).addBox(0.0F, -3.0F, 0.0F, 0.0F, 6.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(50, 47).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 0.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.0F, -6.0F, 13.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 24).addBox(-6.0F, -6.0F, 13.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -1.0F, 15.0F));

		PartDefinition body_connection = partdefinition.addOrReplaceChild("body_connection", CubeListBuilder.create(), PartPose.offset(0.0F, 18.0F, -12.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

}
