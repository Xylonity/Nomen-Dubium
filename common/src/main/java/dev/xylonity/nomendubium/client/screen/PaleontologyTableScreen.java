package dev.xylonity.nomendubium.client.screen;

import dev.xylonity.nomendubium.common.menu.PaleontologyTableMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class PaleontologyTableScreen extends AbstractContainerScreen<PaleontologyTableMenu> {

    public PaleontologyTableScreen(PaleontologyTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

}