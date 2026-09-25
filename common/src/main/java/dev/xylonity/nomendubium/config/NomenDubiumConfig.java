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

    @ConfigEntry(
        category = "Paleontology Table",
        comment = "Total time in seconds to excavate an encased fossil before it breaks.",
        min = 5,
        max = 1600
    )
    public static int PALEONTOLOGY_TABLE_GAME_DURATION = 60;

    @ConfigEntry(
        category = "Paleontology Table",
        comment = "Countdown in seconds shown before the excavation begins.",
        min = 1,
        max = 30
    )
    public static int PALEONTOLOGY_TABLE_COUNTDOWN_DURATION = 3;

    @ConfigEntry(
        category = "Paleontology Table",
        comment = "Base duration in ticks of the first tool round.",
        note = "Rounds last up to twice their base duration while plenty of game time remains, and are then scaled by the round duration multiplier.",
        min = 1,
        max = 1200
    )
    public static int PALEONTOLOGY_TABLE_INITIAL_ROUND_DURATION = 90;

    @ConfigEntry(
        category = "Paleontology Table",
        comment = "Ticks removed from the base round duration on each new round.",
        min = 0,
        max = 1200
    )
    public static int PALEONTOLOGY_TABLE_ROUND_DURATION_DECREASE = 4;

    @ConfigEntry(
        category = "Paleontology Table",
        comment = "Minimum base duration in ticks a round can decrease to.",
        note = "This value should be less than or equal to the initial round duration.",
        min = 1,
        max = 1200
    )
    public static int PALEONTOLOGY_TABLE_MIN_ROUND_DURATION = 34;

    @ConfigEntry(
        category = "Paleontology Table",
        comment = "Extra ticks added to the base duration of chisel rounds.",
        min = 0,
        max = 1200
    )
    public static int PALEONTOLOGY_TABLE_CHISEL_ROUND_BONUS = 30;

    @ConfigEntry(
        category = "Paleontology Table",
        comment = "Multiplier applied to the final duration of every round.",
        min = 0.05,
        max = 4.0
    )
    public static float PALEONTOLOGY_TABLE_ROUND_DURATION_MULTIPLIER = 0.75F;

    @ConfigEntry(
        category = "Paleontology Table",
        comment = "Progress added by each chisel stroke.",
        note = "A fossil is fully excavated at 600 progress.",
        min = 1,
        max = 600
    )
    public static int PALEONTOLOGY_TABLE_CHISEL_PROGRESS = 13;

    @ConfigEntry(
        category = "Paleontology Table",
        comment = "Progress added by each hammer hit.",
        note = "A fossil is fully excavated at 600 progress.",
        min = 1,
        max = 600
    )
    public static int PALEONTOLOGY_TABLE_HAMMER_PROGRESS = 3;

    @ConfigEntry(
        category = "Paleontology Table",
        comment = "Progress added by each full brush circle.",
        note = "A fossil is fully excavated at 600 progress.",
        min = 1,
        max = 600
    )
    public static int PALEONTOLOGY_TABLE_BRUSH_PROGRESS = 7;

    @ConfigEntry(
        category = "Paleontology Table",
        comment = "Maximum chisel strokes that count towards progress in a single round.",
        min = 1,
        max = 1000
    )
    public static int PALEONTOLOGY_TABLE_CHISEL_MAX_ACTIONS = 15;

    @ConfigEntry(
        category = "Paleontology Table",
        comment = "Maximum hammer hits that count towards progress in a single round.",
        min = 1,
        max = 1000
    )
    public static int PALEONTOLOGY_TABLE_HAMMER_MAX_ACTIONS = 100;

    @ConfigEntry(
        category = "Paleontology Table",
        comment = "Maximum brush circles that count towards progress in a single round.",
        min = 1,
        max = 1000
    )
    public static int PALEONTOLOGY_TABLE_BRUSH_MAX_ACTIONS = 40;

}
