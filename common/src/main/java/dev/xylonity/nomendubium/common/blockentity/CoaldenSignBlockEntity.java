package dev.xylonity.nomendubium.common.blockentity;

import dev.xylonity.nomendubium.registry.NomenDubiumBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public final class CoaldenSignBlockEntity extends SignBlockEntity {

    public CoaldenSignBlockEntity(BlockPos pos, BlockState state) {
        super(NomenDubiumBlockEntities.COALDEN_SIGN.get(), pos, state);
    }

}
