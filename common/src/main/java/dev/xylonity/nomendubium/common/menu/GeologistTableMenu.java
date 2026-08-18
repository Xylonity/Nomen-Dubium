package dev.xylonity.nomendubium.common.menu;

import dev.xylonity.nomendubium.common.item.fossil.util.FossilCategory;
import dev.xylonity.nomendubium.registry.NomenDubiumDataComponents;
import dev.xylonity.nomendubium.registry.NomenDubiumItems;
import dev.xylonity.nomendubium.registry.NomenDubiumMenus;
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

public class GeologistTableMenu extends AbstractContainerMenu {

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
    public static final int GLOBAL_DURATION = 20 * 60;
    public static final int COUNTDOWN_DURATION = 20 * 3;
    private static final int INITIAL_ROUND_DURATION = 90;
    private static final int MIN_ROUND_DURATION = 34;
    private static final int CHISEL_ROUND_TIME_BONUS = 30;
    private static final float ROUND_DURATION_SCALE = 0.75F;

    private final int[] gameData = new int[DATA_COUNT];

    private final Container container;
    private final ContainerLevelAccess access;
    private final Player player;

    private long lastGameTick = Long.MIN_VALUE;
    private long lastActionTick = Long.MIN_VALUE;

    private int actionsThisRound;

    public GeologistTableMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(1), ContainerLevelAccess.NULL);
    }

    public GeologistTableMenu(int containerId, Inventory inventory, Container container, ContainerLevelAccess access) {
        super(NomenDubiumMenus.GEOLOGIST_TABLE.get(), containerId);
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

        this.addStandardInventorySlots(inventory, 48, 129);

        for (int i = 0; i < DATA_COUNT; i++) {
            this.addDataSlot(DataSlot.shared(this.gameData, i));
        }

    }

    @Override
    public @NonNull ItemStack quickMoveStack(@NonNull Player player, int i) {
        return null;
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
    public void broadcastChanges() {
        if (!this.player.level().isClientSide()) {
            this.tick();
        }

        super.broadcastChanges();
    }

    private void tick() {
        final long tick = this.player.level().getGameTime();
        if (tick == this.lastGameTick) {
            return;
        }

        this.lastGameTick = tick;

        // Checks if the workpiece (the only slot on the table) is the correct item (encased fossil)
        final ItemStack workpiece = this.container.getItem(TABLE_SLOT);
        final boolean hasEncasedFossil = workpiece.is(NomenDubiumItems.ENCASED_FOSSIL.get());

        if (!hasEncasedFossil) {
            // TODO: fail detection
            return;
        }

        final FossilCategory category = this.getOrAssignCategory(workpiece);
        this.set(DATA_FOSSIL_CATEGORY, category.ordinal());

        if (this.getGameState() == STATE_IDLE || this.getGameState() == STATE_LOST) {
            this.startCountdown();
            return;
        }
        if (this.getGameState() == STATE_COUNTDOWN) {
            this.set(DATA_COUNTDOWN_REMAINING, this.get(DATA_COUNTDOWN_REMAINING) - 1);
            if (this.get(DATA_COUNTDOWN_REMAINING) <= 0) {
                this.startGame();
            }
            return;
        }
        if (this.getGameState() != STATE_PLAYING) {
            return;
        }

        this.set(DATA_GLOBAL_REMAINING, this.get(DATA_GLOBAL_REMAINING) - 1);
        if (this.getHeldTool() == this.getTool()) {
            this.set(DATA_ROUND_REMAINING, this.get(DATA_ROUND_REMAINING) - 1);
        }

        if (this.get(DATA_GLOBAL_REMAINING) <= 0) {
            this.set(DATA_GLOBAL_REMAINING, 0);
            this.failGame();
        } else if (this.get(DATA_ROUND_REMAINING) <= 0) {
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
        FossilCategory category = FossilCategory.name(workpiece.get(NomenDubiumDataComponents.FOSSIL_CATEGORY.get()));
        if (category == null) {
            category = FossilCategory.random(this.player.level().getRandom());
            workpiece.set(NomenDubiumDataComponents.FOSSIL_CATEGORY.get(), category.serializedName());
            this.container.setChanged();
        }

        return category;
    }

    // Resets the previous attempt and gives the player a moment to get ready
    private void startCountdown() {
        this.set(DATA_STATE, STATE_COUNTDOWN);
        this.set(DATA_COUNTDOWN_REMAINING, COUNTDOWN_DURATION);
        this.set(DATA_GLOBAL_REMAINING, GLOBAL_DURATION);
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
        this.beginRound(INITIAL_ROUND_DURATION);
    }

    // Changes to a different tool and gives the player less time on each new round
    private void startNextRound() {
        final int oldTool = this.get(DATA_TOOL);
        final int offset = 1 + this.player.level().getRandom().nextInt(2);

        this.set(DATA_TOOL, (oldTool + offset) % 3);
        this.set(DATA_ROUND_INDEX, this.get(DATA_ROUND_INDEX) + 1);

        final int duration = Math.max(MIN_ROUND_DURATION, INITIAL_ROUND_DURATION - this.get(DATA_ROUND_INDEX) * 4);
        this.beginRound(duration);
    }

    // Gives early rounds some extra time, then reduces that help as the global timer runs out
    private void beginRound(int duration) {
        final int baseDuration = this.getTool() == TOOL_CHISEL ? duration + CHISEL_ROUND_TIME_BONUS : duration;
        final int remainingDuration = Math.max(0, Math.min(GLOBAL_DURATION, this.get(DATA_GLOBAL_REMAINING)));
        final int dynamicDuration = baseDuration + Math.round(baseDuration * remainingDuration / (float) GLOBAL_DURATION);
        final int adjustedDuration = Math.max(1, Math.round(dynamicDuration * ROUND_DURATION_SCALE));

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


}
