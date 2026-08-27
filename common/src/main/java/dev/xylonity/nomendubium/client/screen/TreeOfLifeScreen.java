package dev.xylonity.nomendubium.client.screen;

import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.common.menu.TreeOfLifeMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jspecify.annotations.NonNull;

public final class TreeOfLifeScreen extends AbstractContainerScreen<TreeOfLifeMenu> {

    private static final Identifier TEXTURE = NomenDubium.of("textures/gui/tree_of_life.png");

    private static final int GUI_WIDTH = 180;
    private static final int GUI_HEIGHT = 168;

    public TreeOfLifeScreen(TreeOfLifeMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, GUI_WIDTH, GUI_HEIGHT);
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);

        // Background
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0, 0, GUI_WIDTH, GUI_HEIGHT, GUI_WIDTH, GUI_HEIGHT, 256, 256);

        // Progress bar
        final int progressWidth = this.menu.getProgressWidth(39);
        if (progressWidth > 0) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos + 76, this.topPos + 37, 186, 0, progressWidth, 17, progressWidth, 17, 256, 256);
        }

    }

    /// No title text
    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        ;;
    }

}
