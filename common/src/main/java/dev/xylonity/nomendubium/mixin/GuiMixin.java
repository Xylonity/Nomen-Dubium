package dev.xylonity.nomendubium.mixin;

import dev.xylonity.nomendubium.client.AmberVisionClient;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class GuiMixin {

    @Inject(method = "tick(Z)V", at = @At("TAIL"))
    private void nomendubium$tickAmberVision(boolean paused, CallbackInfo ci) {
        AmberVisionClient.tick();
    }

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void nomendubium$extractAmberVision(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        AmberVisionClient.extractOverlay(graphics);
    }

    @Inject(method = "extractCrosshair", at = @At("HEAD"), cancellable = true)
    private void nomendubium$hideCrosshairDuringAmberVision(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (AmberVisionClient.isActive()) {
            ci.cancel();
        }

    }

    @Inject(method = "setTitle", at = @At("HEAD"), cancellable = true)
    private void nomendubium$captureAmberVisionMessage(Component component, CallbackInfo ci) {
        if (AmberVisionClient.captureMessage(component)) {
            ci.cancel();
        }

    }

}
