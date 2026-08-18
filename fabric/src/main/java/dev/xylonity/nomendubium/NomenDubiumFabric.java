package dev.xylonity.nomendubium;

import dev.xylonity.nomendubium.common.event.NomenDubiumFabricServerEvents;
import net.fabricmc.api.ModInitializer;

public class NomenDubiumFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        NomenDubium.init();
        NomenDubiumFabricServerEvents.init();
    }

}
