package dev.xylonity.nomendubium.common.menu;

import dev.xylonity.nomendubium.registry.NomenDubiumMenus;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class GeologistTableMenu extends AbstractContainerMenu {

    public GeologistTableMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(1), ContainerLevelAccess.NULL);
    }

    public GeologistTableMenu(int containerId, Inventory inventory, Container container, ContainerLevelAccess access) {
        super(NomenDubiumMenus.GEOLOGIST_TABLE.get(), containerId);
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
