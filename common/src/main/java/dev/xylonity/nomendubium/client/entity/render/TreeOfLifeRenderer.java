package dev.xylonity.nomendubium.client.entity.render;

import dev.xylonity.knightlib.client.animation.KnightLibModelSource;
import dev.xylonity.knightlib.client.animation.layer.impl.KnightLibEmissiveLayer;
import dev.xylonity.knightlib.client.animation.renderer.KnightLibMobRenderer;
import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.common.entity.TreeOfLifeEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class TreeOfLifeRenderer extends KnightLibMobRenderer<TreeOfLifeEntity> {

    private static final ResourceLocation TEXTURE = NomenDubium.of("textures/entity/tree_of_life.png");

    public TreeOfLifeRenderer(EntityRendererProvider.Context context) {
        super(context, 1.25F);
        addRenderLayer(new KnightLibEmissiveLayer<>(
            treeOfLifeEntity -> TEXTURE,
            (target, renderTime) -> {
                final float brightness = 0.65F + 0.35F * Mth.sin((float)renderTime * 0.08F);
                final int alpha = Mth.clamp((int)(brightness * 255.0F), 0, 255);
                return alpha << 24 | 0xFFFFFF;
            }

        ));

    }

    @Override
    protected KnightLibModelSource defineModel(TreeOfLifeEntity entity) {
        return KnightLibModelSource.geo(NomenDubium.of("geo/tree_of_life.geo.json"));
    }

    @Override
    public ResourceLocation getTextureLocation(TreeOfLifeEntity treeOfLifeEntity) {
        return TEXTURE;
    }

}
