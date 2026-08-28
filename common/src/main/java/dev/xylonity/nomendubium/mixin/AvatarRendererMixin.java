package dev.xylonity.nomendubium.mixin;

import dev.xylonity.nomendubium.client.entity.render.chimera.ChimeraRiderRenderState;
import dev.xylonity.nomendubium.common.entity.ChimeraEntity;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class AvatarRendererMixin {

    @Inject(
        method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
        at = @At("TAIL")
    )
    private void nomendubium$markChimeraRider(Avatar avatar, AvatarRenderState state, float partialTick, CallbackInfo callback) {
        final ChimeraRiderRenderState riderState = (ChimeraRiderRenderState) state;
        riderState.nomendubium$setMountedOnChimera(avatar.getVehicle() instanceof ChimeraEntity);
        riderState.nomendubium$setAnchoredToChimera(false);
    }

}
