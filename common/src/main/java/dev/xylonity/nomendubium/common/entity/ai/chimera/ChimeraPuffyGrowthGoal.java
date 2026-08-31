package dev.xylonity.nomendubium.common.entity.ai.chimera;

import dev.xylonity.nomendubium.common.entity.ChimeraEntity;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraBodyVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class ChimeraPuffyGrowthGoal extends Goal {

    private final ChimeraEntity chimera;

    private int cooldown;

    public ChimeraPuffyGrowthGoal(ChimeraEntity chimera) {
        this.chimera = chimera;
    }

    @Override
    public boolean canUse() {
        return this.chimera.getBodyVariant() == ChimeraBodyVariant.PUFFY;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void tick() {
        if (this.chimera.getDeltaMovement().horizontalDistanceSqr() < 0.0025 || this.cooldown-- > 0) {
            return;
        }

        this.cooldown = 200;

        final ServerLevel level = getServerLevel(this.chimera);
        final BlockPos center = this.chimera.blockPosition().below();
        for (int attempt = 0; attempt < 3; attempt++) {
            final BlockPos pos = center.offset(this.chimera.getRandom().nextInt(3) - 1, 0, this.chimera.getRandom().nextInt(3) - 1);
            final ItemStack boneMeal = new ItemStack(Items.BONE_MEAL);
            final boolean shouldGrow = BoneMealItem.growCrop(boneMeal, level, pos);
            final BlockPos growthPos = shouldGrow ? pos : pos.above();
            if (!shouldGrow && !BoneMealItem.growCrop(new ItemStack(Items.BONE_MEAL), level, growthPos)) {
                continue;
            }

            BoneMealItem.addGrowthParticles(level, growthPos, 8);
        }

    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

}