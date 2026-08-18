package dev.xylonity.nomendubium.common.menu;

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

    //@Override
    //public void broadcastChanges() {
    //    if (!this.player.level().isClientSide()) {
    //        this.tick();
    //    }

    //    super.broadcastChanges();
    //}

}
