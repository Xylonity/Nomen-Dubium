package dev.xylonity.nomendubium.client;

import dev.xylonity.nomendubium.registry.NomenDubiumSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.util.Mth;

public final class AmberVisionClient {

    private static final long FADE_DURATION_MS = 2000;

    // Sync to the whisper sound effect duration
    private static final long DURATION_MS = 11136;
    private static final long INACTIVE = -1;

    private static long startedAtNanos = INACTIVE;
    private static SoundInstance whisper;
    private static Component message;

    private static void start(Component component) {
        final Minecraft minecraft = Minecraft.getInstance();
        if (whisper != null) {
            minecraft.getSoundManager().stop(whisper);
        }

        startedAtNanos = System.nanoTime();
        message = component;
        whisper = SimpleSoundInstance.forUI(NomenDubiumSounds.WHISPERS.get(), 1, 1);
        minecraft.getSoundManager().play(whisper);
    }

    public static boolean captureMessage(Component component) {
        if (!(component.getContents() instanceof TranslatableContents contents) || !contents.getKey().startsWith("vision.nomendubium.amber.")) {
            return false;
        }

        start(component);

        return true;
    }

    public static void tick() {
        if (startedAtNanos != INACTIVE && elapsedMillis() >= DURATION_MS) {
            finish();
        }

    }

    public static boolean isActive() {
        return startedAtNanos != INACTIVE && elapsedMillis() < DURATION_MS;
    }

    public static void extractOverlay(GuiGraphicsExtractor graphics) {
        if (startedAtNanos == INACTIVE) {
            return;
        }

        final long elapsed = elapsedMillis();
        if (elapsed >= DURATION_MS) {
            finish();
            return;
        }

        // Fade in
        final float darkness;
        if (elapsed < FADE_DURATION_MS) {
            darkness = clampedSmoothstep((float)elapsed / FADE_DURATION_MS);
        }
        // Fade out
        else if (elapsed > FADE_DURATION_MS + (DURATION_MS - 2L * FADE_DURATION_MS)) {
            darkness = clampedSmoothstep((float)(DURATION_MS - elapsed) / FADE_DURATION_MS);
        }
        // Stay
        else {
            darkness = 1.0F;
        }

        final int alpha = Mth.clamp((int)(darkness * 230F), 0, 230);
        if (alpha > 0) {
            graphics.nextStratum();
            // Black overlay
            graphics.fill(0, 0, graphics.guiWidth(), graphics.guiHeight(), alpha << 24);
            if (message != null) {
                // Text rendering
                final Minecraft minecraft = Minecraft.getInstance();
                final int textAlpha = Mth.clamp((int)(darkness * 255.0F), 0, 255);
                graphics.nextStratum();
                graphics.centeredText(minecraft.font, message, graphics.guiWidth() / 2, (graphics.guiHeight() - minecraft.font.lineHeight) / 2, textAlpha << 24 | 0xFFFFFF);
            }

        }

    }

    private static long elapsedMillis() {
        return (System.nanoTime() - startedAtNanos) / 1_000_000L;
    }

    private static void finish() {
        if (whisper != null) {
            Minecraft.getInstance().getSoundManager().stop(whisper);
            whisper = null;
        }

        startedAtNanos = INACTIVE;
        message = null;
    }

    private static float clampedSmoothstep(float value) {
        final float clamped = Mth.clamp(value, 0.0F, 1.0F);
        return clamped * clamped * (3.0F - 2.0F * clamped);
    }

}