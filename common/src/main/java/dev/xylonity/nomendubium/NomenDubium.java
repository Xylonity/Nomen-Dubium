package dev.xylonity.nomendubium;

import dev.xylonity.nomendubium.platform.NomenDubiumPlatform;
import dev.xylonity.nomendubium.registry.NomenDubiumBlockEntities;
import dev.xylonity.nomendubium.registry.NomenDubiumBlocks;
import dev.xylonity.nomendubium.registry.NomenDubiumCreativeTabs;
import dev.xylonity.nomendubium.registry.NomenDubiumDataComponents;
import dev.xylonity.nomendubium.registry.NomenDubiumEntities;
import dev.xylonity.nomendubium.registry.NomenDubiumItems;
import dev.xylonity.nomendubium.registry.NomenDubiumMenus;
import dev.xylonity.nomendubium.registry.NomenDubiumRecipes;
import dev.xylonity.nomendubium.registry.NomenDubiumWorldgen;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ServiceLoader;

public class NomenDubium {

    public static final String MOD_ID = "nomendubium";
    public static final Logger LOGGER = LoggerFactory.getLogger("Nomen Dubium");

    public static final NomenDubiumPlatform PLATFORM = ServiceLoader.load(NomenDubiumPlatform.class).findFirst().orElseThrow();

    public static void init() {
        NomenDubiumDataComponents.init();
        NomenDubiumBlocks.init();
        NomenDubiumItems.init();
        NomenDubiumRecipes.init();
        NomenDubiumEntities.init();
        NomenDubiumBlockEntities.init();
        NomenDubiumMenus.init();
        NomenDubiumWorldgen.init();
        NomenDubiumCreativeTabs.init();
    }

    public static Identifier of(final String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

}
