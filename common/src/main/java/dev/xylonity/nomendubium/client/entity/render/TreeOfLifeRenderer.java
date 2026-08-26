package dev.xylonity.nomendubium.client.entity.render;

import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.client.entity.model.TreeOfLifeModel;
import dev.xylonity.nomendubium.common.entity.TreeOfLifeEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

public final class TreeOfLifeRenderer extends LivingEntityRenderer<TreeOfLifeEntity, LivingEntityRenderState, TreeOfLifeModel> {

    private static final Identifier TEXTURE = NomenDubium.of("textures/entity/tree_of_life.png");

    public TreeOfLifeRenderer(EntityRendererProvider.Context context) {
        super(context, new TreeOfLifeModel(context.bakeLayer(TreeOfLifeModel.LAYER_LOCATION)), 1.25F);
    }

    @Override
    protected boolean shouldShowName(TreeOfLifeEntity entity, double distanceToCameraSq) {
        return false;
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState state) {
        return TEXTURE;
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

}
