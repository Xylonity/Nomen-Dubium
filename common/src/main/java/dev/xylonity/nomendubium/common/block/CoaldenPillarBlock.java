package dev.xylonity.nomendubium.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class CoaldenPillarBlock extends RotatedPillarBlock {

    private static final int FLAMMABILITY = 1;
    private static final int FIRE_SPREAD_SPEED = 1;

    public CoaldenPillarBlock(Properties properties) {
        super(properties);
    }

    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return FLAMMABILITY;
    }

    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return FIRE_SPREAD_SPEED;
    }

}
