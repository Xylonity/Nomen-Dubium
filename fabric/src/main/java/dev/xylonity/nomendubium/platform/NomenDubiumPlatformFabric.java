package dev.xylonity.nomendubium.platform;

import dev.xylonity.nomendubium.NomenDubium;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.List;

public class NomenDubiumPlatformFabric implements NomenDubiumPlatform {

    @Override
    public <T extends Block> Supplier<T> registerBlock(String name, Function<ResourceKey<Block>, T> factory) {
        final ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, NomenDubium.of(name));
        final T value = Registry.register(BuiltInRegistries.BLOCK, key, factory.apply(key));
        return () -> value;
    }

    @Override
    public <T extends Item> Supplier<T> registerItem(String name, Function<ResourceKey<Item>, T> factory) {
        final ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, NomenDubium.of(name));
        final T value = Registry.register(BuiltInRegistries.ITEM, key, factory.apply(key));
        return () -> value;
    }

    @Override
    public <T> Supplier<DataComponentType<T>> registerDataComponent(String name, Supplier<DataComponentType<T>> factory) {
        final DataComponentType<T> value = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            NomenDubium.of(name),
            factory.get()
        );

        return () -> value;
    }

    @Override
    public <T extends Entity> Supplier<EntityType<T>> registerEntity(String name, Function<ResourceKey<EntityType<?>>, EntityType<T>> factory) {
        final ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, NomenDubium.of(name));
        final EntityType<T> value = Registry.register(BuiltInRegistries.ENTITY_TYPE, key, factory.apply(key));
        return () -> value;
    }

    @Override
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(String name, BlockEntityFactory<T> factory, List<Supplier<? extends Block>> validBlocks) {
        final ResourceKey<BlockEntityType<?>> key = ResourceKey.create(Registries.BLOCK_ENTITY_TYPE, NomenDubium.of(name));
        final Block[] blocks = validBlocks.stream().map(Supplier::get).toArray(Block[]::new);
        final BlockEntityType<T> type = FabricBlockEntityTypeBuilder.create(factory::create, blocks).build();
        final BlockEntityType<T> value = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, key, type);
        return () -> value;
    }

    @Override
    public <T extends AbstractContainerMenu> Supplier<MenuType<T>> registerMenu(String name, MenuFactory<T> factory) {
        final ResourceKey<MenuType<?>> key = ResourceKey.create(Registries.MENU, NomenDubium.of(name));
        final MenuType<T> type = new MenuType<>(factory::create, FeatureFlags.VANILLA_SET);
        final MenuType<T> value = Registry.register(BuiltInRegistries.MENU, key, type);
        return () -> value;
    }

    @Override
    public Supplier<CreativeModeTab> registerCreativeTab(String name, Component title, Supplier<ItemStack> icon, List<Supplier<ItemStack>> entries) {
        final ResourceKey<CreativeModeTab> key = ResourceKey.create(Registries.CREATIVE_MODE_TAB, NomenDubium.of(name));
        final CreativeModeTab tab = FabricCreativeModeTab.builder()
            .title(title).icon(icon).displayItems((_, output) -> entries.forEach(entry -> output.accept(entry.get())))
            .build();
        final CreativeModeTab value = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, tab);
        return () -> value;
    }

}
