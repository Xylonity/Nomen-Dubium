package dev.xylonity.nomendubium.common.entity.variant;

import java.util.Locale;

public interface ChimeraPartVariant {

    ChimeraPartCategory category();

    String name();

    default String fossilPart() {
        return name().toLowerCase(Locale.ROOT) + "_" + category().serializedName();
    }

}