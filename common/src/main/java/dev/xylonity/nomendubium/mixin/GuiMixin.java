package dev.xylonity.nomendubium.mixin;

import dev.xylonity.nomendubium.client.AmberVisionClient;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class GuiMixin {

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void nomendubium$hideCrosshairDuringAmberVision(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
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
