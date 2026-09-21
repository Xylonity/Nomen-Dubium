package dev.xylonity.nomendubium.common.item;

import dev.xylonity.nomendubium.common.entity.PrimitiveArrowEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PrimitiveArrowItem extends DescribedArrowItem {

    private final double baseDamage;

    public PrimitiveArrowItem(Properties properties) {
        super(properties);
        baseDamage = PrimitiveArrowEntity.BASE_DAMAGE - 2 / 3f;
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack arrowStack, LivingEntity shooter) {
        final PrimitiveArrowEntity arrow = new PrimitiveArrowEntity(level, shooter, arrowStack.copyWithCount(1), ItemStack.EMPTY);
        arrow.setBaseDamage(baseDamage);
        return arrow;
    }

}
