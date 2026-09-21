package dev.xylonity.nomendubium.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xylonity.nomendubium.common.entity.ChimeraEntity;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

    @Inject(method = "renderEntity", at = @At("HEAD"), cancellable = true)
    private void nomendubium$skipChimeraPassenger(Entity entity, double cameraX, double cameraY, double cameraZ, float partialTick, PoseStack poseStack, MultiBufferSource buffers, CallbackInfo callback) {
        if (entity instanceof Player && entity.getVehicle() instanceof ChimeraEntity) {
            callback.cancel();
        }

    }

}