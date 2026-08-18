package dev.xylonity.nomendubium;

import dev.xylonity.nomendubium.platform.NomenDubiumPlatformNeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(NomenDubium.MOD_ID)
public class NomenDubiumNeoForge {

    public NomenDubiumNeoForge(final IEventBus eventBus) {
        NomenDubium.init();
        NomenDubiumPlatformNeoForge.register(eventBus);
    }

}
