package dev.xylonity.nomendubium.common.entity;

import dev.xylonity.knightlib.KnightLib;
import dev.xylonity.knightlib.api.animation.KnightLibAnimatable;
import dev.xylonity.knightlib.api.animation.KnightLibAnimationHandler;
import dev.xylonity.nomendubium.common.menu.TreeOfLifeMenu;
import dev.xylonity.nomendubium.common.recipe.TreeOfLifeRecipe;
import dev.xylonity.nomendubium.common.recipe.TreeOfLifeRecipeInput;
import dev.xylonity.nomendubium.registry.NomenDubiumRecipes;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.Optional;

public final class TreeOfLifeEntity extends Mob implements MenuProvider, KnightLibAnimatable {

    private final KnightLibAnimationHandler animations = KnightLibAnimationHandler.of(this);

    private final SimpleContainer inventory = new SimpleContainer(TreeOfLifeMenu.SLOT_COUNT);

    private final ContainerData restorationData = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> TreeOfLifeEntity.this.restorationProgress;
                case 1 -> TreeOfLifeEntity.this.restorationTime;
                default -> 0;
            };

        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> TreeOfLifeEntity.this.restorationProgress = value;
                case 1 -> TreeOfLifeEntity.this.restorationTime = value;
                default -> {
                }

            }

        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    private int restorationProgress;
    private int restorationTime;
    private @Nullable ResourceLocation activeRecipe;
    private ItemStack pendingResult = ItemStack.EMPTY;

    public TreeOfLifeEntity(EntityType<? extends TreeOfLifeEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 1);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level() instanceof ServerLevel serverLevel) {
            // Computes restoration timer
            this.tickRestoration(serverLevel);
        }

    }

    private void tickRestoration(ServerLevel level) {
        // Builds the recipe input
        final TreeOfLifeRecipeInput input = new TreeOfLifeRecipeInput(this.inventory.getItem(TreeOfLifeMenu.INGREDIENT_SLOT), this.inventory.getItem(TreeOfLifeMenu.ROOT_OF_LIFE_SLOT));
        // Looks for the matching recipe
        final Optional<Pair<ResourceLocation, TreeOfLifeRecipe>> recipeHolder = level.getRecipeManager()
            .getRecipeFor(NomenDubiumRecipes.TREE_OF_LIFE_TYPE.get(), input, level, this.activeRecipe);

        // Invalid recipe
        if (recipeHolder.isEmpty()) {
            this.resetRestoration();
            return;
        }


        final Pair<ResourceLocation, TreeOfLifeRecipe> holder = recipeHolder.get();
        final TreeOfLifeRecipe recipe = holder.getSecond();

        if (!holder.getFirst().equals(this.activeRecipe) || this.pendingResult.isEmpty()) {
            this.activeRecipe = holder.getFirst();
            this.restorationProgress = 0;
            this.pendingResult = recipe.random(this.random);
        }

        if (this.pendingResult.isEmpty()) {
            this.resetRestoration();
            return;
        }

        this.restorationTime = recipe.processingTime();
        if (!this.canAcceptItemStack(this.pendingResult)) {
            this.restorationProgress = 0;
            return;
        }

        this.restorationProgress++;
        if (this.restorationProgress < this.restorationTime) {
            return;
        }

        // Restoration complete

        this.inventory.removeItem(TreeOfLifeMenu.INGREDIENT_SLOT, 1);
        this.inventory.removeItem(TreeOfLifeMenu.ROOT_OF_LIFE_SLOT, recipe.rootOfLifeCount());

        final ItemStack result = this.pendingResult.copy();
        final ItemStack currentResult = this.inventory.getItem(TreeOfLifeMenu.RESULT_SLOT);
        if (currentResult.isEmpty()) {
            this.inventory.setItem(TreeOfLifeMenu.RESULT_SLOT, result);
        }
        else {
            currentResult.grow(result.getCount());
            this.inventory.setChanged();
        }

        this.restorationProgress = 0;
        this.restorationTime = 0;
        this.activeRecipe = null;
        this.pendingResult = ItemStack.EMPTY;
    }

    private boolean canAcceptItemStack(ItemStack itemStack) {
        final ItemStack current = this.inventory.getItem(TreeOfLifeMenu.RESULT_SLOT);
        if (current.isEmpty()) {
            return itemStack.getCount() <= itemStack.getMaxStackSize();
        }

        return ItemStack.isSameItemSameTags(current, itemStack) && current.getCount() + itemStack.getCount() <= current.getMaxStackSize();
    }

    private void resetRestoration() {
        this.restorationProgress = 0;
        this.restorationTime = 0;
        this.activeRecipe = null;
        this.pendingResult = ItemStack.EMPTY;
    }

    public boolean canUseAsIngredient(ItemStack stack) {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return true;
        }

        return serverLevel.getRecipeManager()
            .getRecipes()
            .stream()
            .filter(TreeOfLifeRecipe.class::isInstance)
            .map(TreeOfLifeRecipe.class::cast)
            .anyMatch(recipe -> recipe.isIngredient(stack));
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 vec, InteractionHand hand) {
        if (this.level().isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        KnightLib.PLATFORM.openMenu((ServerPlayer) player, this, friendlyByteBuf -> friendlyByteBuf.writeInt(getId()));

        return InteractionResult.CONSUME;
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new TreeOfLifeMenu(containerId, inventory, this.inventory, this.restorationData, this);
    }

    @Override
    public void readAdditionalSaveData(@NonNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        this.inventory.clearContent();
        for (int slot = 0; slot < this.inventory.getContainerSize(); slot++) {
            if (tag.contains("InventorySlot" + slot)) {
                this.inventory.setItem(slot, ItemStack.of(tag.getCompound("InventorySlot" + slot)));
            }

        }

        this.restorationProgress = Math.max(0, tag.getInt("restoration_progress"));
        this.restorationTime = Math.max(0, tag.getInt("restoration_time"));
        this.activeRecipe = tag.contains("active_restoration_recipe") ? ResourceLocation.tryParse(tag.getString("active_restoration_recipe")) : null;
        this.pendingResult = tag.contains("pending_restoration_result") ? ItemStack.of(tag.getCompound("pending_restoration_result")) : ItemStack.EMPTY;
    }

    @Override
    public void addAdditionalSaveData(@NonNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        for (int slot = 0; slot < this.inventory.getContainerSize(); slot++) {
            final ItemStack stack = this.inventory.getItem(slot);
            if (!stack.isEmpty()) {
                tag.put("InventorySlot" + slot, stack.save(new CompoundTag()));
            }

        }

        tag.putInt("restoration_progress", this.restorationProgress);
        tag.putInt("restoration_time", this.restorationTime);

        if (this.activeRecipe != null) {
            tag.putString("active_restoration_recipe", this.activeRecipe.toString());
        }
        if (!this.pendingResult.isEmpty()) {
            tag.put("pending_restoration_result", this.pendingResult.save(new CompoundTag()));
        }

    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) || super.isInvulnerableTo(source);
    }

    @Override
    public void push(double xa, double ya, double za) {
        ;;
    }

    @Override
    public @NonNull HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.emptyList();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
        ;;
    }

    @Override
    public KnightLibAnimationHandler getAnimationHandler() {
        return animations;
    }

}
