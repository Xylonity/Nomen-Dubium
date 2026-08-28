package dev.xylonity.nomendubium.common.entity;

import dev.xylonity.nomendubium.common.menu.TreeOfLifeMenu;
import dev.xylonity.nomendubium.common.recipe.TreeOfLifeRecipe;
import dev.xylonity.nomendubium.common.recipe.TreeOfLifeRecipeInput;
import dev.xylonity.nomendubium.registry.NomenDubiumRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public final class TreeOfLifeEntity extends LivingEntity implements MenuProvider {

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
    private @Nullable ResourceKey<Recipe<?>> activeRecipe;
    private ItemStack pendingResult = ItemStack.EMPTY;

    public TreeOfLifeEntity(EntityType<? extends TreeOfLifeEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
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
        final Optional<RecipeHolder<TreeOfLifeRecipe>> recipeHolder = level.recipeAccess().getRecipeFor(NomenDubiumRecipes.TREE_OF_LIFE_TYPE.get(), input, level, this.activeRecipe);

        // Invalid recipe
        if (recipeHolder.isEmpty()) {
            this.resetRestoration();
            return;
        }


        final RecipeHolder<TreeOfLifeRecipe> holder = recipeHolder.get();
        final TreeOfLifeRecipe recipe = holder.value();

        if (!holder.id().equals(this.activeRecipe) || this.pendingResult.isEmpty()) {
            this.activeRecipe = holder.id();
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

        return ItemStack.isSameItemSameComponents(current, itemStack) && current.getCount() + itemStack.getCount() <= current.getMaxStackSize();
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

        return serverLevel.recipeAccess()
            .getRecipes()
            .stream()
            .map(RecipeHolder::value)
            .filter(TreeOfLifeRecipe.class::isInstance)
            .map(TreeOfLifeRecipe.class::cast)
            .anyMatch(recipe -> recipe.isIngredient(stack));
    }

    @Override
    public @NonNull InteractionResult interact(Player player, @NonNull InteractionHand hand, @NonNull Vec3 location) {
        if (this.level().isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        player.openMenu(this);
        return InteractionResult.SUCCESS_SERVER;
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new TreeOfLifeMenu(containerId, inventory, this.inventory, this.restorationData, this);
    }

    @Override
    protected void readAdditionalSaveData(@NonNull ValueInput input) {
        super.readAdditionalSaveData(input);
        final NonNullList<ItemStack> items = this.inventory.getItems();
        items.clear();
        ContainerHelper.loadAllItems(input, items);
        this.restorationProgress = Math.max(0, input.getIntOr("restoration_progress", 0));
        this.restorationTime = Math.max(0, input.getIntOr("restoration_time", 0));
        this.activeRecipe = input.read("active_restoration_recipe", Recipe.KEY_CODEC).orElse(null);
        this.pendingResult = input.read("pending_restoration_result", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
    }

    @Override
    protected void addAdditionalSaveData(@NonNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        ContainerHelper.saveAllItems(output, this.inventory.getItems());
        output.putInt("restoration_progress", this.restorationProgress);
        output.putInt("restoration_time", this.restorationTime);
        output.storeNullable("active_restoration_recipe", Recipe.KEY_CODEC, this.activeRecipe);
        output.store("pending_restoration_result", ItemStack.OPTIONAL_CODEC, this.pendingResult);
    }

    @Override
    public boolean isInvulnerableTo(@NonNull ServerLevel level, DamageSource source) {
        return !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) || super.isInvulnerableTo(level, source);
    }

    @Override
    public void push(double xa, double ya, double za) {
        ;;
    }

    @Override
    public @NonNull HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

}
