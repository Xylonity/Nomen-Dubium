package dev.xylonity.nomendubium.common.item;

import dev.xylonity.nomendubium.common.entity.HuntersArrowEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HuntersArrowItem extends DescribedArrowItem {

    public HuntersArrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack arrowStack, LivingEntity shooter, ItemStack weaponStack) {
        return new HuntersArrowEntity(level, shooter, arrowStack.copyWithCount(1), weaponStack);
    }

    @Override
    public Projectile asProjectile(Level level, Position position, ItemStack stack, Direction direction) {
        final HuntersArrowEntity arrow = new HuntersArrowEntity(level, position.x(), position.y(), position.z(), stack.copyWithCount(1), null);
        arrow.pickup = AbstractArrow.Pickup.ALLOWED;
        return arrow;
    }

}
