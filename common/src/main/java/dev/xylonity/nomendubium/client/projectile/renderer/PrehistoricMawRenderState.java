package dev.xylonity.nomendubium.client.projectile.renderer;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.joml.Quaternionf;

public final class PrehistoricMawRenderState extends EntityRenderState {
    public final ItemStackRenderState item = new ItemStackRenderState();
    public final Quaternionf rotation = new Quaternionf();
    public float bank;
    public float spin;
}