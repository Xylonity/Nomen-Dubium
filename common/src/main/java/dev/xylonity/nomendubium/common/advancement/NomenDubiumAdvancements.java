package dev.xylonity.nomendubium.common.advancement;

import dev.xylonity.nomendubium.NomenDubium;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public final class NomenDubiumAdvancements {

    public static final String RESTORE_FOSSIL = "restore_fossil";
    public static final String ASSEMBLE_SKELETON = "assemble_skeleton";
    public static final String REVIVE_CHIMERA = "revive_chimera";
    public static final String RESTORE_RELIC = "restore_relic";

    public static void award(Player player, String path) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }

        final AdvancementHolder advancement = serverPlayer.server.getAdvancements().get(NomenDubium.of(path));
        if (advancement != null) {
            serverPlayer.getAdvancements().award(advancement, "completed");
        }

    }

}
