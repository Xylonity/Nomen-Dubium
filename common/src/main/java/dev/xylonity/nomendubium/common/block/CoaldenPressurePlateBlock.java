package dev.xylonity.nomendubium.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public final class CoaldenPressurePlateBlock extends PressurePlateBlock {

    private static final int FLAMMABILITY = 4;
    private static final int FIRE_SPREAD_SPEED = 1;

    public CoaldenPressurePlateBlock(BlockSetType blockSetType, Properties properties) {
        super(blockSetType, properties);
    }

    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return FLAMMABILITY;
    }

    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return FIRE_SPREAD_SPEED;
    }

}
