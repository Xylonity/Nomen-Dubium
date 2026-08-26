package dev.xylonity.nomendubium.client.screen;

import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.common.menu.PaleontologyTableMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class PaleontologyTableScreen extends AbstractContainerScreen<PaleontologyTableMenu> {

    private static final Identifier BACKGROUND = NomenDubium.of("textures/gui/paleontology_table.png");
    private static final Identifier CHISEL = NomenDubium.of("textures/gui/chisel.png");
    private static final Identifier HAMMER = NomenDubium.of("textures/gui/hammer.png");
    private static final Identifier BRUSH = NomenDubium.of("textures/gui/brush.png");
    private static final Identifier[] FOSSIL_TEXTURES = {
            NomenDubium.of("textures/gui/fossil_1.png"),
            NomenDubium.of("textures/gui/fossil_2.png"),
            NomenDubium.of("textures/gui/fossil_3.png"),
            NomenDubium.of("textures/gui/fossil_4.png")
    };

    private static final int GUI_WIDTH = 256;
    private static final int GUI_HEIGHT = 210;

    private static final int FOSSIL_X = 96;
    private static final int FOSSIL_Y = 19;
    private static final int FOSSIL_SIZE = 64;
    private static final int FOSSIL_TEXTURE_SIZE = 80;
    private static final int FOSSIL_POP_DURATION = 10;

    private static final int TOOL_X = 11;
    private static final int TOOL_WIDTH = 32;
    private static final int TOOL_HEIGHT = 34;

    public PaleontologyTableScreen(PaleontologyTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

}