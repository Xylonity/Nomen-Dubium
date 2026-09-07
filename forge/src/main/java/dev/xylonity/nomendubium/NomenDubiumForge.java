package dev.xylonity.nomendubium;

import dev.xylonity.nomendubium.common.event.NomenDubiumForgeEvents;
import dev.xylonity.nomendubium.platform.NomenDubiumPlatformForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(NomenDubium.MOD_ID)
public class NomenDubiumForge {

    public NomenDubiumForge() {
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        NomenDubium.init();
        NomenDubiumPlatformForge.register(eventBus);
        eventBus.addListener(NomenDubiumForgeEvents::registerEntityAttributes);
    }

}
