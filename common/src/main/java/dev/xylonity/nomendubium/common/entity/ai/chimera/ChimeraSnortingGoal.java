package dev.xylonity.nomendubium.common.entity.ai.chimera;

import dev.xylonity.nomendubium.common.entity.ChimeraEntity;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraHeadVariant;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

public final class ChimeraSnortingGoal extends Goal {

    private final ChimeraEntity chimera;

    private int cooldown = 200;

    public ChimeraSnortingGoal(ChimeraEntity chimera) {
        this.chimera = chimera;
    }

    @Override
    public boolean canUse() {
        return this.chimera.getHeadVariant() == ChimeraHeadVariant.SNORTING;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void tick() {
        if (this.chimera.getTarget() != null || !this.chimera.onGround() || this.cooldown-- > 0) {
            return;
        }

        final ServerLevel level = getServerLevel(this.chimera);
        final BlockState ground = level.getBlockState(this.chimera.blockPosition().below());
        if (!ground.is(BlockTags.DIRT) && !ground.is(BlockTags.BASE_STONE_OVERWORLD)) {
            this.cooldown = 100;
            return;
        }

        this.cooldown = 500 + this.chimera.getRandom().nextInt(301);
        final int random = this.chimera.getRandom().nextInt(100);
        final Item resource = random < 50 ? Items.FLINT
            : random < 75 ? Items.COAL
            : random < 90 ? Items.RAW_COPPER
            : random < 98 ? Items.RAW_IRON
            : Items.RAW_GOLD;

        this.chimera.spawnAtLocation(level, resource);
        this.chimera.playSound(SoundEvents.SNIFFER_DIGGING, 1.0F, 0.85F + this.chimera.getRandom().nextFloat() * 0.2F);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

}
