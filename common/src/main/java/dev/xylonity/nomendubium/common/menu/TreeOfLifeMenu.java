package dev.xylonity.nomendubium.common.menu;

import dev.xylonity.nomendubium.common.entity.TreeOfLifeEntity;
import dev.xylonity.nomendubium.registry.NomenDubiumItems;
import dev.xylonity.nomendubium.registry.NomenDubiumMenus;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public final class TreeOfLifeMenu extends AbstractContainerMenu {

    private static final int INGREDIENT_SLOT = 0;
    private static final int ROOT_OF_LIFE_SLOT = 1;
    private static final int RESULT_SLOT = 2;
    private static final int SLOT_COUNT = 3;

    private static final int PLAYER_SLOT_START = SLOT_COUNT;
    private static final int PLAYER_INVENTORY_END = PLAYER_SLOT_START + 27;
    private static final int PLAYER_SLOT_END = PLAYER_INVENTORY_END + 9;

    private final Container container;
    private final ContainerData data;
    private final @Nullable TreeOfLifeEntity treeOfLife;

    public TreeOfLifeMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(SLOT_COUNT), new SimpleContainerData(2), null);
    }

    public TreeOfLifeMenu(int containerId, Inventory inventory, Container container, ContainerData data, @Nullable TreeOfLifeEntity treeOfLife) {
        super(NomenDubiumMenus.TREE_OF_LIFE.get(), containerId);
        checkContainerSize(container, SLOT_COUNT);
        checkContainerDataCount(data, 2);

        this.container = container;
        this.data = data;
        this.treeOfLife = treeOfLife;
        this.container.startOpen(inventory.player);

        this.addSlot(new Slot(container, INGREDIENT_SLOT, 50, 38) {
            @Override
            public boolean mayPlace(@NonNull ItemStack stack) {
                return TreeOfLifeMenu.this.canUseAsIngredient(stack);
            }

        });
        this.addSlot(new Slot(container, ROOT_OF_LIFE_SLOT, 87, 62) {
            @Override
            public boolean mayPlace(@NonNull ItemStack stack) {
                return stack.is(NomenDubiumItems.ROOT_OF_LIFE.get());
            }

        });
        this.addSlot(new Slot(container, RESULT_SLOT, 125, 39) {
            @Override
            public boolean mayPlace(@NonNull ItemStack stack) {
                return false;
            }

        });

        // Inventory
        this.addStandardInventorySlots(inventory, 10, 86);

        // Extra slots
        this.addDataSlots(data);
    }

    private boolean canUseAsIngredient(ItemStack stack) {
        return this.treeOfLife == null || this.treeOfLife.canUseAsIngredient(stack);
    }

    @Override
    public @NonNull ItemStack quickMoveStack(@NonNull Player player, int slotIndex) {
        if (slotIndex < 0 || slotIndex >= this.slots.size()) {
            return ItemStack.EMPTY;
        }

        final Slot slot = this.slots.get(slotIndex);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        final ItemStack stack = slot.getItem();
        final ItemStack original = stack.copy();

        if (slotIndex == RESULT_SLOT) {
            if (!this.moveItemStackTo(stack, PLAYER_SLOT_START, PLAYER_SLOT_END, true)) {
                return ItemStack.EMPTY;
            }

            slot.onQuickCraft(stack, original);
        }
        else if (slotIndex < SLOT_COUNT) {
            if (!this.moveItemStackTo(stack, PLAYER_SLOT_START, PLAYER_SLOT_END, false)) {
                return ItemStack.EMPTY;
            }

        }
        else if (stack.is(NomenDubiumItems.ROOT_OF_LIFE.get())) {
            if (!this.moveItemStackTo(stack, ROOT_OF_LIFE_SLOT, ROOT_OF_LIFE_SLOT + 1, false)) {
                return ItemStack.EMPTY;
            }

        }
        else if (this.canUseAsIngredient(stack)) {
            if (!this.moveItemStackTo(stack, INGREDIENT_SLOT, INGREDIENT_SLOT + 1, false)) {
                return ItemStack.EMPTY;
            }

        }
        else if (slotIndex < PLAYER_INVENTORY_END) {
            if (!this.moveItemStackTo(stack, PLAYER_INVENTORY_END, PLAYER_SLOT_END, false)) {
                return ItemStack.EMPTY;
            }

        }
        else if (!this.moveItemStackTo(stack, PLAYER_SLOT_START, PLAYER_INVENTORY_END, false)) {
            return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        }
        else {
            slot.setChanged();
        }

        if (stack.getCount() == original.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, stack);

        return original;
    }

    public int getProgressWidth(int maximumWidth) {
        final int progress = this.data.get(0);
        final int total = this.data.get(1);
        if (progress <= 0 || total <= 0) {
            return 0;
        }

        return Math.min(maximumWidth, (int)((long) progress * maximumWidth / total));
    }

    @Override
    public boolean stillValid(@NonNull Player player) {
        return this.treeOfLife == null || this.treeOfLife.isAlive() && player.distanceToSqr(this.treeOfLife) <= 64.0;
    }

    @Override
    public void removed(@NonNull Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }

}