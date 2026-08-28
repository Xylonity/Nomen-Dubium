package dev.xylonity.nomendubium.registry;

import dev.xylonity.nomendubium.NomenDubium;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public final class NomenDubiumSounds {

    public static void init() {
        ;;
    }

    public static final Supplier<SoundEvent> WHISPERS = NomenDubium.PLATFORM.registerSound("whispers");

}
