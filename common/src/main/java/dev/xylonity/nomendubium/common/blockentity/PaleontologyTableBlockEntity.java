package dev.xylonity.nomendubium.common.blockentity;

import dev.xylonity.nomendubium.common.menu.PaleontologyTableMenu;
import dev.xylonity.nomendubium.registry.NomenDubiumBlockEntities;
import dev.xylonity.nomendubium.registry.NomenDubiumItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;

public final class PaleontologyTableBlockEntity extends BaseContainerBlockEntity {

    private NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);

    public PaleontologyTableBlockEntity(BlockPos pos, BlockState state) {
        super(NomenDubiumBlockEntities.PALEONTOLOGY_TABLE.get(), pos, state);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, this.items);
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return slot == 0 && stack.is(NomenDubiumItems.ENCASED_FOSSIL.get());
    }

    @Override
    public boolean canTakeItem(Container target, int slot, ItemStack stack) {
        return !stack.is(NomenDubiumItems.ENCASED_FOSSIL.get());
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    public void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected @NonNull Component getDefaultName() {
        return Component.translatable("container.nomendubium.paleontology_table");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new PaleontologyTableMenu(containerId, inventory, this, ContainerLevelAccess.create(this.getLevel(), this.getBlockPos()));
    }

    @Override
    public boolean isEmpty() {
        return this.items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        return ContainerHelper.removeItem(this.items, slot, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.items.set(slot, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }

        this.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

}
