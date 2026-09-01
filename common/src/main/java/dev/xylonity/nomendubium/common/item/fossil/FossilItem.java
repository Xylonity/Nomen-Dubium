package dev.xylonity.nomendubium.common.item.fossil;

import dev.xylonity.nomendubium.common.entity.SkeletonPartEntity;
import dev.xylonity.nomendubium.common.entity.skeleton.SkeletonPartType;
import dev.xylonity.nomendubium.common.item.DescribedItem;
import dev.xylonity.nomendubium.registry.NomenDubiumDataComponents;
import dev.xylonity.nomendubium.registry.NomenDubiumEntities;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public final class FossilItem extends DescribedItem {

    public FossilItem(Properties properties) {
        super(properties);
    }

    public ItemStack createStack(String part) {
        final ItemStack stack = new ItemStack(this);
        stack.set(NomenDubiumDataComponents.FOSSIL_PART.get(), part);
        return stack;
    }

    public static String getPart(ItemStack stack) {
        return stack.get(NomenDubiumDataComponents.FOSSIL_PART.get());
    }

    @Override
    public @NonNull InteractionResult useOn(UseOnContext context) {
        // Only able to place skeleton body parts
        final SkeletonPartType partType = SkeletonPartType.byFossilPart(getPart(context.getItemInHand()));
        if (partType == null || !partType.isBody() || context.getClickedFace() != Direction.UP) {
            return InteractionResult.FAIL;
        }

        final Level level = context.getLevel();
        final BlockPlaceContext placeContext = new BlockPlaceContext(context);
        final Vec3 position = Vec3.atBottomCenterOf(placeContext.getClickedPos());
        final SkeletonPartEntity skeleton = new SkeletonPartEntity(NomenDubiumEntities.SKELETON_PART.get(), level);

        skeleton.setPartType(partType);

        // Specifies the looking yaw
        float yaw = Mth.wrapDegrees(context.getRotation());
        if (context.isSecondaryUseActive()) {
            // If the player is shifting, snaps the yaw to a main direction
            yaw = Math.round(yaw / 90F) * 90F;
        }

        // Centers the skeleton
        skeleton.snapTo(position.x(), position.y(), position.z(), yaw, 0.0F);

        if (!level.noCollision(skeleton, skeleton.getBoundingBox())) {
            return InteractionResult.FAIL;
        }

        if (!level.isClientSide()) {
            // Adds the skeleton
            level.addFreshEntity(skeleton);
            level.playSound(null, position.x(), position.y(), position.z(), SoundEvents.BONE_BLOCK_PLACE, SoundSource.BLOCKS, 0.9F, 0.9F + level.getRandom().nextFloat() * 0.2F);
            if (context.getPlayer() == null || !context.getPlayer().hasInfiniteMaterials()) {
                context.getItemInHand().shrink(1);
            }

            return InteractionResult.SUCCESS_SERVER;
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public @NonNull Component getName(@NonNull ItemStack stack) {
        final String part = getPart(stack);
        return part == null ? super.getName(stack) : Component.translatableWithFallback(this.getDescriptionId() + "." + part, super.getName(stack).getString());
    }

    @Override
    protected void appendDescription(ItemStack stack, Consumer<Component> tooltip) {
        final String part = getPart(stack);
        if (SkeletonPartType.byFossilPart(part) != null) {
            tooltip.accept(Component.translatable(this.getDescriptionId() + "." + part + ".tooltip").withStyle(ChatFormatting.GRAY));
        }

        super.appendDescription(stack, tooltip);
    }

    @Override
    protected int descriptionLineCount() {
        return 2;
    }

}
