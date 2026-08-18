package dev.xylonity.nomendubium;

import dev.xylonity.nomendubium.client.event.NomenDubiumFabricClientEvents;
import net.fabricmc.api.ClientModInitializer;

public class NomenDubiumFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        NomenDubiumFabricClientEvents.init();
    }

}
