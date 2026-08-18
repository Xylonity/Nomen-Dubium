package dev.xylonity.nomendubium.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.List;

/**
 * Based off my own registrar abstraction
 * https://github.com/bonsaistudi0s/Ghosts/blob/main/common/src/main/java/dev/xylonity/bonsai/ghosts/platform/GhostsPlatform.java
 *
 * (Adapted to the component system of 26.1)
 */
public interface NomenDubiumPlatform {

    <T extends Block> Supplier<T> registerBlock(String name, Function<ResourceKey<Block>, T> factory);
    <T extends Item> Supplier<T> registerItem(String name, Function<ResourceKey<Item>, T> factory);
    <T> Supplier<DataComponentType<T>> registerDataComponent(String name, Supplier<DataComponentType<T>> factory);

    <T extends Entity> Supplier<EntityType<T>> registerEntity(String name, Function<ResourceKey<EntityType<?>>, EntityType<T>> factory);

    <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(String name, BlockEntityFactory<T> factory, Supplier<? extends Block> validBlock);

    <T extends AbstractContainerMenu> Supplier<MenuType<T>> registerMenu(String name, MenuFactory<T> factory);

    Supplier<CreativeModeTab> registerCreativeTab(String name, Component title, Supplier<ItemStack> icon, List<Supplier<? extends ItemLike>> entries);

    @FunctionalInterface
    interface BlockEntityFactory<T extends BlockEntity> {
        T create(BlockPos pos, BlockState state);
    }

    @FunctionalInterface
    interface MenuFactory<T extends AbstractContainerMenu> {
        T create(int containerId, Inventory inventory);
    }

}
