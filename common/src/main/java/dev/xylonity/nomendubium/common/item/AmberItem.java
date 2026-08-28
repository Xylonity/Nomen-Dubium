package dev.xylonity.nomendubium.common.item;

import dev.xylonity.nomendubium.NomenDubium;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jspecify.annotations.NonNull;

public class AmberItem extends Item {

    private static final TagKey<Structure> LIFE_HOLLOWS = TagKey.create(Registries.STRUCTURE, NomenDubium.of("life_hollow"));

    public AmberItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull ItemStack finishUsingItem(@NonNull ItemStack stack, @NonNull Level level, @NonNull LivingEntity entity) {
        final ItemStack result = super.finishUsingItem(stack, level, entity);
        if (entity instanceof ServerPlayer player && level instanceof ServerLevel serverLevel) {
            sendVisionMessage(player, serverLevel);
        }

        return result;
    }

    private static void sendVisionMessage(ServerPlayer player, ServerLevel level) {
        final int variant = player.getRandom().nextInt(10);
        final Component message;
        if (variant >= 5) {
            final BlockPos hollow = level.findNearestMapStructure(LIFE_HOLLOWS, player.blockPosition(), 100, false);
            message = hollow == null ? Component.translatable("vision.nomendubium.amber.life_hollow_missing") : Component.translatable("vision.nomendubium.amber.4", hollow.getX(), hollow.getZ());
        }
        else {
            message = Component.translatable("vision.nomendubium.amber." + variant);
        }

        player.connection.send(new ClientboundSetTitleTextPacket(message));
    }

}
