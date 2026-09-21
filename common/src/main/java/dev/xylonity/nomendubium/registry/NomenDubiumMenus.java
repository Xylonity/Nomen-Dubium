package dev.xylonity.nomendubium.registry;

import dev.xylonity.knightlib.api.registrar.ResourceDispatcher;
import dev.xylonity.knightlib.api.registrar.ResourceEntry;
import dev.xylonity.knightlib.api.registrar.ResourceRegistry;
import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.common.menu.PaleontologyTableMenu;
import dev.xylonity.nomendubium.common.menu.TreeOfLifeMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;

public final class NomenDubiumMenus {

    public static final ResourceRegistry<MenuType<?>> MENUS = ResourceDispatcher.create(BuiltInRegistries.MENU, NomenDubium.MOD_ID);

    public static final ResourceEntry<MenuType<PaleontologyTableMenu>> PALEONTOLOGY_TABLE = MENUS.registerMenu("paleontology_table", (syncId, inventory, buffer) -> new PaleontologyTableMenu(syncId, inventory));
    public static final ResourceEntry<MenuType<TreeOfLifeMenu>> TREE_OF_LIFE = MENUS.registerMenu("tree_of_life", (syncId, inventory, buffer) -> new TreeOfLifeMenu(syncId, inventory));

}