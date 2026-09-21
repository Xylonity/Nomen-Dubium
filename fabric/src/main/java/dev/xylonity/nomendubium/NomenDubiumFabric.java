package dev.xylonity.nomendubium;

import dev.xylonity.knightlib.api.config.ConfigComposer;
import dev.xylonity.nomendubium.common.event.NomenDubiumFabricServerEvents;
import dev.xylonity.nomendubium.config.NomenDubiumConfig;
import net.fabricmc.api.ModInitializer;

public class NomenDubiumFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        ConfigComposer.registerConfig(NomenDubium.MOD_ID, NomenDubiumConfig.class);
        NomenDubium.init();
        NomenDubiumFabricServerEvents.init();
    }

}
