package dev.xylonity.nomendubium.registry;

import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.common.menu.PaleontologyTableMenu;
import dev.xylonity.nomendubium.common.menu.TreeOfLifeMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public final class NomenDubiumMenus {

    public static void init() {
        ;;
    }

    public static final Supplier<MenuType<PaleontologyTableMenu>> PALEONTOLOGY_TABLE = NomenDubium.PLATFORM.registerMenu("paleontology_table", PaleontologyTableMenu::new);
    public static final Supplier<MenuType<TreeOfLifeMenu>> TREE_OF_LIFE = NomenDubium.PLATFORM.registerMenu("tree_of_life", TreeOfLifeMenu::new);

}
