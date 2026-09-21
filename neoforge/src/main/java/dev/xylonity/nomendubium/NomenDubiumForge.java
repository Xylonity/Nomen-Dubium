package dev.xylonity.nomendubium;

import dev.xylonity.knightlib.api.config.ConfigComposer;
import dev.xylonity.knightlib.api.event.KnightLibEvents;
import dev.xylonity.nomendubium.config.NomenDubiumConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(NomenDubium.MOD_ID)
public class NomenDubiumForge {

    public NomenDubiumForge() {
        ConfigComposer.registerConfig(NomenDubium.MOD_ID, NomenDubiumConfig.class);

        NomenDubium.init();

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
            KnightLibEvents.CLIENT.register("dev.xylonity.nomendubium.client.event.NomenDubiumClientEvents")
        );

    }

}