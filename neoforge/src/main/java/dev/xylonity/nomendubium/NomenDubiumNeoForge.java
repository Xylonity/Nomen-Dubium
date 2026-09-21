package dev.xylonity.nomendubium;

import dev.xylonity.knightlib.api.config.ConfigComposer;
import dev.xylonity.knightlib.api.event.KnightLibEvents;
import dev.xylonity.nomendubium.config.NomenDubiumConfig;
import net.neoforged.fml.common.Mod;

@Mod(NomenDubium.MOD_ID)
public class NomenDubiumNeoForge {

    public NomenDubiumNeoForge() {
        ConfigComposer.registerConfig(NomenDubium.MOD_ID, NomenDubiumConfig.class);

        NomenDubium.init();

        KnightLibEvents.CLIENT.register("dev.xylonity.nomendubium.client.event.NomenDubiumClientEvents");
    }

}