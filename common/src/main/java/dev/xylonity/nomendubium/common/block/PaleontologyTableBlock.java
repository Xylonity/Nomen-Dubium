package dev.xylonity.nomendubium.common.block;

import com.mojang.serialization.MapCodec;
import dev.xylonity.nomendubium.common.blockentity.PaleontologyTableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.NonNull;

public final class PaleontologyTableBlock extends BaseEntityBlock {

    public static final MapCodec<PaleontologyTableBlock> CODEC = simpleCodec(PaleontologyTableBlock::new);

    public PaleontologyTableBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<PaleontologyTableBlock> codec() {
        return CODEC;
    }

    @Override
    protected @NonNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level instanceof ServerLevel && level.getBlockEntity(pos) instanceof PaleontologyTableBlockEntity table) {
            player.openMenu(table);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public @NonNull BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PaleontologyTableBlockEntity(pos, state);
    }

}