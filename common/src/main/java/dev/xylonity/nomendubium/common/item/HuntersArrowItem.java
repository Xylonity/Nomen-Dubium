package dev.xylonity.nomendubium.common.item;

import dev.xylonity.nomendubium.common.entity.HuntersArrowEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HuntersArrowItem extends DescribedArrowItem {

    public HuntersArrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack arrowStack, LivingEntity shooter) {
        return new HuntersArrowEntity(level, shooter, arrowStack.copyWithCount(1), ItemStack.EMPTY);
    }

}
