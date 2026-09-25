package dev.xylonity.nomendubium.client.screen;

import com.mojang.math.Axis;
import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.common.item.fossil.util.FossilCategory;
import dev.xylonity.nomendubium.common.menu.PaleontologyTableMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/// Some chunks of the implementation are derived from the cornelius companions! screen
/// https://github.com/Xylonity/Companions/blob/v1.20.1/common/src/main/java/dev/xylonity/companions/client/gui/screen/CorneliusScreen.java
public class PaleontologyTableScreen extends AbstractContainerScreen<PaleontologyTableMenu> {

    private static final ResourceLocation BACKGROUND = NomenDubium.of("textures/gui/paleontology_table.png");
    private static final ResourceLocation CHISEL = NomenDubium.of("textures/gui/chisel.png");
    private static final ResourceLocation HAMMER = NomenDubium.of("textures/gui/hammer.png");
    private static final ResourceLocation BRUSH = NomenDubium.of("textures/gui/brush.png");
    private static final ResourceLocation[] FOSSIL_TEXTURES = {
            NomenDubium.of("textures/gui/fossil_0.png"),
            NomenDubium.of("textures/gui/fossil_body_1.png"),
            NomenDubium.of("textures/gui/fossil_body_2.png"),
            NomenDubium.of("textures/gui/fossil_body_3.png")
    };

    private static final ResourceLocation[] IMPACT_TEXTURES = createParticleTextures("big_smoke_", 12);
    private static final ResourceLocation[] BRUSH_DUST_TEXTURES = createBrushDustTextures();

    private static final int GUI_WIDTH = 256;
    private static final int GUI_HEIGHT = 210;

    private static final int FOSSIL_X = 96;
    private static final int FOSSIL_Y = 19;
    private static final int FOSSIL_SIZE = 64;
    private static final int FOSSIL_TEXTURE_SIZE = 80;
    private static final int FOSSIL_POP_DURATION = 10;

    private static final int INSTRUCTION_AREA_X = 142;
    private static final int INSTRUCTION_AREA_Y = 96;
    private static final int INSTRUCTION_AREA_WIDTH = GUI_WIDTH - INSTRUCTION_AREA_X - 4;
    private static final int INSTRUCTION_AREA_HEIGHT = 20;

    private static final int TOOL_X = 11;
    private static final int TOOL_WIDTH = 32;
    private static final int TOOL_HEIGHT = 34;
    private static final int[] TOOL_Y = {10, 36, 62};
    private static final float TOOL_SWING_MAX_ANGLE = 0.55F;
    private static final float TOOL_SWING_SPEED_FACTOR = 0.24F;
    private static final float TOOL_SWING_STIFFNESS = 0.22F;
    private static final float TOOL_SWING_DAMPING = 0.72F;
    private static final int TOOL_DROP_WIDTH = (GUI_WIDTH + 2) / 3;

    private static final int HELD_TOOL_WIDTH = 32;
    private static final int HELD_TOOL_HEIGHT = 34;

    private static final float BRUSH_MIN_RADIUS = 4.0F;
    private static final float BRUSH_MAX_RADIUS = 46.0F;
    private static final float BRUSH_REQUIRED_ROTATION = Mth.TWO_PI * 0.78F;
    private static final float BRUSH_MAX_ANGLE_STEP = 1.15F;
    private static final float BRUSH_REVERSE_PENALTY = 0.75F;
    private static final float BRUSH_DUST_STEP = 0.24F;

    private static final int CHISEL_PARTS = 13;
    private static final int CHISEL_GUIDE_DURATION = 60;
    private static final float CHISEL_START_RADIUS = 9.0F;
    private static final float CHISEL_TRACE_TOLERANCE = 9.0F;
    private static final float CHISEL_MAX_TRACE_STEP = 0.75F;
    private static final float CHISEL_BACKTRACK_TOLERANCE = 0.22F;
    private static final float CHISEL_REQUIRED_PROGRESS = 0.88F;

    private static final int BAR_Y = 15;
    private static final int BAR_WIDTH = 10;
    private static final int BAR_HEIGHT = 70;
    private static final int ROUND_BAR_X = 215;
    private static final int PROGRESS_BAR_X = 229;
    private static final int FEEDBACK_LIMIT = 96;

    private final List<DustParticle> dustParticles = new ArrayList<>();
    private final Map<ResourceLocation, Boolean> availableCategoryTextures = new HashMap<>();

    private ResourceLocation lastFossilTexture = FOSSIL_TEXTURES[0];
    private int fossilPopAge = FOSSIL_POP_DURATION;

    private int seenFossilStage = -1;
    private int seenGameState = PaleontologyTableMenu.STATE_IDLE;
    private int seenRound = -1;
    private int seenCountdownSecond = -1;
    private int seenGlobalSecond = -1;

    private boolean draggingBrush;
    private boolean tracingChisel;

    private int chiselGuideAge;
    private float chiselTraceProgress;
    private final float[] chiselPathX = new float[CHISEL_PARTS];
    private final float[] chiselPathY = new float[CHISEL_PARTS];

    private float brushStartAngle;
    private float brushLastAngle;
    private float brushRotation;
    private float brushDustRotation;
    private int brushDirection;

    private boolean cursorTracking;
    private double lastCursorX;
    private double lastCursorY;

    private int selectedTool = -1;
    private float toolAngle;
    private float toolAngularVelocity;
    private final Random random = new Random();

    public PaleontologyTableScreen(PaleontologyTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = GUI_WIDTH;
        this.imageHeight = GUI_HEIGHT;
        this.inventoryLabelX = 48;
        this.inventoryLabelY = 117;
        this.titleLabelX = 8;
        this.titleLabelY = 4;
    }

    @Override
    protected void init() {
        super.init();
        this.seenRound = -1;
        this.seenFossilStage = -1;
        this.seenCountdownSecond = -1;
        this.seenGlobalSecond = -1;
        this.fossilPopAge = FOSSIL_POP_DURATION;
        this.seenGameState = this.menu.getGameState();
        this.lastFossilTexture = FOSSIL_TEXTURES[0];
        this.selectedTool = -1;
        this.dustParticles.clear();
        this.availableCategoryTextures.clear();
        this.resetToolSwing();
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        final int gameState = this.menu.getGameState();
        // Updates the fossil texture
        if (this.menu.hasWorkpiece()) {
            this.lastFossilTexture = this.getFossilTexture(this.getFossilStage());
        }
        // On game lose
        if (gameState == PaleontologyTableMenu.STATE_LOST && this.seenGameState != PaleontologyTableMenu.STATE_LOST) {
            this.spawnFossilBreakParticles(this.lastFossilTexture);
            this.playUiSound(SoundEvents.ITEM_BREAK, 0.72F, 0.85F);
        }
        else if (gameState == PaleontologyTableMenu.STATE_WON && this.seenGameState != PaleontologyTableMenu.STATE_WON) {
            this.playUiSound(SoundEvents.PLAYER_LEVELUP, 1.15F, 0.65F);
        }

        this.tickTimerSounds(gameState);
        this.seenGameState = gameState;

        if (gameState == PaleontologyTableMenu.STATE_PLAYING && this.menu.getRoundIndex() != this.seenRound) {
            this.seenRound = this.menu.getRoundIndex();
            this.resetBrushCircle();
            this.moveChiselPath();
            this.playToolChangeSound();
        }

        if (this.isCorrectToolSelected() && this.selectedTool == PaleontologyTableMenu.TOOL_CHISEL && !this.tracingChisel && ++this.chiselGuideAge >= CHISEL_GUIDE_DURATION) {
            this.moveChiselPath();
        }

        // Ticks pop animation
        this.tickFossilStage();
        this.tickFeedback();

        if (gameState != PaleontologyTableMenu.STATE_PLAYING) {
            this.selectedTool = -1;
            this.resetBrushCircle();
            this.resetChiselTrace();
            this.resetToolSwing();
        }
    }

    @Override
    protected void renderBg(@NonNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, GUI_WIDTH, GUI_HEIGHT);

        final int ox = this.leftPos;
        final int oy = this.topPos;

        // Fossil in the middle of the gui
        if (this.menu.hasWorkpiece()) {
            final int fossilStage = this.getFossilStage();
            final ResourceLocation fossilTexture = this.getFossilTexture(fossilStage);
            this.lastFossilTexture = fossilTexture;
            final float fossilScale = this.getFossilPopScale(partialTick);
            final float fossilCenterX = ox + FOSSIL_X + FOSSIL_SIZE / 2.0F;
            final float fossilCenterY = oy + FOSSIL_Y + FOSSIL_SIZE / 2.0F;

            graphics.pose().pushPose();

            graphics.pose().translate(fossilCenterX, fossilCenterY, 0.0F);
            graphics.pose().scale(fossilScale, fossilScale, 1.0F);
            graphics.pose().translate(-fossilCenterX, -fossilCenterY, 0.0F);
            graphics.blit(fossilTexture, ox + FOSSIL_X, oy + FOSSIL_Y, FOSSIL_SIZE, FOSSIL_SIZE, 0, 0, FOSSIL_TEXTURE_SIZE, FOSSIL_TEXTURE_SIZE, FOSSIL_TEXTURE_SIZE, FOSSIL_TEXTURE_SIZE);

            graphics.pose().popPose();
        }

        // Encased fossil slot
        graphics.fill(ox + 118, oy + 96, ox + 138, oy + 116, 0xFF3B281B);
        graphics.fill(ox + 120, oy + 98, ox + 136, oy + 114, 0xFFB78556);
        graphics.renderOutline(ox + 118, oy + 96, 20, 20, 0xFFE5C48A);

        // Tool rendering at the left of the gui
        this.renderTool(graphics, CHISEL, PaleontologyTableMenu.TOOL_CHISEL, ox, oy);
        this.renderTool(graphics, HAMMER, PaleontologyTableMenu.TOOL_HAMMER, ox, oy);
        this.renderTool(graphics, BRUSH, PaleontologyTableMenu.TOOL_BRUSH, ox, oy);

        // Chisel arrows
        if (this.isCorrectToolSelected() && this.selectedTool == PaleontologyTableMenu.TOOL_CHISEL) {
            this.renderChiselGuide(graphics, ox, oy);
        }

        // Particles generated by tool actions and fossil stage changes
        this.renderFeedback(graphics, ox, oy, partialTick);
        this.renderInstruction(graphics, ox, oy);
        this.renderProgressBars(graphics, ox, oy);
        this.renderCountdown(graphics, ox, oy, partialTick);
    }

    @Override
    public void render(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);

        // If a tool is selected, tool rendering
        if (this.selectedTool >= 0) {
            this.updateToolSwing(mouseX, mouseY);

            graphics.pose().pushPose();

            graphics.pose().translate(mouseX, mouseY, 300.0F);
            graphics.pose().mulPose(Axis.ZP.rotation(this.toolAngle));
            graphics.pose().translate(-mouseX, -mouseY, 0.0F);
            graphics.blit(toolTexture(this.selectedTool), mouseX - HELD_TOOL_WIDTH / 2, mouseY - HELD_TOOL_HEIGHT / 2, HELD_TOOL_WIDTH, HELD_TOOL_HEIGHT, 0, 0, 32, 32, 32, 32);

            graphics.pose().popPose();
        }

        this.renderTooltip(graphics, mouseX, mouseY);
    }

    /// No title
    @Override
    protected void renderLabels(@NonNull GuiGraphics graphics, int mouseX, int mouseY) {
        ;;
    }

    @Override
    public void onClose() {
        if (this.isGameLocked()) {
            this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 0.55F));
            return;
        }

        super.onClose();
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        final int x = (int) mouseX - this.leftPos;
        final int y = (int) mouseY - this.topPos;

        if (button == 0 && this.tracingChisel && this.isCorrectToolSelected() && this.selectedTool == PaleontologyTableMenu.TOOL_CHISEL) {
            this.updateChiselTrace(x, y);
            return true;
        }

        if (button == 0 && this.draggingBrush && this.isCorrectToolSelected()) {
            this.updateBrushCircle(x, y);
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && (this.draggingBrush || this.tracingChisel)) {
            this.resetBrushCircle();
            this.resetChiselTrace();
            return true;
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            final int x = (int) mouseX - this.leftPos;
            final int y = (int) mouseY - this.topPos;

            if (this.selectedTool >= 0 && inside(x, y, 0, 0, TOOL_DROP_WIDTH, GUI_HEIGHT)) {
                this.releaseTool();
                return true;
            }

            if (this.isPlaying() && this.selectedTool < 0) {
                final int tool = isToolAt(x, y);
                if (tool >= 0) {
                    this.selectTool(tool, mouseX, mouseY);
                    return true;
                }
            }

            if (this.isCorrectToolSelected()) {
                if (this.selectedTool == PaleontologyTableMenu.TOOL_CHISEL && this.isNearChiselStart(x, y)) {
                    this.tracingChisel = true;
                    this.chiselTraceProgress = 0.0F;
                    this.chiselGuideAge = 0;
                    return true;
                }

                if (this.selectedTool == PaleontologyTableMenu.TOOL_HAMMER
                        && inside(x, y, FOSSIL_X, FOSSIL_Y, FOSSIL_SIZE, FOSSIL_SIZE)) {
                    this.sendToolAction(PaleontologyTableMenu.TOOL_HAMMER, x, y);
                    return true;
                }

                if (this.selectedTool == PaleontologyTableMenu.TOOL_BRUSH && this.isValidBrushRadius(x, y)) {
                    this.beginBrushCircle(x, y);
                    return true;
                }

            }

        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void updateChiselTrace(float x, float y) {
        final ChiselPathHit hit = this.findClosestChiselPathPoint(x, y);
        final float toleranceSquared = CHISEL_TRACE_TOLERANCE * CHISEL_TRACE_TOLERANCE;
        final boolean leftGuide = hit.distanceSquared() > toleranceSquared;
        final boolean backtracked = hit.progress() + CHISEL_BACKTRACK_TOLERANCE < this.chiselTraceProgress;
        final boolean skippedAhead = hit.progress() > this.chiselTraceProgress + CHISEL_MAX_TRACE_STEP;
        if (leftGuide || backtracked || skippedAhead) {
            this.resetChiselTrace();
            return;
        }

        this.chiselTraceProgress = Math.max(this.chiselTraceProgress, hit.progress());
        this.chiselGuideAge = 0;
        if (this.chiselTraceProgress >= CHISEL_REQUIRED_PROGRESS) {
            final int last = CHISEL_PARTS - 1;
            final int impactX = Math.round(this.chiselPathX[last]);
            final int impactY = Math.round(this.chiselPathY[last]);
            this.sendToolAction(PaleontologyTableMenu.TOOL_CHISEL, impactX, impactY);
            this.moveChiselPath();
        }

    }

    private boolean isNearChiselStart(float x, float y) {
        final float dx = x - this.chiselPathX[0];
        final float dy = y - this.chiselPathY[0];
        return dx * dx + dy * dy <= CHISEL_START_RADIUS * CHISEL_START_RADIUS;
    }

    private void beginBrushCircle(float x, float y) {
        final float centerX = FOSSIL_X + FOSSIL_SIZE / 2.0F;
        final float centerY = FOSSIL_Y + FOSSIL_SIZE / 2.0F;
        this.draggingBrush = true;
        this.brushStartAngle = (float) Math.atan2(y - centerY, x - centerX);
        this.brushLastAngle = this.brushStartAngle;
        this.brushRotation = 0.0F;
        this.brushDustRotation = 0.0F;
        this.brushDirection = 0;
        this.spawnToolParticles(x, y, true, 2);
    }

    private void releaseTool() {
        this.selectedTool = -1;
        this.resetBrushCircle();
        this.resetChiselTrace();
        this.resetToolSwing();
        this.sendMenuButton(PaleontologyTableMenu.BUTTON_RELEASE_TOOL);
    }

    private void selectTool(int tool, double mouseX, double mouseY) {
        this.selectedTool = tool;
        this.lastCursorX = mouseX;
        this.lastCursorY = mouseY;
        this.cursorTracking = true;
        this.toolAngle = 0.0F;
        this.toolAngularVelocity = 0.0F;
        this.sendMenuButton(PaleontologyTableMenu.BUTTON_SELECT_TOOL_BASE + tool);
    }

    private void renderChiselGuide(GuiGraphics graphics, int ox, int oy) {
        final int pathColor = 0x60E9D7AA;
        final int markerColor = 0x80FFF4D2;

        for (int segment = 0; segment < CHISEL_PARTS - 1; segment++) {
            drawLine(graphics, ox + this.chiselPathX[segment], oy + this.chiselPathY[segment], ox + this.chiselPathX[segment + 1], oy + this.chiselPathY[segment + 1], 1, pathColor);
        }

        final float startX = ox + this.chiselPathX[0];
        final float startY = oy + this.chiselPathY[0];
        graphics.fill(Mth.floor(startX - 2.0F), Mth.floor(startY - 2.0F), Mth.ceil(startX + 2.0F), Mth.ceil(startY + 2.0F), markerColor);

        final int last = CHISEL_PARTS - 1;

        final float endX = ox + this.chiselPathX[last];
        final float endY = oy + this.chiselPathY[last];

        float directionX = endX - (ox + this.chiselPathX[last - 1]);
        float directionY = endY - (oy + this.chiselPathY[last - 1]);

        final float length = Mth.sqrt(directionX * directionX + directionY * directionY);
        if (length > 0.0001F) {
            directionX /= length;
            directionY /= length;

            final float size = 3.8F;
            final float normalX = -directionY * size * 0.65F;
            final float normalY = directionX * size * 0.65F;
            final float baseX = endX - directionX * size;

            final float baseY = endY - directionY * size;
            drawLine(graphics, endX, endY, baseX - normalX, baseY - normalY, 2, markerColor);
            drawLine(graphics, endX, endY, baseX + normalX, baseY + normalY, 2, markerColor);
        }

    }

    private static void drawLine(GuiGraphics graphics, float startX, float startY, float endX, float endY, int width, int color) {
        final float dx = endX - startX;
        final float dy = endY - startY;
        final int steps = Math.max(1, Mth.ceil(Math.max(Math.abs(dx), Math.abs(dy))));
        final int halfWidth = width / 2;

        for (int step = 0; step <= steps; step++) {
            final float progress = step / (float) steps;
            final int x = Math.round(startX + dx * progress);
            final int y = Math.round(startY + dy * progress);
            graphics.fill(x - halfWidth, y - halfWidth, x - halfWidth + width, y - halfWidth + width, color);
        }

    }

    private void renderFeedback(GuiGraphics graphics, int ox, int oy, float partialTick) {
        for (final DustParticle particle : this.dustParticles) {
            final float life = Mth.clamp((particle.age + partialTick) / particle.lifetime, 0.0F, 1.0F);
            final int alpha = Mth.clamp((int) ((1.0F - life) * 170.0F), 0, 170);
            final int size = Math.max(3, Math.round(particle.size * (0.75F + life * 0.65F)));
            final int x = ox + (int) Math.round(particle.x + particle.velocityX * partialTick) - size / 2;
            final int y = oy + (int) Math.round(particle.y + particle.velocityY * partialTick) - size / 2;

            if (particle.texture != null) {
                blitTinted(graphics, particle.texture, x, y, size, size, particle.fragmentU, particle.fragmentV, particle.fragmentSize, particle.fragmentSize, FOSSIL_TEXTURE_SIZE, FOSSIL_TEXTURE_SIZE, 0xFFFFFF, alpha);
                continue;
            }

            final ResourceLocation[] textures = particle.brushDust ? BRUSH_DUST_TEXTURES : IMPACT_TEXTURES;
            final int textureSize = particle.brushDust ? 8 : 16;
            final int frame = Math.min(textures.length - 1, (int) (life * textures.length));
            blitTinted(graphics, textures[frame], x, y, size, size, 0, 0, textureSize, textureSize, textureSize, textureSize, 0xD8C3A4, alpha);
        }

    }

    private static void blitTinted(GuiGraphics graphics, ResourceLocation texture, int x, int y, int width, int height, int sourceX, int sourceY, int sourceWidth, int sourceHeight, int textureWidth, int textureHeight, int rgb, int alpha) {
        final float red = (rgb >> 16 & 0xFF) / 255.0F;
        final float green = (rgb >> 8 & 0xFF) / 255.0F;
        final float blue = (rgb & 0xFF) / 255.0F;

        graphics.setColor(red, green, blue, alpha / 255.0F);
        graphics.blit(texture, x, y, width, height, sourceX, sourceY, sourceWidth, sourceHeight, textureWidth, textureHeight);
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private ChiselPathHit findClosestChiselPathPoint(float x, float y) {
        float closestDistanceSquared = Float.MAX_VALUE;
        float closestProgress = 0.0F;

        for (int sample = 0; sample < CHISEL_PARTS - 1; sample++) {
            final float startX = this.chiselPathX[sample];
            final float startY = this.chiselPathY[sample];
            final float segmentX = this.chiselPathX[sample + 1] - startX;
            final float segmentY = this.chiselPathY[sample + 1] - startY;
            final float lengthSquared = segmentX * segmentX + segmentY * segmentY;
            final float projection = lengthSquared <= 0.0001F ? 0.0F : Mth.clamp(((x - startX) * segmentX + (y - startY) * segmentY) / lengthSquared, 0.0F, 1.0F);
            final float nearestX = startX + segmentX * projection;
            final float nearestY = startY + segmentY * projection;
            final float dx = x - nearestX;
            final float dy = y - nearestY;
            final float distanceSquared = dx * dx + dy * dy;

            if (distanceSquared < closestDistanceSquared) {
                closestDistanceSquared = distanceSquared;
                closestProgress = (sample + projection) / (CHISEL_PARTS - 1.0F);
            }

        }

        return new ChiselPathHit(closestProgress, closestDistanceSquared);
    }

    private void updateBrushCircle(float x, float y) {
        if (!this.isValidBrushRadius(x, y)) {
            this.resetBrushCircle();
            return;
        }

        final float centerX = FOSSIL_X + FOSSIL_SIZE / 2.0F;
        final float centerY = FOSSIL_Y + FOSSIL_SIZE / 2.0F;
        final float angle = (float) Math.atan2(y - centerY, x - centerX);
        final float delta = (float) Math.atan2(Math.sin(angle - this.brushLastAngle), Math.cos(angle - this.brushLastAngle));
        this.brushLastAngle = angle;

        if (Math.abs(delta) < 0.004F || Math.abs(delta) > BRUSH_MAX_ANGLE_STEP) {
            return;
        }

        final int direction = delta > 0.0F ? 1 : -1;
        if (this.brushDirection == 0) {
            this.brushDirection = direction;
        }

        if (direction == this.brushDirection) {
            this.brushRotation += Math.abs(delta);
            this.brushDustRotation += Math.abs(delta);
            while (this.brushDustRotation >= BRUSH_DUST_STEP) {
                this.spawnToolParticles(x, y, true, 2);
                this.brushDustRotation -= BRUSH_DUST_STEP;
            }

        }
        else {
            this.brushRotation -= Math.abs(delta) * BRUSH_REVERSE_PENALTY;
            if (this.brushRotation <= 0.0F) {
                this.brushRotation = 0.0F;
                this.brushDirection = direction;
                this.brushStartAngle = angle;
            }

        }

        if (this.brushRotation >= BRUSH_REQUIRED_ROTATION) {
            this.sendToolAction(PaleontologyTableMenu.TOOL_BRUSH, Math.round(x), Math.round(y));
            this.brushRotation -= BRUSH_REQUIRED_ROTATION;
            this.brushStartAngle = angle - this.brushDirection * this.brushRotation;
        }

    }

    private void sendToolAction(int tool, int x, int y) {
        if (this.sendMenuButton(tool)) {
            this.spawnFeedback(tool, x, y);
        }

    }

    private boolean sendMenuButton(int button) {
        if (this.minecraft.player != null && this.minecraft.gameMode != null && this.menu.clickMenuButton(this.minecraft.player, button)) {
            Minecraft.getInstance().gameMode.handleInventoryButtonClick(this.menu.containerId, button);
            return true;
        }

        return false;
    }

    private void spawnFeedback(int tool, int x, int y) {
        final boolean brushDust = tool == PaleontologyTableMenu.TOOL_BRUSH;
        final int particleCount = brushDust ? 7 : tool == PaleontologyTableMenu.TOOL_HAMMER ? 6 : 4;
        this.spawnToolParticles(x, y, brushDust, particleCount);
        this.playToolSound(tool);
    }

    private void playToolSound(int tool) {
        final SoundEvent sound;
        final float pitch;
        final float volume;

        switch (tool) {
            case PaleontologyTableMenu.TOOL_CHISEL -> {
                sound = SoundEvents.COPPER_HIT;
                pitch = 1.20F + this.random.nextFloat() * 0.18F;
                volume = 0.48F;
            }
            case PaleontologyTableMenu.TOOL_HAMMER -> {
                sound = SoundEvents.STONE_HIT;
                pitch = 0.72F + this.random.nextFloat() * 0.12F;
                volume = 0.82F;
            }
            default -> {
                sound = SoundEvents.BRUSH_SAND;
                pitch = 0.92F + this.random.nextFloat() * 0.16F;
                volume = 0.55F;
            }

        }

        this.playUiSound(sound, pitch, volume);
    }

    private void playToolChangeSound() {
        final float pitch = 0.82F + this.menu.getTool() * 0.16F;
        this.playUiSound(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, pitch, 0.72F);
    }

    private void tickTimerSounds(int gameState) {
        if (gameState == PaleontologyTableMenu.STATE_COUNTDOWN) {
            this.seenGlobalSecond = -1;
            final int seconds = Math.max(0, (this.menu.getCountdownTicksRemaining() + 19) / 20);
            if (seconds > 0 && seconds != this.seenCountdownSecond) {
                this.seenCountdownSecond = seconds;
                final float pitch = 0.82F + Math.max(0, 3 - seconds) * 0.18F;
                this.playUiSound(SoundEvents.WOODEN_BUTTON_CLICK_ON, pitch, 0.58F);
            }

            return;
        }

        this.seenCountdownSecond = -1;
        if (gameState != PaleontologyTableMenu.STATE_PLAYING) {
            this.seenGlobalSecond = -1;
            return;
        }

        final int seconds = Math.max(0, (this.menu.getGlobalTicksRemaining() + 19) / 20);
        if (this.seenGlobalSecond < 0) {
            this.seenGlobalSecond = seconds;
            return;
        }

        if (seconds == this.seenGlobalSecond) {
            return;
        }

        this.seenGlobalSecond = seconds;
        final float urgency = Mth.clamp((10.0F - seconds) / 10.0F, 0.0F, 1.0F);
        this.playUiSound(SoundEvents.WOODEN_BUTTON_CLICK_ON, 0.72F + urgency * 0.58F, 0.20F + urgency * 0.34F);
    }

    private void playUiSound(SoundEvent sound, float pitch, float volume) {
        this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(sound, pitch, volume));
    }

    private boolean isValidBrushRadius(float x, float y) {
        final float centerX = FOSSIL_X + FOSSIL_SIZE / 2.0F;
        final float centerY = FOSSIL_Y + FOSSIL_SIZE / 2.0F;
        final float dx = x - centerX;
        final float dy = y - centerY;
        final float radius = Mth.sqrt(dx * dx + dy * dy);
        return radius >= BRUSH_MIN_RADIUS && radius <= BRUSH_MAX_RADIUS;
    }

    private void moveChiselPath() {
        final float centerX = FOSSIL_X + FOSSIL_SIZE / 2.0F + this.random.nextFloat() * 6.0F - 3.0F;
        final float centerY = FOSSIL_Y + FOSSIL_SIZE / 2.0F + this.random.nextFloat() * 6.0F - 3.0F;

        final float angle = this.random.nextFloat() * Mth.TWO_PI;
        final float halfLength = 15.0F + this.random.nextFloat() * 2.5F;

        final float directionX = Mth.cos(angle);
        final float directionY = Mth.sin(angle);

        final float normalX = -directionY;
        final float bend = (this.random.nextBoolean() ? 1.0F : -1.0F) * (2.0F + this.random.nextFloat() * 3.0F);

        final float startX = centerX - directionX * halfLength;
        final float startY = centerY - directionY * halfLength;

        final float endX = centerX + directionX * halfLength;
        final float endY = centerY + directionY * halfLength;

        final float controlX = centerX + normalX * bend;
        final float controlY = centerY + directionX * bend;

        for (int sample = 0; sample < CHISEL_PARTS; sample++) {
            final float progress = sample / (CHISEL_PARTS - 1.0F);
            final float inverse = 1.0F - progress;
            this.chiselPathX[sample] = inverse * inverse * startX + 2.0F * inverse * progress * controlX + progress * progress * endX;
            this.chiselPathY[sample] = inverse * inverse * startY + 2.0F * inverse * progress * controlY + progress * progress * endY;
        }

        this.chiselGuideAge = 0;
        this.resetChiselTrace();
    }

    private void renderCountdown(GuiGraphics graphics, int ox, int oy, float partialTick) {
        if (this.menu.getGameState() != PaleontologyTableMenu.STATE_COUNTDOWN) {
            return;
        }

        final float remaining = Math.max(0.001F, this.menu.getCountdownTicksRemaining() - partialTick);
        final int number = Mth.ceil(remaining / 20.0F);

        final float phase = (number * 20.0F - remaining) / 20.0F;
        final float enter = Mth.clamp(phase / 0.28F, 0.0F, 1.0F);
        final float eased = 1.0F - (float) Math.pow(1.0F - enter, 3.0D);
        final float pop = Mth.clamp((phase - 0.78F) / 0.22F, 0.0F, 1.0F);
        final float scale = 0.7F + eased * 2.8F + pop * 0.75F;
        final int alpha = Mth.clamp((int) ((1.0F - pop) * 255.0F), 0, 255);

        graphics.pose().pushPose();

        graphics.pose().translate(ox + GUI_WIDTH / 2.0F, oy + 58.0F, 250.0F);
        graphics.pose().scale(scale, scale, 1.0F);
        graphics.drawCenteredString(this.font, Integer.toString(number), 0, -this.font.lineHeight / 2, alpha << 24 | 0x00FFE7A0);

        graphics.pose().popPose();
    }

    private void renderInstruction(GuiGraphics graphics, int ox, int oy) {
        final Component instruction;
        int color = 0xFFFFFFFF;

        switch (this.menu.getGameState()) {
            case PaleontologyTableMenu.STATE_PLAYING -> instruction = Component.translatable("gui.nomendubium.tool." + toolName(this.menu.getTool()));
            case PaleontologyTableMenu.STATE_WON -> {
                instruction = Component.translatable("gui.nomendubium.finished");
                color = 0xFF77EE88;
            }
            case PaleontologyTableMenu.STATE_LOST -> {
                instruction = Component.translatable("gui.nomendubium.time_up");
                color = 0xFFFF7777;
            }
            case PaleontologyTableMenu.STATE_COUNTDOWN -> instruction = Component.empty();
            default -> instruction = Component.translatable("gui.nomendubium.insert_fossil");
        }

        final int textWidth = this.font.width(instruction);
        final int textX = ox + INSTRUCTION_AREA_X + Math.max(0, (INSTRUCTION_AREA_WIDTH - textWidth) / 2);
        final int textY = oy + INSTRUCTION_AREA_Y + (INSTRUCTION_AREA_HEIGHT - this.font.lineHeight) / 2;

        graphics.drawString(this.font, instruction, textX, textY, color, true);
    }

    private void renderProgressBars(GuiGraphics graphics, int ox, int oy) {
        if (!this.isPlaying() && this.menu.getGameState() != PaleontologyTableMenu.STATE_WON) {
            return;
        }

        final int height = BAR_HEIGHT - 4;
        final int round = this.menu.getRoundDuration() <= 0 ? 0 : Mth.clamp(this.menu.getRoundTicksRemaining() * height / this.menu.getRoundDuration(), 0, height);
        this.renderProgressBar(graphics, ox + ROUND_BAR_X, oy + BAR_Y, round, 0xFF9F382E, 0xFFF0745D);

        final int seconds = Math.max(0, (this.menu.getGlobalTicksRemaining() + 19) / 20);
        final String time = String.format("%d:%02d", seconds / 60, seconds % 60);
        graphics.drawString(this.font, Component.translatable("gui.nomendubium.time", time), ox + 6, oy + 4, 0xFFFFFFFF, true);

        final int progress = Mth.clamp(this.menu.getProgress() * height / PaleontologyTableMenu.MAX_PROGRESS, 0, height);
        this.renderProgressBar(graphics, ox + PROGRESS_BAR_X, oy + BAR_Y, progress, 0xFFA97832, 0xFFE9C765);
    }

    private void renderProgressBar(GuiGraphics graphics, int x, int y, int filled, int fillColor, int highlightColor) {
        final int right = x + BAR_WIDTH;
        final int bottom = y + BAR_HEIGHT;

        graphics.fill(x + 2, y + 2, right + 2, bottom + 2, 0x66000000);
        graphics.fill(x, y, right, bottom, 0xFF3A291F);
        graphics.renderOutline(x, y, BAR_WIDTH, BAR_HEIGHT, 0xFFE1C28A);
        graphics.fill(x + 2, y + 2, right - 2, bottom - 2, 0xFF1B1512);

        if (filled <= 0) {
            return;
        }

        final int top = bottom - 2 - filled;
        graphics.fill(x + 2, top, right - 2, bottom - 2, fillColor);

        if (filled > 2) {
            graphics.fill(x + 3, top + 1, x + 5, bottom - 3, highlightColor);
        }

        graphics.fill(x + 2, top, right - 2, Math.min(bottom - 2, top + 1), highlightColor);
    }

    private void tickFossilStage() {
        if (!this.menu.hasWorkpiece()) {
            this.seenFossilStage = -1;
            this.fossilPopAge = FOSSIL_POP_DURATION;
            return;
        }

        final int stage = this.getFossilStage();
        if (this.seenFossilStage < 0 || stage < this.seenFossilStage) {
            this.seenFossilStage = stage;
            this.fossilPopAge = FOSSIL_POP_DURATION;
        }
        else if (stage > this.seenFossilStage) {
            this.seenFossilStage = stage;
            this.fossilPopAge = 0;
            this.spawnFossilStageDust();
            this.playUiSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.88F + stage * 0.18F, 0.68F);
        }
        else if (this.fossilPopAge < FOSSIL_POP_DURATION) {
            this.fossilPopAge++;
        }

    }

    private boolean isCorrectToolSelected() {
        return this.isPlaying() && this.selectedTool == this.menu.getTool();
    }

    private void updateToolSwing(double mouseX, double mouseY) {
        if (!this.cursorTracking) {
            this.lastCursorX = mouseX;
            this.lastCursorY = mouseY;
            this.cursorTracking = true;
            return;
        }

        final double dx = mouseX - this.lastCursorX;
        final double dy = mouseY - this.lastCursorY;
        this.lastCursorX = mouseX;
        this.lastCursorY = mouseY;

        final double speed = Math.sqrt(dx * dx + dy * dy);
        final float targetAngle = speed < 0.01D ? 0.0F : (float) (-dx / speed * Math.min(TOOL_SWING_MAX_ANGLE, speed * TOOL_SWING_SPEED_FACTOR));
        this.toolAngularVelocity += (targetAngle - this.toolAngle) * TOOL_SWING_STIFFNESS;
        this.toolAngularVelocity *= TOOL_SWING_DAMPING;
        this.toolAngle += this.toolAngularVelocity;
    }

    private void spawnFossilBreakParticles(ResourceLocation fossilTexture) {
        final float centerX = FOSSIL_X + FOSSIL_SIZE / 2.0F;
        final float centerY = FOSSIL_Y + FOSSIL_SIZE / 2.0F;

        for (int i = 0; i < 52; i++) {
            final double offsetX = (this.random.nextDouble() - 0.5D) * FOSSIL_SIZE * 0.9D;
            final double offsetY = (this.random.nextDouble() - 0.5D) * FOSSIL_SIZE * 0.9D;
            final double distance = Math.max(4.0D, Math.sqrt(offsetX * offsetX + offsetY * offsetY));
            final double speed = 0.18D + this.random.nextDouble() * 0.72D;
            final int size = 2 + this.random.nextInt(4);
            final int bounds = FOSSIL_TEXTURE_SIZE - size;
            this.dustParticles.add(new DustParticle(
                    centerX + offsetX, centerY + offsetY,
                    offsetX / distance * speed + this.random.nextDouble() * 0.5D - 0.25D,
                    offsetY / distance * speed - 0.25D - this.random.nextDouble() * 0.55D,
                    20 + this.random.nextInt(17), 2.0F + this.random.nextFloat() * 3.5F,
                    fossilTexture, this.random.nextInt(bounds + 1),
                    this.random.nextInt(bounds + 1), size));
        }

        this.removeExtraParticles();
    }

    private void spawnFossilStageDust() {
        final float centerX = FOSSIL_X + FOSSIL_SIZE / 2.0F;
        final float centerY = FOSSIL_Y + FOSSIL_SIZE / 2.0F;

        for (int i = 0; i < 26; i++) {
            final double offsetX = (this.random.nextDouble() - 0.5D) * FOSSIL_SIZE * 1.05D;
            final double offsetY = (this.random.nextDouble() - 0.5D) * FOSSIL_SIZE * 0.9D;
            final double distance = Math.max(5.0D, Math.sqrt(offsetX * offsetX + offsetY * offsetY));
            final double speed = 0.08D + this.random.nextDouble() * 0.48D;
            this.dustParticles.add(new DustParticle(
                    centerX + offsetX, centerY + offsetY,
                    offsetX / distance * speed + this.random.nextDouble() * 0.34D - 0.17D,
                    offsetY / distance * speed - 0.08D - this.random.nextDouble() * 0.3D,
                    11 + this.random.nextInt(11), 4.0F + this.random.nextFloat() * 6.0F, true));
        }

        this.removeExtraParticles();
    }

    private void spawnToolParticles(float x, float y, boolean brushDust, int particleCount) {
        for (int i = 0; i < particleCount; i++) {
            this.dustParticles.add(new DustParticle(
                    x + this.random.nextDouble() * 7.0D - 3.5D,
                    y + this.random.nextDouble() * 5.0D - 2.5D,
                    this.random.nextDouble() * (brushDust ? 1.1D : 0.8D) - (brushDust ? 0.55D : 0.4D),
                    -0.15D - this.random.nextDouble() * (brushDust ? 0.35D : 0.5D),
                    9 + this.random.nextInt(brushDust ? 5 : 7),
                    brushDust ? 4.0F + this.random.nextFloat() * 3.5F : 7.5F + this.random.nextFloat() * 4.5F,
                    brushDust));
        }

        this.removeExtraParticles();
    }

    private void tickFeedback() {
        this.dustParticles.removeIf(DustParticle::tick);
    }

    private void removeExtraParticles() {
        while (this.dustParticles.size() > FEEDBACK_LIMIT) {
            this.dustParticles.remove(0);
        }

    }

    private void renderTool(GuiGraphics graphics, ResourceLocation texture, int tool, int ox, int oy) {
        if (this.selectedTool != tool) {
            graphics.blit(texture, ox + TOOL_X, oy + TOOL_Y[tool], TOOL_WIDTH, TOOL_HEIGHT, 0, 0, 32, 32, 32, 32);
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
        return this.menu.getGameState() == PaleontologyTableMenu.STATE_PLAYING;
    }

    private boolean isGameLocked() {
        final int state = this.menu.getGameState();
        return state == PaleontologyTableMenu.STATE_COUNTDOWN || state == PaleontologyTableMenu.STATE_PLAYING;
    }

    private float getFossilPopScale(float partialTick) {
        if (this.fossilPopAge >= FOSSIL_POP_DURATION) {
            return 1.0F;
        }

        final float progress = Mth.clamp((this.fossilPopAge + partialTick) / FOSSIL_POP_DURATION, 0.0F, 1.0F);
        final float eased = 1.0F - (float) Math.pow(1.0F - progress, 3.0D);
        return 0.78F + eased * 0.22F + Mth.sin(progress * Mth.PI) * 0.12F;
    }

    private static ResourceLocation toolTexture(int tool) {
        return switch (tool) {
            case PaleontologyTableMenu.TOOL_CHISEL -> CHISEL;
            case PaleontologyTableMenu.TOOL_HAMMER -> HAMMER;
            default -> BRUSH;
        };

    }

    private static String toolName(int tool) {
        return switch (tool) {
            case PaleontologyTableMenu.TOOL_CHISEL -> "chisel";
            case PaleontologyTableMenu.TOOL_HAMMER -> "hammer";
            default -> "brush";
        };

    }

    private static ResourceLocation[] createParticleTextures(String prefix, int frameCount) {
        final ResourceLocation[] textures = new ResourceLocation[frameCount];
        for (int i = 0; i < textures.length; i++) {
            textures[i] = new ResourceLocation("minecraft", "textures/particle/" + prefix + i + ".png");
        }

        return textures;
    }

    private static ResourceLocation[] createBrushDustTextures() {
        final ResourceLocation[] textures = new ResourceLocation[8];
        for (int i = 0; i < textures.length; i++) {
            textures[i] = new ResourceLocation("minecraft", "textures/particle/generic_" + i + ".png");
        }

        return textures;
    }

    private ResourceLocation getFossilTexture(int stage) {
        if (stage == 0) {
            return FOSSIL_TEXTURES[0];
        }

        final FossilCategory category = this.menu.getFossilCategory();
        final ResourceLocation categoryTexture = NomenDubium.of("textures/gui/fossil_" + category.serializedName() + "_" + stage + ".png");
        final boolean available = this.availableCategoryTextures.computeIfAbsent(categoryTexture, texture -> this.minecraft.getResourceManager().getResource(texture).isPresent());
        return available ? categoryTexture : FOSSIL_TEXTURES[stage];
    }

    private int getFossilStage() {
        return Math.min(FOSSIL_TEXTURES.length - 1, this.menu.getProgress() * FOSSIL_TEXTURES.length / PaleontologyTableMenu.MAX_PROGRESS);
    }

    private void resetToolSwing() {
        this.cursorTracking = false;
        this.toolAngle = 0.0F;
        this.toolAngularVelocity = 0.0F;
    }

    private void resetBrushCircle() {
        this.draggingBrush = false;
        this.brushStartAngle = 0.0F;
        this.brushLastAngle = 0.0F;
        this.brushRotation = 0.0F;
        this.brushDustRotation = 0.0F;
        this.brushDirection = 0;
    }

    private void resetChiselTrace() {
        this.tracingChisel = false;
        this.chiselTraceProgress = 0.0F;
    }

    private record ChiselPathHit(
            float progress,
            float distanceSquared
    ) {
        ;;
    }

    /// 2D cast of a generic particle
    private static final class DustParticle {

        private double x;
        private double y;
        private double velocityX;
        private double velocityY;
        private final int lifetime;
        private final float size;
        private final boolean brushDust;
        private final ResourceLocation texture;
        private final int fragmentU;
        private final int fragmentV;
        private final int fragmentSize;
        private int age;

        private DustParticle(double x, double y, double velocityX, double velocityY, int lifetime, float size, boolean brushDust) {
            this.x = x;
            this.y = y;
            this.velocityX = velocityX;
            this.velocityY = velocityY;
            this.lifetime = lifetime;
            this.size = size;
            this.brushDust = brushDust;
            this.texture = null;
            this.fragmentU = 0;
            this.fragmentV = 0;
            this.fragmentSize = 0;
        }

        private DustParticle(double x, double y, double velocityX, double velocityY, int lifetime, float size, ResourceLocation texture, int fragmentU, int fragmentV, int fragmentSize) {
            this.x = x;
            this.y = y;
            this.velocityX = velocityX;
            this.velocityY = velocityY;
            this.lifetime = lifetime;
            this.size = size;
            this.brushDust = false;
            this.texture = texture;
            this.fragmentU = fragmentU;
            this.fragmentV = fragmentV;
            this.fragmentSize = fragmentSize;
        }

        private boolean tick() {
            this.x += this.velocityX;
            this.y += this.velocityY;
            this.velocityX *= 0.88D;
            this.velocityY = this.velocityY * 0.92D - 0.015D;
            return ++this.age >= this.lifetime;
        }

    }

}
