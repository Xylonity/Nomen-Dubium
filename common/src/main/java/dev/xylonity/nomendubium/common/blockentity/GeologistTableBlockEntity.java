package dev.xylonity.nomendubium.common.blockentity;

import dev.xylonity.nomendubium.common.menu.GeologistTableMenu;
import dev.xylonity.nomendubium.registry.NomenDubiumBlockEntities;
import dev.xylonity.nomendubium.registry.NomenDubiumItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;

public final class GeologistTableBlockEntity extends BaseContainerBlockEntity {

    private NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);

    public GeologistTableBlockEntity(BlockPos pos, BlockState state) {
        super(NomenDubiumBlockEntities.GEOLOGIST_TABLE.get(), pos, state);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(input, this.items);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, this.items);
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

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected @NonNull Component getDefaultName() {
        return Component.translatable("container.nomendubium.geologist_table");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new GeologistTableMenu(containerId, inventory, this, ContainerLevelAccess.create(this.getLevel(), this.getBlockPos()));
    }

}
