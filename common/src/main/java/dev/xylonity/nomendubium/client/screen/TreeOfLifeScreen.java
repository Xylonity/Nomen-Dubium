package dev.xylonity.nomendubium.client.screen;

import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.common.menu.TreeOfLifeMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jspecify.annotations.NonNull;

public final class TreeOfLifeScreen extends AbstractContainerScreen<TreeOfLifeMenu> {

    private static final ResourceLocation TEXTURE = NomenDubium.of("textures/gui/tree_of_life.png");

    private static final int GUI_WIDTH = 180;
    private static final int GUI_HEIGHT = 168;

    public TreeOfLifeScreen(TreeOfLifeMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = GUI_WIDTH;
        this.imageHeight = GUI_HEIGHT;
    }

    @Override
    public void render(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NonNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        // Background
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, GUI_WIDTH, GUI_HEIGHT);

        // Progress bar
        final int progressWidth = this.menu.getProgressWidth(39);
        if (progressWidth > 0) {
            graphics.blit(TEXTURE, this.leftPos + 76, this.topPos + 37, 186, 0, progressWidth, 17);
        }

    }

    /// No title text
    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        ;;
    }

}
