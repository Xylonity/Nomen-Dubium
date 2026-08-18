package dev.xylonity.nomendubium.client.screen;

import dev.xylonity.nomendubium.common.menu.GeologistTableMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GeologistTableScreen extends AbstractContainerScreen<GeologistTableMenu> {

    public GeologistTableScreen(GeologistTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

}