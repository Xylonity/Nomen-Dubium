package dev.xylonity.nomendubium.common.block;

import com.mojang.serialization.MapCodec;
import dev.xylonity.nomendubium.common.blockentity.GeologistTableBlockEntity;
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

public final class GeologistTableBlock extends BaseEntityBlock {

    public static final MapCodec<GeologistTableBlock> CODEC = simpleCodec(GeologistTableBlock::new);

    public GeologistTableBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<GeologistTableBlock> codec() {
        return CODEC;
    }

    @Override
    protected @NonNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level instanceof ServerLevel && level.getBlockEntity(pos) instanceof GeologistTableBlockEntity table) {
            player.openMenu(table);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public @NonNull BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GeologistTableBlockEntity(pos, state);
    }

}