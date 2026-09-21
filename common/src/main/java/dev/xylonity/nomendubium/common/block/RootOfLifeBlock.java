package dev.xylonity.nomendubium.common.block;

import dev.xylonity.nomendubium.registry.NomenDubiumItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;

public final class RootOfLifeBlock extends BushBlock {

    public RootOfLifeBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Block.box(0.0, 0.0, 0.0, 16.0, 13.0, 16.0);
    }

    @Override
    public @NonNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.SUCCESS;
        }

        final ItemStack root = new ItemStack(NomenDubiumItems.ROOT_OF_LIFE.get());
        if (!player.addItem(root)) {
            Block.popResource(serverLevel, pos, root);
        }

        serverLevel.playSound(null, pos, SoundEvents.ROOTS_BREAK, SoundSource.BLOCKS, 1.0F, 0.9F + serverLevel.getRandom().nextFloat() * 0.2F);
        serverLevel.removeBlock(pos, false);

        return InteractionResult.CONSUME;
    }

}