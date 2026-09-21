package dev.xylonity.nomendubium.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class SedimentBlock extends SnowLayerBlock {

    private boolean creativePlayerDestroying;

    public SedimentBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        level.scheduleTick(pos, this, 2);
        return state;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (FallingBlock.isFree(level.getBlockState(pos.below())) && pos.getY() >= level.getMinBuildHeight()) {
            final FallingBlockEntity fallingSediment = FallingBlockEntity.fall(level, pos, state);
            fallingSediment.disableDrop();
        }

    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        ;;
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        this.creativePlayerDestroying = player.isCreative();
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
        final int layers = state.getValue(LAYERS);
        final int destroyedLayers = this.creativePlayerDestroying ? 1 : 2;
        this.creativePlayerDestroying = false;

        if (layers > destroyedLayers) {
            level.setBlock(pos, state.setValue(LAYERS, layers - destroyedLayers), 3);
        }

    }

}