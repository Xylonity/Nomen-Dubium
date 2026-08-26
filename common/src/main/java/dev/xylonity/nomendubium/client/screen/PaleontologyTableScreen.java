package dev.xylonity.nomendubium.client.screen;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.common.menu.PaleontologyTableMenu;
import dev.xylonity.nomendubium.common.menu.PaleontologyTableMenuReal;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.jspecify.annotations.NonNull;

/// Some of the implementation is derived from the cornelius companions! screen
/// https://github.com/Xylonity/Companions/blob/v1.20.1/common/src/main/java/dev/xylonity/companions/client/gui/screen/CorneliusScreen.java
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
    private static final int[] TOOL_Y = { 10, 36, 62 };
    private static final int HELD_TOOL_WIDTH = 32;
    private static final int HELD_TOOL_HEIGHT = 34;

    private Identifier lastFossilTexture = FOSSIL_TEXTURES[0];
    private int fossilPopAge = FOSSIL_POP_DURATION;

    private int selectedTool = -1;

    private float toolAngle;

    public PaleontologyTableScreen(PaleontologyTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.inventoryLabelX = 48;
        this.inventoryLabelY = 117;
        this.titleLabelX = 8;
        this.titleLabelY = 4;
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, this.leftPos, this.topPos, 0, 0, GUI_WIDTH, GUI_HEIGHT, GUI_WIDTH, GUI_HEIGHT, 256, 256);

        final int ox = this.leftPos;
        final int oy = this.topPos;

        // Fossil in the middle of the gui
        if (this.menu.hasWorkpiece()) {
            final int fossilStage = this.getFossilStage();
            final Identifier fossilTexture = this.getFossilTexture(fossilStage);
            this.lastFossilTexture = fossilTexture;
            final float fossilScale = this.getFossilPopScale(partialTick);
            final float fossilCenterX = ox + FOSSIL_X + FOSSIL_SIZE / 2.0F;
            final float fossilCenterY = oy + FOSSIL_Y + FOSSIL_SIZE / 2.0F;

            graphics.pose().pushMatrix();

            graphics.pose().translate(fossilCenterX, fossilCenterY);
            graphics.pose().scale(fossilScale, fossilScale);
            graphics.pose().translate(-fossilCenterX, -fossilCenterY);
            graphics.blit(RenderPipelines.GUI_TEXTURED, fossilTexture, ox + FOSSIL_X, oy + FOSSIL_Y, 0, 0, FOSSIL_SIZE, FOSSIL_SIZE, FOSSIL_TEXTURE_SIZE, FOSSIL_TEXTURE_SIZE, FOSSIL_TEXTURE_SIZE, FOSSIL_TEXTURE_SIZE);

            graphics.pose().popMatrix();
        }

        // Encased fossil slot
        graphics.fill(ox + 118, oy + 96, ox + 138, oy + 116, 0xFF3B281B);
        graphics.fill(ox + 120, oy + 98, ox + 136, oy + 114, 0xFFB78556);
        graphics.outline(ox + 118, oy + 96, 20, 20, 0xFFE5C48A);

        // Tool rendering at the left of the gui
        this.extractTool(graphics, CHISEL, PaleontologyTableMenuReal.TOOL_CHISEL, ox, oy, mouseX, mouseY);
        this.extractTool(graphics, HAMMER, PaleontologyTableMenuReal.TOOL_HAMMER, ox, oy, mouseX, mouseY);
        this.extractTool(graphics, BRUSH, PaleontologyTableMenuReal.TOOL_BRUSH, ox, oy, mouseX, mouseY);

        // If a tool is selected, tool rendering
        if (this.selectedTool >= 0) {
            final Identifier texture = toolTexture(this.selectedTool);
            graphics.nextStratum();

            graphics.pose().pushMatrix();

            graphics.pose().translate(mouseX, mouseY);
            graphics.pose().rotate(this.toolAngle);
            graphics.pose().translate(-mouseX, -mouseY);
            graphics.blit(RenderPipelines.GUI_TEXTURED, texture, mouseX - HELD_TOOL_WIDTH / 2, mouseY - HELD_TOOL_HEIGHT / 2, 0, 0, HELD_TOOL_WIDTH, HELD_TOOL_HEIGHT, 32, 32, 32, 32);

            graphics.pose().popMatrix();
        }

    }

    private void extractTool(GuiGraphicsExtractor graphics, Identifier texture, int tool, int ox, int oy, int mouseX, int mouseY) {
        final int y = TOOL_Y[tool];
        final boolean selected = this.selectedTool == tool;
        final boolean hovered = isToolAt(mouseX - ox, mouseY - oy) == tool;
        if (!selected) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, texture, ox + TOOL_X, oy + y, 0, 0, TOOL_WIDTH, TOOL_HEIGHT, 32, 32, 32, 32);
        }
        if (hovered) {
            graphics.requestCursor(this.isPlaying() && this.selectedTool < 0 ? CursorTypes.POINTING_HAND : CursorTypes.NOT_ALLOWED);
        }

    }

    private static int isToolAt(double x, double y) {
        if (x < TOOL_X || x >= TOOL_X + TOOL_WIDTH) {
            return -1;
        }

        int closestTool = -1;
        double closestDistance = Double.MAX_VALUE;
        for (int tool = 0; tool < TOOL_Y.length; tool++) {
            if (!inside(x, y, TOOL_X, TOOL_Y[tool], TOOL_WIDTH, TOOL_HEIGHT)) {
                continue;
            }

            final double distance = Math.abs(y - (TOOL_Y[tool] + TOOL_HEIGHT / 2.0D));
            if (distance < closestDistance) {
                closestTool = tool;
                closestDistance = distance;
            }

        }

        return closestTool;
    }

    /// Whether xy is inside the bounds specified
    private static boolean inside(double x, double y, int left, int top, int width, int height) {
        return x >= left && y >= top && x < left + width && y < top + height;
    }

    /// Whether the game is active or not
    private boolean isPlaying() {
        return this.menu.getGameState() == PaleontologyTableMenuReal.STATE_PLAYING;
    }

    private float getFossilPopScale(float partialTick) {
        if (this.fossilPopAge >= FOSSIL_POP_DURATION) {
            return 1;
        }

        final float progress = Mth.clamp((this.fossilPopAge + partialTick) / FOSSIL_POP_DURATION, 0, 1);
        final float eased = 1 - (float) Math.pow(1 - progress, 3);
        return 0.78f + eased * 0.22F + Mth.sin(progress * Mth.PI) * 0.12F;
    }

    private static Identifier toolTexture(int tool) {
        return switch (tool) {
            case PaleontologyTableMenu.TOOL_CHISEL -> CHISEL;
            case PaleontologyTableMenu.TOOL_HAMMER -> HAMMER;
            default -> BRUSH;
        };
    }

    private Identifier getFossilTexture(int stage) {
        return FOSSIL_TEXTURES[0];
    }

    private int getFossilStage() {
        return 0;
    }

}