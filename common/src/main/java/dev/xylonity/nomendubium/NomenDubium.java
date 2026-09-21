package dev.xylonity.nomendubium;

import dev.xylonity.knightlib.api.event.KnightLibEvents;
import dev.xylonity.knightlib.api.util.ResourceLocations;
import dev.xylonity.nomendubium.common.event.NomenDubiumServerEvents;
import dev.xylonity.nomendubium.registry.NomenDubiumBlockEntities;
import dev.xylonity.nomendubium.registry.NomenDubiumBlocks;
import dev.xylonity.nomendubium.registry.NomenDubiumCreativeTabs;
import dev.xylonity.nomendubium.registry.NomenDubiumEntities;
import dev.xylonity.nomendubium.registry.NomenDubiumItems;
import dev.xylonity.nomendubium.registry.NomenDubiumMenus;
import dev.xylonity.nomendubium.registry.NomenDubiumRecipes;
import dev.xylonity.nomendubium.registry.NomenDubiumSounds;
import dev.xylonity.nomendubium.registry.NomenDubiumWorldgen;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NomenDubium {

    public static final String MOD_ID = "nomendubium";
    public static final Logger LOGGER = LoggerFactory.getLogger("Nomen Dubium");

    public static void init() {
        NomenDubiumSounds.SOUNDS.init();
        NomenDubiumBlocks.BLOCKS.init();
        NomenDubiumItems.ITEMS.init();
        NomenDubiumRecipes.TYPES.init();
        NomenDubiumRecipes.SERIALIZERS.init();
        NomenDubiumEntities.ENTITIES.init();
        NomenDubiumBlockEntities.BLOCK_ENTITIES.init();
        NomenDubiumMenus.MENUS.init();
        NomenDubiumWorldgen.FEATURES.init();
        NomenDubiumWorldgen.STRUCTURE_TYPES.init();
        NomenDubiumWorldgen.STRUCTURE_TYPES.init();
        NomenDubiumCreativeTabs.CREATIVE_TABS.init();

        KnightLibEvents.SERVER.register(NomenDubiumServerEvents.class);
    }

    public static ResourceLocation of(final String path) {
        return ResourceLocations.of(MOD_ID, path);
    }

}