package dev.xylonity.nomendubium.common.entity.ai.chimera;

import dev.xylonity.nomendubium.common.entity.ChimeraEntity;
import dev.xylonity.nomendubium.registry.NomenDubiumBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.state.BlockState;

public final class ChimeraHostileRootsGoal extends Goal {

    private static final int ROOT_RADIUS = 8;

    private final ChimeraEntity chimera;

    private int cooldown = 200;

    public ChimeraHostileRootsGoal(ChimeraEntity chimera) {
        this.chimera = chimera;
    }

    @Override
    public boolean canUse() {
        return this.chimera.isHostile();
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void tick() {
        if (this.cooldown-- > 0) {
            return;
        }

        this.cooldown = 400 + this.chimera.getRandom().nextInt(201);
        final ServerLevel level = getServerLevel(this.chimera);
        final BlockPos center = this.chimera.blockPosition();
        int nearbyRoots = 0;
        for (final BlockPos pos : BlockPos.betweenClosed(center.offset(-ROOT_RADIUS, -2, -ROOT_RADIUS), center.offset(ROOT_RADIUS, 2, ROOT_RADIUS)
        )) {
            if (level.getBlockState(pos).is(NomenDubiumBlocks.ROOT_OF_LIFE.get()) && ++nearbyRoots >= 6) {
                return;
            }

        }

        final BlockState root = NomenDubiumBlocks.ROOT_OF_LIFE.get().defaultBlockState();
        for (int attempt = 0; attempt < 12; attempt++) {
            final BlockPos target = center.offset(
                this.chimera.getRandom().nextInt(ROOT_RADIUS * 2 + 1) - ROOT_RADIUS,
                this.chimera.getRandom().nextInt(4) - 2,
                this.chimera.getRandom().nextInt(ROOT_RADIUS * 2 + 1) - ROOT_RADIUS
            );
            if (level.isEmptyBlock(target) && root.canSurvive(level, target)) {
                level.setBlockAndUpdate(target, root);
                return;
            }

        }

    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

}