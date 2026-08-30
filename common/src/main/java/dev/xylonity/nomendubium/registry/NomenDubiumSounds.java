package dev.xylonity.nomendubium.registry;

import dev.xylonity.nomendubium.NomenDubium;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public final class NomenDubiumSounds {

    public static void init() {
        ;;
    }

    public static final Supplier<SoundEvent> WHISPERS = NomenDubium.PLATFORM.registerSound("whispers");
    public static final Supplier<SoundEvent> CHIMERA_IDLE = NomenDubium.PLATFORM.registerSound("chimera_idle");
    public static final Supplier<SoundEvent> CHIMERA_DEATH = NomenDubium.PLATFORM.registerSound("chimera_death");
    public static final Supplier<SoundEvent> CHIMERA_ROAR = NomenDubium.PLATFORM.registerSound("chimera_roar");

}
