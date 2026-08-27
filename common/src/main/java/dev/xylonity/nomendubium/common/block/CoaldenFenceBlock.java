package dev.xylonity.nomendubium.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class CoaldenFenceBlock extends FenceBlock {

    private static final int FLAMMABILITY = 4;
    private static final int FIRE_SPREAD_SPEED = 1;

    public CoaldenFenceBlock(Properties properties) {
        super(properties);
    }

    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return FLAMMABILITY;
    }

    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return FIRE_SPREAD_SPEED;
    }

}
