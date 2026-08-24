package dev.xylonity.nomendubium.common.item;

import dev.xylonity.nomendubium.common.entity.PrimitiveArrowEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PrimitiveArrowItem extends ArrowItem {

    private final double baseDamage;

    public PrimitiveArrowItem(Properties properties) {
        super(properties);
        baseDamage = PrimitiveArrowEntity.BASE_DAMAGE - 2 / 3f;
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack arrowStack, LivingEntity shooter, ItemStack weaponStack) {
        final PrimitiveArrowEntity arrow = new PrimitiveArrowEntity(level, shooter, arrowStack.copyWithCount(1), weaponStack);
        arrow.setBaseDamage(baseDamage);
        return arrow;
    }

    @Override
    public Projectile asProjectile(Level level, Position position, ItemStack stack, Direction direction) {
        final PrimitiveArrowEntity arrow = new PrimitiveArrowEntity(level, position.x(), position.y(), position.z(), stack.copyWithCount(1), null);
        arrow.setBaseDamage(baseDamage);
        arrow.pickup = AbstractArrow.Pickup.ALLOWED;
        return arrow;
    }

}
