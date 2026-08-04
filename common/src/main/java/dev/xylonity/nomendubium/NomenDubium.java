package dev.xylonity.nomendubium;

import dev.xylonity.nomendubium.platform.NomenDubiumPlatform;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ServiceLoader;

public class NomenDubium {

    public static final String MOD_ID = "nomendubium";
    public static final Logger LOGGER = LoggerFactory.getLogger("Nomen Dubium");

    public static final NomenDubiumPlatform PLATFORM = ServiceLoader.load(NomenDubiumPlatform.class).findFirst().orElseThrow();

    public static void init() {
        ;;
    }

    public static Identifier of(final String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

}