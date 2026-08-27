package dev.xylonity.nomendubium.common.block;

import com.mojang.serialization.MapCodec;
import dev.xylonity.nomendubium.registry.NomenDubiumItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;

public final class RootOfLifeBlock extends VegetationBlock {

    public static final MapCodec<RootOfLifeBlock> CODEC = simpleCodec(RootOfLifeBlock::new);

    public RootOfLifeBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<RootOfLifeBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Block.column(16.0, 0.0, 13.0);
    }

    @Override
    protected @NonNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.SUCCESS;
        }

        final ItemStack root = new ItemStack(NomenDubiumItems.ROOT_OF_LIFE.get());
        if (!player.addItem(root)) {
            Block.popResource(serverLevel, pos, root);
        }

        serverLevel.playSound(null, pos, SoundEvents.ROOTS_BREAK, SoundSource.BLOCKS, 1.0F, 0.9F + serverLevel.getRandom().nextFloat() * 0.2F);
        serverLevel.removeBlock(pos, false);

        return InteractionResult.SUCCESS_SERVER;
    }

}