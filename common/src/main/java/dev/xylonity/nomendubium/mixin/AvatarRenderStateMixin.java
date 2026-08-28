package dev.xylonity.nomendubium.mixin;

import dev.xylonity.nomendubium.client.entity.render.chimera.ChimeraRiderRenderState;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AvatarRenderState.class)
public abstract class AvatarRenderStateMixin implements ChimeraRiderRenderState {

    @Unique
    private boolean nomendubium$mountedOnChimera;
    @Unique
    private boolean nomendubium$anchoredToChimera;

    @Override
    public boolean nomendubium$isMountedOnChimera() {
        return this.nomendubium$mountedOnChimera;
    }

    @Override
    public void nomendubium$setMountedOnChimera(boolean mountedOnChimera) {
        this.nomendubium$mountedOnChimera = mountedOnChimera;
    }

    @Override
    public boolean nomendubium$isAnchoredToChimera() {
        return this.nomendubium$anchoredToChimera;
    }

    @Override
    public void nomendubium$setAnchoredToChimera(boolean anchoredToChimera) {
        this.nomendubium$anchoredToChimera = anchoredToChimera;
    }

}
