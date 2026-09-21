package dev.xylonity.nomendubium.config;

import dev.xylonity.knightlib.api.config.AutoConfig;
import dev.xylonity.knightlib.api.config.ConfigEntry;

@AutoConfig(
    file = "nomendubium",
    title = "Nomen Dubium Config",
    description = "Common configuration for Nomen Dubium",
    accentColor = 0xFFD9A441
)
public final class NomenDubiumConfig {

    @ConfigEntry(
        category = "Regenerating Chop",
        comment = "Ticks required to restore one durability point.",
        min = 1,
        max = 1200
    )
    public static int REGENERATING_CHOP_REGENERATION_INTERVAL = 20;

    @ConfigEntry(
        category = "Maws",
        comment = "Damage dealt by the Fossilised Maw.",
        min = 0.0,
        max = 1024.0
    )
    public static float FOSSILISED_MAW_DAMAGE = 6.0F;

    @ConfigEntry(
        category = "Maws",
        comment = "Base damage dealt by the Prehistoric Maw.",
        min = 0.0,
        max = 1024.0
    )
    public static float PREHISTORIC_MAW_BASE_DAMAGE = 9.0F;

    @ConfigEntry(
        category = "Maws",
        comment = "Maximum damage dealt by the Prehistoric Maw.",
        note = "This value should be greater than or equal to the base damage.",
        min = 0.0,
        max = 1024.0
    )
    public static float PREHISTORIC_MAW_MAX_DAMAGE = 14.0F;

    @ConfigEntry(
        category = "Chimera",
        comment = "Movement speed of a Hulking Chimera.",
        min = 0.0,
        max = 1.0
    )
    public static double HULKING_CHIMERA_SPEED = 0.22;

    @ConfigEntry(
        category = "Chimera",
        comment = "Movement speed of a Shelled Chimera.",
        min = 0.0,
        max = 1.0
    )
    public static double SHELLED_CHIMERA_SPEED = 0.145;

    @ConfigEntry(
        category = "Chimera",
        comment = "Movement speed of an Avian Chimera.",
        min = 0.0,
        max = 1.0
    )
    public static double AVIAN_CHIMERA_SPEED = 0.36;

    @ConfigEntry(
        category = "Chimera",
        comment = "Movement speed of a Lanky Chimera.",
        min = 0.0,
        max = 1.0
    )
    public static double LANKY_CHIMERA_SPEED = 0.22;

    @ConfigEntry(
        category = "Chimera",
        comment = "Movement speed of a Puffy Chimera.",
        min = 0.0,
        max = 1.0
    )
    public static double PUFFY_CHIMERA_SPEED = 0.22;

    @ConfigEntry(
        category = "Chimera",
        comment = "Attack damage provided by the Crunching head before tail modifiers.",
        min = 0.0,
        max = 1024.0
    )
    public static double CRUNCHING_CHIMERA_HEAD_DAMAGE = 8.0;

    @ConfigEntry(
        category = "Chimera",
        comment = "Attack damage provided by the Shielded head before tail modifiers.",
        min = 0.0,
        max = 1024.0
    )
    public static double SHIELDED_CHIMERA_HEAD_DAMAGE = 7.0;

    @ConfigEntry(
        category = "Chimera",
        comment = "Attack damage provided by the Beaked head before tail modifiers.",
        min = 0.0,
        max = 1024.0
    )
    public static double BEAKED_CHIMERA_HEAD_DAMAGE = 6.0;

    @ConfigEntry(
        category = "Chimera",
        comment = "Damage dealt by the mounted Beaked head peck.",
        min = 0.0,
        max = 1024.0
    )
    public static float BEAKED_CHIMERA_PECK_DAMAGE = 3.5F;

    @ConfigEntry(
        category = "Chimera",
        comment = "Attack damage provided by the Snorting head before tail modifiers.",
        min = 0.0,
        max = 1024.0
    )
    public static double SNORTING_CHIMERA_HEAD_DAMAGE = 5.0;

}
