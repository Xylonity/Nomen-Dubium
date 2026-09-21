package dev.xylonity.nomendubium.registry;

import dev.xylonity.knightlib.api.registrar.ResourceDispatcher;
import dev.xylonity.knightlib.api.registrar.ResourceEntry;
import dev.xylonity.knightlib.api.registrar.ResourceRegistry;
import dev.xylonity.nomendubium.NomenDubium;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

public final class NomenDubiumSounds {

    public static final ResourceRegistry<SoundEvent> SOUNDS = ResourceDispatcher.create(BuiltInRegistries.SOUND_EVENT, NomenDubium.MOD_ID);

    public static final ResourceEntry<SoundEvent> WHISPERS = register("whispers");
    public static final ResourceEntry<SoundEvent> CHIMERA_IDLE = register("chimera_idle");
    public static final ResourceEntry<SoundEvent> CHIMERA_DEATH = register("chimera_death");
    public static final ResourceEntry<SoundEvent> CHIMERA_ROAR = register("chimera_roar");

    private static ResourceEntry<SoundEvent> register(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(NomenDubium.of(name)));
    }

}