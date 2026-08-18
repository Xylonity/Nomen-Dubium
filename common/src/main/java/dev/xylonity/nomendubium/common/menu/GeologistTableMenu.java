package dev.xylonity.nomendubium.common.menu;

import dev.xylonity.nomendubium.registry.NomenDubiumItems;
import dev.xylonity.nomendubium.registry.NomenDubiumMenus;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public class GeologistTableMenu extends AbstractContainerMenu {

    private final Container container;
    private final ContainerLevelAccess access;
    private final Player player;

    public GeologistTableMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(1), ContainerLevelAccess.NULL);
    }

    public GeologistTableMenu(int containerId, Inventory inventory, Container container, ContainerLevelAccess access) {
        super(NomenDubiumMenus.GEOLOGIST_TABLE.get(), containerId);
        checkContainerSize(container, 1);

        this.container = container;
        this.access = access;
        this.player = inventory.player;
        this.container.startOpen(inventory.player);

        // Only the encased fossil can be placed in the only slot in the whole menu
        this.addSlot(new Slot(container, 0, 120, 98) {

            @Override
            public boolean mayPlace(@NonNull ItemStack stack) {
                return stack.is(NomenDubiumItems.ENCASED_FOSSIL.get());
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }

            @Override
            public boolean mayPickup(@NonNull Player player) {
                return !this.getItem().is(NomenDubiumItems.ENCASED_FOSSIL.get());
            }

        });

        this.addStandardInventorySlots(inventory, 48, 129);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }

}
