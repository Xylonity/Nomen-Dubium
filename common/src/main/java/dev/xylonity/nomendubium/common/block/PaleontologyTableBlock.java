package dev.xylonity.nomendubium.common.block;

import dev.xylonity.knightlib.KnightLib;
import dev.xylonity.nomendubium.common.blockentity.PaleontologyTableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.NonNull;

public final class PaleontologyTableBlock extends BaseEntityBlock {

    public PaleontologyTableBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level instanceof ServerLevel && level.getBlockEntity(pos) instanceof PaleontologyTableBlockEntity table) {
            KnightLib.PLATFORM.openMenu((ServerPlayer) player, table, friendlyByteBuf -> friendlyByteBuf.writeBlockPos(pos));
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public @NonNull BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PaleontologyTableBlockEntity(pos, state);
    }

    @Override
    public @NonNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

}
