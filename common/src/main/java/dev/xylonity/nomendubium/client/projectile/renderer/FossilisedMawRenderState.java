package dev.xylonity.nomendubium.client.projectile.renderer;

import net.minecraft.core.Direction;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.joml.Quaternionf;

public final class FossilisedMawRenderState extends EntityRenderState {
    public final ItemStackRenderState item = new ItemStackRenderState();
    public final Quaternionf rotation = new Quaternionf();
    public Direction impactDirection = Direction.UP;
    public boolean embedded;
    public float impactRoll;
    public float shake;
    public float spin;
}
