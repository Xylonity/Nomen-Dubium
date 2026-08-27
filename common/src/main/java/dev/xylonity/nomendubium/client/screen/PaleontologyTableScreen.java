package dev.xylonity.nomendubium.client.screen;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.common.item.fossil.util.FossilCategory;
import dev.xylonity.nomendubium.common.menu.PaleontologyTableMenu;
import dev.xylonity.nomendubium.mixin.GuiGraphicsExtractorAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.joml.Matrix3x2f;
import org.jspecify.annotations.NonNull;

import java.util.*;

/// Some chunks of the implementation are derived from the cornelius companions! screen
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

    private static final Identifier[] IMPACT_TEXTURES = createParticleTextures("big_smoke_", 12);
    private static final Identifier[] BRUSH_DUST_TEXTURES = createBrushDustTextures();

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
    private static final int[] TOOL_Y = { 10, 36, 62 };
    private static final float TOOL_SWING_MAX_ANGLE = 0.45F;
    private static final float TOOL_SWING_SPEED_FACTOR = 0.085F;
    private static final float TOOL_SWING_STIFFNESS = 0.28F;
    private static final float TOOL_SWING_DAMPING = 0.68F;
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
    private final Map<Identifier, Boolean> availableCategoryTextures = new HashMap<>();

    private Identifier lastFossilTexture = FOSSIL_TEXTURES[0];
    private int fossilPopAge = FOSSIL_POP_DURATION;

    private int seenFossilStage = -1;
    private int seenGameState = PaleontologyTableMenu.STATE_IDLE;
    private int seenRound = -1;

    private boolean draggingBrush;
    private boolean tracingChisel;

    private int chiselPathIndex;
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
        super(menu, inventory, title, GUI_WIDTH, GUI_HEIGHT);
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
        }

        this.seenGameState = gameState;

        if (this.menu.getGameState() == PaleontologyTableMenu.STATE_PLAYING && this.menu.getRoundIndex() != this.seenRound) {
            this.seenRound = this.menu.getRoundIndex();
            this.chiselPathIndex = 0;
            this.resetBrushCircle();
            this.moveChiselPath();
        }
        if (this.isCorrectToolSelected() && this.selectedTool == PaleontologyTableMenu.TOOL_CHISEL && !this.tracingChisel && ++this.chiselGuideAge >= CHISEL_GUIDE_DURATION) {
            this.moveChiselPath();
        }

        // Ticks pop animation
        this.tickFossilStage();
        this.tickFeedback();

        if (this.menu.getGameState() != PaleontologyTableMenu.STATE_PLAYING) {
            this.selectedTool = -1;
            this.resetBrushCircle();
            this.resetChiselTrace();
            this.resetToolSwing();
        }

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
        this.extractTool(graphics, CHISEL, PaleontologyTableMenu.TOOL_CHISEL, ox, oy, mouseX, mouseY);
        this.extractTool(graphics, HAMMER, PaleontologyTableMenu.TOOL_HAMMER, ox, oy, mouseX, mouseY);
        this.extractTool(graphics, BRUSH, PaleontologyTableMenu.TOOL_BRUSH, ox, oy, mouseX, mouseY);

        // Chisel arrows
        if (this.isCorrectToolSelected() && this.selectedTool == PaleontologyTableMenu.TOOL_CHISEL) {
            this.extractChiselGuide(graphics, ox, oy);
        }

        // Particles generated by tool actions and fossil stage changes
        this.extractFeedback(graphics, ox, oy, partialTick);

        // Game instructions
        this.extractInstruction(graphics, ox, oy);
        // Progress bars
        this.extractProgressBars(graphics, ox, oy);
        // Game start countdown
        this.extractCountdown(graphics, ox, oy, partialTick);

        // If a tool is selected, tool rendering
        if (this.selectedTool >= 0) {
            final Identifier texture = toolTexture(this.selectedTool);
            this.updateToolSwing();
            graphics.nextStratum();

            graphics.pose().pushMatrix();

            graphics.pose().translate(mouseX, mouseY);
            graphics.pose().rotate(this.toolAngle);
            graphics.pose().translate(-mouseX, -mouseY);
            graphics.blit(RenderPipelines.GUI_TEXTURED, texture, mouseX - HELD_TOOL_WIDTH / 2, mouseY - HELD_TOOL_HEIGHT / 2, 0, 0, HELD_TOOL_WIDTH, HELD_TOOL_HEIGHT, 32, 32, 32, 32);

            graphics.pose().popMatrix();
        }

    }

    /// No title
    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym) {
        ;;
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        final int x = (int)event.x() - this.leftPos;
        final int y = (int)event.y() - this.topPos;
        if (event.button() == 0 && this.tracingChisel && this.isCorrectToolSelected() && this.selectedTool == PaleontologyTableMenu.TOOL_CHISEL) {
            this.updateChiselTrace(x, y);
            return true;
        }

        if (event.button() == 0 && this.draggingBrush && this.isCorrectToolSelected()) {
            this.updateBrushCircle(x, y);
            return true;
        }
        
        return super.mouseDragged(event, dx, dy);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        if (event.button() == 0 && (this.draggingBrush || this.tracingChisel)) {
            this.resetBrushCircle();
            this.resetChiselTrace();
            return true;
        }

        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (event.button() == 0) {
            final int x = (int) event.x() - this.leftPos;
            final int y = (int) event.y() - this.topPos;

            if (this.selectedTool >= 0 && inside(x, y, 0, 0, TOOL_DROP_WIDTH, GUI_HEIGHT)) {
                this.releaseTool();
                return true;
            }

            if (this.isPlaying() && this.selectedTool < 0) {
                int tool = isToolAt(x, y);
                if (tool >= 0) {
                    this.selectTool(tool, event.x(), event.y());
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
                if (this.selectedTool == PaleontologyTableMenu.TOOL_HAMMER && inside(x, y, FOSSIL_X, FOSSIL_Y, FOSSIL_SIZE, FOSSIL_SIZE)) {
                    this.sendToolAction(PaleontologyTableMenu.TOOL_HAMMER, x, y);
                    return true;
                }
                if (this.selectedTool == PaleontologyTableMenu.TOOL_BRUSH && this.isValidBrushRadius(x, y)) {
                    this.beginBrushCircle(x, y);
                    return true;
                }

            }

        }

        return super.mouseClicked(event, doubleClick);
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
        float centerX = FOSSIL_X + FOSSIL_SIZE / 2.0F;
        float centerY = FOSSIL_Y + FOSSIL_SIZE / 2.0F;
        this.draggingBrush = true;
        this.brushStartAngle = (float)Math.atan2(y - centerY, x - centerX);
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
        this.toolAngle = 0;
        this.toolAngularVelocity = 0;
        this.sendMenuButton(PaleontologyTableMenu.BUTTON_SELECT_TOOL_BASE + tool);
    }

    private void extractChiselGuide(GuiGraphicsExtractor graphics, int ox, int oy) {
        graphics.nextStratum();
        ((GuiGraphicsExtractorAccessor) graphics).nomendubium$getGuiRenderState().addGuiElement(new ChiselGuideRenderState(new Matrix3x2f(graphics.pose()), this.chiselPathX, this.chiselPathY, ox, oy, null));
    }

    private void extractFeedback(GuiGraphicsExtractor graphics, int ox, int oy, float partialTick) {
        if (this.dustParticles.isEmpty()) {
            return;
        }

        graphics.nextStratum();
        for (final DustParticle particle : this.dustParticles) {
            final float life = Mth.clamp((particle.age + partialTick) / particle.lifetime, 0.0F, 1.0F);
            final int alpha = Mth.clamp((int)((1.0F - life) * 170.0F), 0, 170);
            final int size = Math.max(3, Math.round(particle.size * (0.75F + life * 0.65F)));
            final int x = ox + (int)Math.round(particle.x + particle.velocityX * partialTick) - size / 2;
            final int y = oy + (int)Math.round(particle.y + particle.velocityY * partialTick) - size / 2;

            if (particle.texture != null) {
                graphics.blit(RenderPipelines.GUI_TEXTURED, particle.texture, x, y, particle.fragmentU, particle.fragmentV, size, size, particle.fragmentSize, particle.fragmentSize, FOSSIL_TEXTURE_SIZE, FOSSIL_TEXTURE_SIZE, alpha << 24 | 0x00FFFFFF);
                continue;
            }

            final Identifier[] textures = particle.brushDust ? BRUSH_DUST_TEXTURES : IMPACT_TEXTURES;
            final int textureSize = particle.brushDust ? 8 : 16;
            final int frame = Math.min(textures.length - 1, (int)(life * textures.length));
            graphics.blit(RenderPipelines.GUI_TEXTURED, textures[frame], x, y, 0, 0, size, size, textureSize, textureSize, textureSize, textureSize, alpha << 24 | 0x00D8C3A4);
        }

    }

    private ChiselPathHit findClosestChiselPathPoint(float x, float y) {
        float closestDistanceSqr = Float.MAX_VALUE;
        float closestProgress = 0.0F;
        for (int sample = 0; sample < CHISEL_PARTS - 1; sample++) {
            final float startX = this.chiselPathX[sample];
            final float startY = this.chiselPathY[sample];
            final float segmentX = this.chiselPathX[sample + 1] - startX;
            final float segmentY = this.chiselPathY[sample + 1] - startY;
            final float lengthSquared = segmentX * segmentX + segmentY * segmentY;
            final float projection = lengthSquared <= 0.0001 ? 0 : Mth.clamp(((x - startX) * segmentX + (y - startY) * segmentY) / lengthSquared, 0, 1);
            final float nearestX = startX + segmentX * projection;
            final float nearestY = startY + segmentY * projection;
            final float dx = x - nearestX;
            final float dy = y - nearestY;
            final float distanceSquared = dx * dx + dy * dy;
            if (distanceSquared < closestDistanceSqr) {
                closestDistanceSqr = distanceSquared;
                closestProgress = (sample + projection) / (CHISEL_PARTS - 1.0F);
            }

        }

        return new ChiselPathHit(closestProgress, closestDistanceSqr);
    }

    private void updateBrushCircle(float x, float y) {
        if (!this.isValidBrushRadius(x, y)) {
            this.resetBrushCircle();
            return;
        }

        final float centerX = FOSSIL_X + FOSSIL_SIZE / 2.0F;
        final float centerY = FOSSIL_Y + FOSSIL_SIZE / 2.0F;
        final float angle = (float)Math.atan2(y - centerY, x - centerX);
        final float delta = (float)Math.atan2(Math.sin(angle - this.brushLastAngle), Math.cos(angle - this.brushLastAngle));

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
        if (this.menu.clickMenuButton(this.minecraft.player, button)) {
            Minecraft.getInstance().gameMode.handleInventoryButtonClick(this.menu.containerId, button);
            return true;
        }

        return false;
    }

    private void spawnFeedback(int tool, int x, int y) {
        final boolean brushDust = tool == PaleontologyTableMenu.TOOL_BRUSH;
        final int particleCount = brushDust ? 7 : tool == PaleontologyTableMenu.TOOL_HAMMER ? 6 : 4;
        this.spawnToolParticles(x, y, brushDust, particleCount);
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
        final float centerX = FOSSIL_X + FOSSIL_SIZE / 2f + random.nextFloat() * 6f - 3f;
        final float centerY = FOSSIL_Y + FOSSIL_SIZE / 2f + random.nextFloat() * 6f - 3f;
        final float angle = random.nextFloat() * Mth.TWO_PI;
        final float halfLength = 15 + random.nextFloat() * 2.5F;
        final float directionX = Mth.cos(angle);
        final float directionY = Mth.sin(angle);
        final float normalX = -directionY;
        final float bend = (random.nextBoolean() ? 1 : -1) * (2f + random.nextFloat() * 3f);
        final float startX = centerX - directionX * halfLength;
        final float startY = centerY - directionY * halfLength;
        final float endX = centerX + directionX * halfLength;
        final float endY = centerY + directionY * halfLength;
        final float controlX = centerX + normalX * bend;
        final float controlY = centerY + directionX * bend;

        for (int sample = 0; sample < CHISEL_PARTS; sample++) {
            final float sam = sample / (CHISEL_PARTS - 1f);
            final float inverse = 1.0F - sam;
            this.chiselPathX[sample] = inverse * inverse * startX + 2.0F * inverse * sam * controlX + sam * sam * endX;
            this.chiselPathY[sample] = inverse * inverse * startY + 2.0F * inverse * sam * controlY + sam * sam * endY;
        }

        this.chiselGuideAge = 0;
        this.resetChiselTrace();
    }

    private void extractCountdown(GuiGraphicsExtractor graphics, int ox, int oy, float partialTick) {
        if (this.menu.getGameState() != PaleontologyTableMenu.STATE_COUNTDOWN) {
            return;
        }

        final float elapsed = Mth.clamp(PaleontologyTableMenu.COUNTDOWN_DURATION - this.menu.getCountdownTicksRemaining() + partialTick, 0, PaleontologyTableMenu.COUNTDOWN_DURATION - 0.001F);
        final int segment = Math.min(2, (int)(elapsed / 20f));
        final int number = 3 - segment;
        final float phase = (elapsed - segment * 20f) / 20f;
        final float enter = Mth.clamp(phase / 0.28F, 0, 1);
        final float eased = 1.0F - (float)Math.pow(1.0F - enter, 3.0D);
        final float pop = Mth.clamp((phase - 0.78F) / 0.22f, 0, 1);
        final float scale = 0.7F + eased * 2.8F + pop * 0.75F;
        final int alpha = Mth.clamp((int)((1.0F - pop) * 255.0F), 0, 255);

        graphics.nextStratum();
        graphics.pose().pushMatrix();

        graphics.pose().translate(ox + GUI_WIDTH / 2f, oy + 58);
        graphics.pose().scale(scale, scale);
        graphics.centeredText(this.font, Integer.toString(number), 0, -this.font.lineHeight / 2, alpha << 24 | 0x00FFE7A0);

        graphics.pose().popMatrix();
    }

    private void extractInstruction(GuiGraphicsExtractor graphics, int ox, int oy) {
        final Component instruction;
        int color = 0xFFFFFFFF;
        switch (this.menu.getGameState()) {
            case PaleontologyTableMenu.STATE_PLAYING -> instruction = Component.translatable(
                    "gui.nomendubium.tool." + toolName(this.menu.getTool())
            );
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

        graphics.text(this.font, instruction, textX, textY, color, true);
    }

    private void extractProgressBars(GuiGraphicsExtractor graphics, int ox, int oy) {
        if (!this.isPlaying() && this.menu.getGameState() != PaleontologyTableMenu.STATE_WON) {
            return;
        }

        final int height = BAR_HEIGHT - 4;
        final int round = this.menu.getRoundDuration() <= 0 ? 0 : Mth.clamp(this.menu.getRoundTicksRemaining() * height / this.menu.getRoundDuration(), 0, height);
        this.extractProgressBar(graphics, ox + ROUND_BAR_X, oy + BAR_Y, round, 0xFF9F382E, 0xFFF0745D);

        final int seconds = Math.max(0, (this.menu.getGlobalTicksRemaining() + 19) / 20);
        final String time = String.format("%d:%02d", seconds / 60, seconds % 60);

        graphics.text(this.font, Component.translatable("gui.nomendubium.time", time), ox + 6, oy + 4, 0xFFFFFFFF, true);

        final int progress = Mth.clamp(this.menu.getProgress() * height / PaleontologyTableMenu.MAX_PROGRESS, 0, height);
        this.extractProgressBar(graphics, ox + PROGRESS_BAR_X, oy + BAR_Y, progress, 0xFFA97832, 0xFFE9C765);
    }

    private void extractProgressBar(GuiGraphicsExtractor graphics, int x, int y, int filled, int fillColor, int color) {
        final int right = x + BAR_WIDTH;
        final int bottom = y + BAR_HEIGHT;

        graphics.fill(x + 2, y + 2, right + 2, bottom + 2, 0x66000000);
        graphics.fill(x, y, right, bottom, 0xFF3A291F);
        graphics.outline(x, y, BAR_WIDTH, BAR_HEIGHT, 0xFFE1C28A);
        graphics.fill(x + 2, y + 2, right - 2, bottom - 2, 0xFF1B1512);

        if (filled <= 0) {
            return;
        }

        final int top = bottom - 2 - filled;
        graphics.fill(x + 2, top, right - 2, bottom - 2, fillColor);
        if (filled > 2) {
            graphics.fill(x + 3, top + 1, x + 5, bottom - 3, color);
        }

        graphics.fill(x + 2, top, right - 2, Math.min(bottom - 2, top + 1), color);
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
        }
        else if (this.fossilPopAge < FOSSIL_POP_DURATION) {
            this.fossilPopAge++;
        }

    }

    private boolean isCorrectToolSelected() {
        return this.isPlaying() && this.selectedTool == this.menu.getTool();
    }

    private void updateToolSwing() {
        final double mouseX = this.minecraft.mouseHandler.getScaledXPos(this.minecraft.getWindow());
        final double mouseY = this.minecraft.mouseHandler.getScaledYPos(this.minecraft.getWindow());
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
        final float targetAngle = speed < 0.001 ? 0.0F : (float)(-dx / speed * Math.min(TOOL_SWING_MAX_ANGLE, speed * TOOL_SWING_SPEED_FACTOR));

        this.toolAngularVelocity += (targetAngle - this.toolAngle) * TOOL_SWING_STIFFNESS;
        this.toolAngularVelocity *= TOOL_SWING_DAMPING;
        this.toolAngle = Mth.clamp(this.toolAngle + this.toolAngularVelocity, -TOOL_SWING_MAX_ANGLE, TOOL_SWING_MAX_ANGLE);
    }

    private void spawnFossilBreakParticles(Identifier fossilTexture) {
        final float centerX = FOSSIL_X + FOSSIL_SIZE / 2f;
        final float centerY = FOSSIL_Y + FOSSIL_SIZE / 2f;
        for (int i = 0; i < 52; i++) {
            final double offsetX = (this.random.nextDouble() - 0.5) * FOSSIL_SIZE * 0.9;
            final double offsetY = (this.random.nextDouble() - 0.5) * FOSSIL_SIZE * 0.9;
            final double distance = Math.max(4.0D, Math.sqrt(offsetX * offsetX + offsetY * offsetY));
            final double speed = 0.18D + this.random.nextDouble() * 0.72D;
            final int size = 2 + this.random.nextInt(4);
            final int bounds = FOSSIL_TEXTURE_SIZE - size;
            this.dustParticles.add(new DustParticle(
                    centerX + offsetX, centerY + offsetY,
                    offsetX / distance * speed + this.random.nextDouble() * 0.5 - 0.25,
                    offsetY / distance * speed - 0.25 - this.random.nextDouble() * 0.55,
                    20 + this.random.nextInt(17), 2 + this.random.nextFloat() * 3.5f, fossilTexture,
                    this.random.nextInt(bounds + 1),
                    this.random.nextInt(bounds + 1), size)
            );

        }

        this.removeExtraParticles();
    }

    private void spawnFossilStageDust() {
        final float centerX = FOSSIL_X + FOSSIL_SIZE / 2f;
        final float centerY = FOSSIL_Y + FOSSIL_SIZE / 2f;
        for (int i = 0; i < 26; i++) {
            final double offsetX = (this.random.nextDouble() - 0.5D) * FOSSIL_SIZE * 1.05;
            final double offsetY = (this.random.nextDouble() - 0.5D) * FOSSIL_SIZE * 0.9;
            final double distance = Math.max(5.0D, Math.sqrt(offsetX * offsetX + offsetY * offsetY));
            final double speed = 0.08D + this.random.nextDouble() * 0.48D;
            this.dustParticles.add(new DustParticle(
                    centerX + offsetX, centerY + offsetY,
                    offsetX / distance * speed + this.random.nextDouble() * 0.34 - 0.17,
                    offsetY / distance * speed - 0.08 - this.random.nextDouble() * 0.3,
                    11 + this.random.nextInt(11), 4 + this.random.nextFloat() * 6f, true
            ));

        }

        this.removeExtraParticles();
    }

    private void spawnToolParticles(float x, float y, boolean brushDust, int particleCount) {
        for (int i = 0; i < particleCount; i++) {
            this.dustParticles.add(new DustParticle(
                    x + this.random.nextDouble() * 7d - 3.5,
                    y + this.random.nextDouble() * 5d - 2.5,
                    this.random.nextDouble() * (brushDust ? 1.1D : 0.8D) - (brushDust ? 0.55D : 0.4D),
                    -0.15D - this.random.nextDouble() * (brushDust ? 0.35D : 0.5D),
                    9 + this.random.nextInt(brushDust ? 5 : 7),
                    brushDust ? 4.0F + this.random.nextFloat() * 3.5F : 7.5F + this.random.nextFloat() * 4.5F, brushDust
            ));

        }

        removeExtraParticles();
    }

    private void tickFeedback() {
        this.dustParticles.removeIf(DustParticle::tick);
    }

    private void removeExtraParticles() {
        while (this.dustParticles.size() > FEEDBACK_LIMIT) {
            this.dustParticles.removeFirst();
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
        return this.menu.getGameState() == PaleontologyTableMenu.STATE_PLAYING;
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

    private static String toolName(int tool) {
        return switch (tool) {
            case PaleontologyTableMenu.TOOL_CHISEL -> "chisel";
            case PaleontologyTableMenu.TOOL_HAMMER -> "hammer";
            default -> "brush";
        };

    }

    private static Identifier[] createParticleTextures(String prefix, int frameCount) {
        final Identifier[] textures = new Identifier[frameCount];
        for (int i = 0; i < textures.length; i++) {
            textures[i] = Identifier.withDefaultNamespace("textures/particle/" + prefix + i + ".png");
        }

        return textures;
    }

    private static Identifier[] createBrushDustTextures() {
        final Identifier[] textures = new Identifier[8];
        for (int i = 0; i < textures.length; i++) {
            textures[i] = Identifier.withDefaultNamespace("textures/particle/generic_" + i + ".png");
        }

        return textures;
    }

    private Identifier getFossilTexture(int stage) {
        final FossilCategory category = this.menu.getFossilCategory();
        final Identifier categoryTexture = NomenDubium.of("textures/gui/fossil_" + category.serializedName() + "_" + (stage + 1) + ".png");
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
        this.chiselTraceProgress = 0;
    }

    private record ChiselPathHit(
            float progress,
            float distanceSquared
    ) {
        ;;
    }

    private static final class ChiselGuideRenderState implements GuiElementRenderState {

        private static final int COLOR = 0x80FFF4D2;
        private static final int PARTS = 12;
        private static final float MARGIN = 7;

        private final Matrix3x2f pose;
        private final float[] pathX;
        private final float[] pathY;
        private final ScreenRectangle scissorArea;
        private final ScreenRectangle bounds;

        private ChiselGuideRenderState(Matrix3x2f pose, float[] pathX, float[] pathY, float offsetX, float offsetY, ScreenRectangle scissorArea) {
            this.pose = pose;
            this.pathX = pathX.clone();
            this.pathY = pathY.clone();
            this.scissorArea = scissorArea;

            float minX = Float.MAX_VALUE;
            float minY = Float.MAX_VALUE;
            float maxX = -Float.MAX_VALUE;
            float maxY = -Float.MAX_VALUE;
            for (int sample = 0; sample < this.pathX.length; sample++) {
                this.pathX[sample] += offsetX;
                this.pathY[sample] += offsetY;
                minX = Math.min(minX, this.pathX[sample]);
                minY = Math.min(minY, this.pathY[sample]);
                maxX = Math.max(maxX, this.pathX[sample]);
                maxY = Math.max(maxY, this.pathY[sample]);
            }

            final int left = Mth.floor(minX - MARGIN);
            final int top = Mth.floor(minY - MARGIN);
            final int right = Mth.ceil(maxX + MARGIN);
            final int bottom = Mth.ceil(maxY + MARGIN);
            final ScreenRectangle bounds = new ScreenRectangle(left, top, right - left, bottom - top).transformMaxBounds(pose);
            this.bounds = scissorArea == null ? bounds : scissorArea.intersection(bounds);
        }

        @Override
        public void buildVertices(@NonNull VertexConsumer vertices) {
            addPath(vertices, this.pose, this.pathX, this.pathY, 1.0F, 0x60E9D7AA);
            addPoint(vertices, this.pose, this.pathX[0], this.pathY[0], 1.8F, COLOR);

            final int last = this.pathX.length - 1;
            addArrow(vertices, this.pose, this.pathX[last - 1], this.pathY[last - 1], this.pathX[last], this.pathY[last], 3.8F, COLOR);
        }

        @Override
        public RenderPipeline pipeline() {
            return RenderPipelines.GUI;
        }

        @Override
        public TextureSetup textureSetup() {
            return TextureSetup.noTexture();
        }

        @Override
        public ScreenRectangle scissorArea() {
            return this.scissorArea;
        }

        @Override
        public ScreenRectangle bounds() {
            return this.bounds;
        }

        private static void addPath(VertexConsumer vertices, Matrix3x2f pose, float[] pathX, float[] pathY, float width, int color) {
            for (int segment = 0; segment < pathX.length - 1; segment++) {
                addRibbonSegment(vertices, pose, pathX[segment], pathY[segment], pathX[segment + 1], pathY[segment + 1], width, color);
            }

        }

        private static void addRibbonSegment(VertexConsumer vertices, Matrix3x2f pose, float startX, float startY, float endX, float endY, float width, int color) {
            final float directionX = endX - startX;
            final float directionY = endY - startY;
            final float length = Mth.sqrt(directionX * directionX + directionY * directionY);
            if (length <= 0.0001) {
                return;
            }

            final float normalX = -directionY / length * width * 0.5F;
            final float normalY = directionX / length * width * 0.5F;

            addVertex(vertices, pose, startX - normalX, startY - normalY, color);
            addVertex(vertices, pose, startX + normalX, startY + normalY, color);
            addVertex(vertices, pose, endX + normalX, endY + normalY, color);
            addVertex(vertices, pose, endX - normalX, endY - normalY, color);
        }

        private static void addPoint(VertexConsumer vertices, Matrix3x2f pose, float centerX, float centerY, float radius, int color) {
            for (int segment = 0; segment < PARTS; segment++) {
                final float angle0 = Mth.TWO_PI * segment / PARTS;
                final float angle1 = Mth.TWO_PI * (segment + 1) / PARTS;
                addVertex(vertices, pose, centerX, centerY, color);
                addVertex(vertices, pose, centerX + Mth.cos(angle1) * radius, centerY + Mth.sin(angle1) * radius, color);
                addVertex(vertices, pose, centerX + Mth.cos(angle0) * radius, centerY + Mth.sin(angle0) * radius, color);
                addVertex(vertices, pose, centerX, centerY, color);
            }

        }

        private static void addArrow(VertexConsumer vertices, Matrix3x2f pose, float previousX, float previousY, float endX, float endY, float size, int color) {
            float directionX = endX - previousX;
            float directionY = endY - previousY;
            final float length = Mth.sqrt(directionX * directionX + directionY * directionY);
            if (length <= 0.0001) {
                return;
            }

            directionX /= length;
            directionY /= length;

            final float normalX = -directionY * size * 0.65F;
            final float normalY = directionX * size * 0.65F;
            final float baseX = endX - directionX * size;
            final float baseY = endY - directionY * size;

            addVertex(vertices, pose, endX, endY, color);
            addVertex(vertices, pose, baseX - normalX, baseY - normalY, color);
            addVertex(vertices, pose, baseX + normalX, baseY + normalY, color);
            addVertex(vertices, pose, endX, endY, color);
        }

        private static void addVertex(VertexConsumer vertices, Matrix3x2f pose, float x, float y, int color) {
            vertices.addVertexWith2DPose(pose, x, y).setColor(color);
        }

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
        private final Identifier texture;
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

        private DustParticle(double x, double y, double velocityX, double velocityY, int lifetime, float size, Identifier texture, int fragmentU, int fragmentV, int fragmentSize) {
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