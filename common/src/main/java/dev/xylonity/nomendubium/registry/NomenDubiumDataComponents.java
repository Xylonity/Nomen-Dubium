package dev.xylonity.nomendubium.registry;

import com.mojang.serialization.Codec;
import dev.xylonity.nomendubium.NomenDubium;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;

import java.util.function.Supplier;

public final class NomenDubiumDataComponents {

    public static void init() {
        ;;
    }

    // Decided when the encased fossil is inserted into paleontology table
    public static final Supplier<DataComponentType<String>> FOSSIL_CATEGORY = registerString("fossil_category");
    // Decided by the fossil (only meant for chimera parts)
    public static final Supplier<DataComponentType<String>> FOSSIL_PART = registerString("fossil_part");

    public static final Supplier<DataComponentType<String>> CHIMERA_PALETTE = registerString("chimera_palette");

    public static final Supplier<DataComponentType<Float>> PREHISTORIC_MAW_DAMAGE = NomenDubium.PLATFORM.registerDataComponent("prehistoric_maw_damage", () -> DataComponentType.<Float>builder()
        .persistent(Codec.FLOAT)
        .networkSynchronized(ByteBufCodecs.FLOAT)
        .build()
    );
    public static final Supplier<DataComponentType<Long>> PREHISTORIC_MAW_LAST_DECAY_TICK = NomenDubium.PLATFORM.registerDataComponent("prehistoric_maw_last_decay_tick", () -> DataComponentType.<Long>builder()
        .persistent(Codec.LONG)
        .networkSynchronized(ByteBufCodecs.VAR_LONG)
        .build()
    );

    private static Supplier<DataComponentType<String>> registerString(String name) {
        return NomenDubium.PLATFORM.registerDataComponent(name, () -> DataComponentType.<String>builder()
            .persistent(Codec.STRING)
            .networkSynchronized(ByteBufCodecs.STRING_UTF8)
            .build()
        );

    }

}
