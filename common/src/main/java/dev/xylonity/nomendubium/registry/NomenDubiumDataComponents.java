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

    private static Supplier<DataComponentType<String>> registerString(String name) {
        return NomenDubium.PLATFORM.registerDataComponent(name, () -> DataComponentType.<String>builder()
            .persistent(Codec.STRING)
            .networkSynchronized(ByteBufCodecs.STRING_UTF8)
            .build()
        );

    }

}