package dev.xylonity.nomendubium.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class SedimentBlock extends SnowLayerBlock {

    public SedimentBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        tickAccess.scheduleTick(pos, this, 2);
        return state;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (FallingBlock.isFree(level.getBlockState(pos.below())) && pos.getY() >= level.getMinY()) {
            final FallingBlockEntity fallingSediment = FallingBlockEntity.fall(level, pos, state);
            fallingSediment.disableDrop();
        }
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        final BlockState destroyedState = super.playerWillDestroy(level, pos, state, player);
        return player.isCreative() ? destroyedState.setValue(LAYERS, 1) : destroyedState;
    }

    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
        final int layers = state.getValue(LAYERS);
        if (layers > 2) {
            level.setBlock(pos, state.setValue(LAYERS, layers - 2), 3);
        }

    }

}
