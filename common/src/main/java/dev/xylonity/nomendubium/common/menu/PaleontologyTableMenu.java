package dev.xylonity.nomendubium.common.menu;

import dev.xylonity.nomendubium.common.item.fossil.util.FossilCategory;
import dev.xylonity.nomendubium.common.item.util.ItemStackData;
import dev.xylonity.nomendubium.config.NomenDubiumConfig;
import dev.xylonity.nomendubium.registry.NomenDubiumItems;
import dev.xylonity.nomendubium.registry.NomenDubiumMenus;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;

public class PaleontologyTableMenu extends AbstractContainerMenu {

    public static final int STATE_IDLE = 0;
    public static final int STATE_PLAYING = 1;
    public static final int STATE_WON = 2;
    public static final int STATE_LOST = 3;
    public static final int STATE_COUNTDOWN = 4;

    public static final int TOOL_CHISEL = 0;
    public static final int TOOL_HAMMER = 1;
    public static final int TOOL_BRUSH = 2;

    private static final int DATA_STATE = 0;
    private static final int DATA_TOOL = 1;
    private static final int DATA_ROUND_REMAINING = 2;
    private static final int DATA_ROUND_DURATION = 3;
    private static final int DATA_GLOBAL_REMAINING = 4;
    private static final int DATA_PROGRESS = 5;
    private static final int DATA_ROUND_INDEX = 6;
    private static final int DATA_COUNTDOWN_REMAINING = 7;
    private static final int DATA_HELD_TOOL = 8;
    private static final int DATA_FOSSIL_CATEGORY = 9;
    private static final int DATA_COUNT = 10;

    private static final int TABLE_SLOT = 0;
    private static final int PLAYER_SLOT_START = 1;

    public static final int BUTTON_SELECT_TOOL_BASE = 10;
    public static final int BUTTON_RELEASE_TOOL = 13;

    public static final int MAX_PROGRESS = 600;

    private final int[] gameData = new int[DATA_COUNT];

    private final Container container;
    private final ContainerLevelAccess access;
    private final Player player;

    private long lastGameTick = Long.MIN_VALUE;
    private long lastActionTick = Long.MIN_VALUE;

    private int actionsThisRound;

    public PaleontologyTableMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(1), ContainerLevelAccess.NULL);
    }

    public PaleontologyTableMenu(int containerId, Inventory inventory, Container container, ContainerLevelAccess access) {
        super(NomenDubiumMenus.PALEONTOLOGY_TABLE.get(), containerId);
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

        this.addPlayerInventory(inventory, 48, 129);

        for (int i = 0; i < DATA_COUNT; i++) {
            this.addDataSlot(DataSlot.shared(this.gameData, i));
        }

    }

    @Override
    public @NonNull ItemStack quickMoveStack(@NonNull Player player, int i) {
        if (i < 0 || i >= this.slots.size()) {
            return ItemStack.EMPTY;
        }

        final Slot slot = this.slots.get(i);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        // Keeps a copy because moving the stack modifies the stack itself
        final ItemStack stack = slot.getItem();
        final ItemStack original = stack.copy();

        // If the clicked slot is the table one
        if (i == TABLE_SLOT) {
            // The encased fossil stays locked in the table, while a finished reward can be taken out
            if (stack.is(NomenDubiumItems.ENCASED_FOSSIL.get())) {
                return ItemStack.EMPTY;
            }

            if (!this.moveItemStackTo(stack, PLAYER_SLOT_START, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }

        }
        // Only encased fossils can be moved from the player inventory into the table slot
        else if (!stack.is(NomenDubiumItems.ENCASED_FOSSIL.get()) || !this.moveItemStackTo(stack, TABLE_SLOT, TABLE_SLOT + 1, false)) {
            return ItemStack.EMPTY;
        }

        // Clears the source slot if everything moved
        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        }
        else {
            slot.setChanged();
        }

        return original;
    }

    private void addPlayerInventory(Inventory inventory, int x, int y) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                this.addSlot(new Slot(inventory, column + row * 9 + 9, x + column * 18, y + row * 18));
            }

        }

        for (int column = 0; column < 9; column++) {
            this.addSlot(new Slot(inventory, column, x + column * 18, y + 58));
        }

    }

    @Override
    public void removed(@NonNull Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }

    @Override
    public boolean stillValid(@NonNull Player player) {
        return this.container.stillValid(player);
    }

    private int get(int index) {
        return this.gameData[index];
    }

    private void set(int index, int value) {
        this.gameData[index] = value;
    }

    @Override
    public boolean clickMenuButton(Player player, int buttonId) {
        // Tool selection uses separated button ids so they don't clash with the action ids below
        if (buttonId >= BUTTON_SELECT_TOOL_BASE && buttonId < BUTTON_SELECT_TOOL_BASE + 3) {
            if (this.getGameState() != STATE_PLAYING) {
                return false;
            }

            this.set(DATA_HELD_TOOL, buttonId - BUTTON_SELECT_TOOL_BASE);

            return true;
        }

        // Releasing the tool stops the round timer until the correct one is held again
        if (buttonId == BUTTON_RELEASE_TOOL) {
            if (this.getGameState() != STATE_PLAYING) {
                return false;
            }

            this.set(DATA_HELD_TOOL, -1);

            return true;
        }

        // Ignores invalid actions
        if (buttonId < TOOL_CHISEL || buttonId > TOOL_BRUSH || this.getGameState() != STATE_PLAYING || this.getTool() != buttonId || this.getHeldTool() != buttonId) {
            return false;
        }

        if (player.level().isClientSide()) {
            return true;
        }

        if (!this.container.getItem(TABLE_SLOT).is(NomenDubiumItems.ENCASED_FOSSIL.get())) {
            return false;
        }

        // Limits actions to one per tick
        final long tick = player.level().getGameTime();
        if (tick == this.lastActionTick || this.actionsThisRound >= maxActions(buttonId)) {
            return false;
        }

        this.lastActionTick = tick;
        this.actionsThisRound++;

        this.set(DATA_PROGRESS, Math.min(MAX_PROGRESS, this.get(DATA_PROGRESS) + progressPerAction(buttonId)));

        // Reaching the cap immediately turns the encased fossil into the reward per se
        if (this.get(DATA_PROGRESS) >= MAX_PROGRESS) {
            this.finishGame();
        }

        return true;
    }

    @Override
    public void broadcastChanges() {
        if (!this.player.level().isClientSide()) {
            this.tick();
        }

        super.broadcastChanges();
    }

    private void tick() {
        // So it doesn't tick twice, just in case
        final long tick = this.player.level().getGameTime();
        if (tick == this.lastGameTick) {
            return;
        }

        this.lastGameTick = tick;

        // The game only runs while an encased fossil is in the table slot
        final ItemStack workpiece = this.container.getItem(TABLE_SLOT);
        final boolean hasEncasedFossil = workpiece.is(NomenDubiumItems.ENCASED_FOSSIL.get());

        if (!hasEncasedFossil) {
            if (workpiece.isEmpty()) {
                // Keeps the lost state visible after the encased fossil has been consumed
                if (this.getGameState() != STATE_LOST) {
                    this.resetGame();
                }

            }
            // A different item interrupts the current attempt without starting a new one (if the reward is in the slot)
            else if (this.getGameState() == STATE_PLAYING || this.getGameState() == STATE_COUNTDOWN) {
                this.set(DATA_STATE, STATE_IDLE);
                this.set(DATA_HELD_TOOL, -1);
            }

            return;
        }

        // Syncs the fossil category so the client can render the matching fossil
        final FossilCategory category = this.getOrAssignCategory(workpiece);
        this.set(DATA_FOSSIL_CATEGORY, category.ordinal());

        // A newly inserted fossil begins with a fresh countdown
        if (this.getGameState() == STATE_IDLE || this.getGameState() == STATE_LOST) {
            this.startCountdown();
            return;
        }

        // Finishes the countdown before any round timer or player action can progress
        if (this.getGameState() == STATE_COUNTDOWN) {
            this.set(DATA_COUNTDOWN_REMAINING, this.get(DATA_COUNTDOWN_REMAINING) - 1);
            if (this.get(DATA_COUNTDOWN_REMAINING) <= 0) {
                this.startGame();
            }

            return;
        }

        // Terminal states stay frozen until the workpiece changes
        if (this.getGameState() != STATE_PLAYING) {
            return;
        }

        // Global time always advances but the round time only advances with the requested tool held
        this.set(DATA_GLOBAL_REMAINING, this.get(DATA_GLOBAL_REMAINING) - 1);
        if (this.getHeldTool() == this.getTool()) {
            this.set(DATA_ROUND_REMAINING, this.get(DATA_ROUND_REMAINING) - 1);
        }

        // The global countdown takes priority if both timers expire on the same tick
        if (this.get(DATA_GLOBAL_REMAINING) <= 0) {
            this.set(DATA_GLOBAL_REMAINING, 0);
            this.failGame();
        }
        else if (this.get(DATA_ROUND_REMAINING) <= 0) {
            this.startNextRound();
        }

    }

    public int getGameState() {
        return this.get(DATA_STATE);
    }

    public int getTool() {
        return this.get(DATA_TOOL);
    }

    public int getRoundTicksRemaining() {
        return this.get(DATA_ROUND_REMAINING);
    }

    public int getRoundDuration() {
        return this.get(DATA_ROUND_DURATION);
    }

    public int getGlobalTicksRemaining() {
        return this.get(DATA_GLOBAL_REMAINING);
    }

    public int getProgress() {
        return this.get(DATA_PROGRESS);
    }

    public int getRoundIndex() {
        return this.get(DATA_ROUND_INDEX);
    }

    public int getCountdownTicksRemaining() {
        return this.get(DATA_COUNTDOWN_REMAINING);
    }

    public int getHeldTool() {
        return this.get(DATA_HELD_TOOL);
    }

    public FossilCategory getFossilCategory() {
        return FossilCategory.index(this.get(DATA_FOSSIL_CATEGORY));
    }

    public boolean hasWorkpiece() {
        return !this.container.getItem(TABLE_SLOT).isEmpty();
    }

    private FossilCategory getOrAssignCategory(ItemStack workpiece) {
        final CompoundTag workpieceData = ItemStackData.get(workpiece);
        FossilCategory category = FossilCategory.name(workpieceData.contains("FossilCategory") ? workpieceData.getString("FossilCategory") : null);
        if (category == null) {
            category = FossilCategory.random(this.player.level().getRandom());
            final FossilCategory selectedCategory = category;
            ItemStackData.update(workpiece, tag -> tag.putString("FossilCategory", selectedCategory.serializedName()));
            this.container.setChanged();
        }

        return category;
    }

    // Resets the previous attempt and gives the player a moment to get ready
    private void startCountdown() {
        this.set(DATA_STATE, STATE_COUNTDOWN);
        this.set(DATA_COUNTDOWN_REMAINING, NomenDubiumConfig.PALEONTOLOGY_TABLE_COUNTDOWN_DURATION * 20);
        this.set(DATA_GLOBAL_REMAINING, globalDuration());
        this.set(DATA_PROGRESS, 0);
        this.set(DATA_ROUND_INDEX, 0);
        this.set(DATA_HELD_TOOL, -1);
    }

    // Starts the first round with a random tool once the countdown is over
    private void startGame() {
        this.set(DATA_STATE, STATE_PLAYING);
        // Specifies a random tool among the 3 available
        this.set(DATA_TOOL, this.player.level().getRandom().nextInt(3));
        this.set(DATA_COUNTDOWN_REMAINING, 0);
        this.beginRound(NomenDubiumConfig.PALEONTOLOGY_TABLE_INITIAL_ROUND_DURATION);
    }

    // Changes to a different tool and gives the player less time on each new round
    private void startNextRound() {
        final int oldTool = this.get(DATA_TOOL);
        final int offset = 1 + this.player.level().getRandom().nextInt(2);

        this.set(DATA_TOOL, (oldTool + offset) % 3);
        this.set(DATA_ROUND_INDEX, this.get(DATA_ROUND_INDEX) + 1);

        final int decrease = this.get(DATA_ROUND_INDEX) * NomenDubiumConfig.PALEONTOLOGY_TABLE_ROUND_DURATION_DECREASE;
        final int duration = Math.max(NomenDubiumConfig.PALEONTOLOGY_TABLE_MIN_ROUND_DURATION, NomenDubiumConfig.PALEONTOLOGY_TABLE_INITIAL_ROUND_DURATION - decrease);
        this.beginRound(duration);
    }

    // Gives early rounds some extra time, then reduces that help as the global timer runs out
    private void beginRound(int duration) {
        final int globalDuration = globalDuration();
        final int baseDuration = this.getTool() == TOOL_CHISEL ? duration + NomenDubiumConfig.PALEONTOLOGY_TABLE_CHISEL_ROUND_BONUS : duration;
        final int remainingDuration = Math.max(0, Math.min(globalDuration, this.get(DATA_GLOBAL_REMAINING)));
        final int dynamicDuration = baseDuration + Math.round(baseDuration * remainingDuration / (float) globalDuration);
        final int adjustedDuration = Math.max(1, Math.round(dynamicDuration * NomenDubiumConfig.PALEONTOLOGY_TABLE_ROUND_DURATION_MULTIPLIER));

        this.set(DATA_ROUND_DURATION, adjustedDuration);
        this.set(DATA_ROUND_REMAINING, adjustedDuration);

        this.actionsThisRound = 0;
        this.lastActionTick = Long.MIN_VALUE;
    }

    // Clears the current attempt so the table is ready for another encased fossil
    private void resetGame() {
        Arrays.fill(this.gameData, 0);

        this.set(DATA_HELD_TOOL, -1);
        this.set(DATA_FOSSIL_CATEGORY, -1);

        this.actionsThisRound = 0;
        this.lastActionTick = Long.MIN_VALUE;
    }

    private static int globalDuration() {
        return NomenDubiumConfig.PALEONTOLOGY_TABLE_GAME_DURATION * 20;
    }

    private static int maxActions(int tool) {
        return switch (tool) {
            case TOOL_CHISEL -> NomenDubiumConfig.PALEONTOLOGY_TABLE_CHISEL_MAX_ACTIONS;
            case TOOL_HAMMER -> NomenDubiumConfig.PALEONTOLOGY_TABLE_HAMMER_MAX_ACTIONS;
            case TOOL_BRUSH -> NomenDubiumConfig.PALEONTOLOGY_TABLE_BRUSH_MAX_ACTIONS;
            default -> 0;
        };
    }

    private static int progressPerAction(int tool) {
        return switch (tool) {
            case TOOL_CHISEL -> NomenDubiumConfig.PALEONTOLOGY_TABLE_CHISEL_PROGRESS;
            case TOOL_HAMMER -> NomenDubiumConfig.PALEONTOLOGY_TABLE_HAMMER_PROGRESS;
            case TOOL_BRUSH -> NomenDubiumConfig.PALEONTOLOGY_TABLE_BRUSH_PROGRESS;
            default -> 0;
        };

    }

    private void finishGame() {
        final FossilCategory category = this.getFossilCategory();
        this.container.setItem(TABLE_SLOT, category.randomResult(this.player.level().getRandom()));
        this.container.setChanged();

        this.set(DATA_PROGRESS, MAX_PROGRESS);
        this.set(DATA_STATE, STATE_WON);
        this.set(DATA_HELD_TOOL, -1);
    }

    private void failGame() {
        this.container.setItem(TABLE_SLOT, ItemStack.EMPTY);
        this.container.setChanged();

        this.set(DATA_STATE, STATE_LOST);
        this.set(DATA_HELD_TOOL, -1);
    }

}
