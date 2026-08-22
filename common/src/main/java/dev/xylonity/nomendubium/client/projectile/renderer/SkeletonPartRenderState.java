package dev.xylonity.nomendubium.client.projectile.renderer;

import dev.xylonity.nomendubium.common.entity.skeleton.SkeletonPartType;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public final class SkeletonPartRenderState extends EntityRenderState {
    public SkeletonPartType partType = SkeletonPartType.HULKING_BODY;
    public float yRot;
}
