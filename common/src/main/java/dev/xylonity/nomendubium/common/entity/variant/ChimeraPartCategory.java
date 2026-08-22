package dev.xylonity.nomendubium.common.entity.variant;

import java.util.Locale;

public enum ChimeraPartCategory {
    BODY,
    HEAD,
    TAIL,
    BACK;

    public String serializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

}