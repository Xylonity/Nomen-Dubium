package dev.xylonity.nomendubium.platform;

import dev.xylonity.nomendubium.NomenDubium;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.List;

/**
 * Neo doesn't like inline variables for some reason
 */
public class NomenDubiumPlatformNeoForge implements NomenDubiumPlatform {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, NomenDubium.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, NomenDubium.MOD_ID);
    private static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, NomenDubium.MOD_ID);
    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, NomenDubium.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, NomenDubium.MOD_ID);
    private static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, NomenDubium.MOD_ID);
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, NomenDubium.MOD_ID);
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, NomenDubium.MOD_ID);
    private static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, NomenDubium.MOD_ID);

    private static boolean registered;

    public static void register(IEventBus eventBus) {
        if (registered) {
            return;
        }

        BLOCKS.register(eventBus);
        DATA_COMPONENT_TYPES.register(eventBus);
        ITEMS.register(eventBus);
        ENTITY_TYPES.register(eventBus);
        BLOCK_ENTITY_TYPES.register(eventBus);
        MENU_TYPES.register(eventBus);
        RECIPE_TYPES.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
        CREATIVE_TABS.register(eventBus);

        registered = true;
    }

    @Override
    public <T extends Block> Supplier<T> registerBlock(String name, Function<ResourceKey<Block>, T> factory) {
        return BLOCKS.register(name, id -> factory.apply(ResourceKey.create(Registries.BLOCK, id)));
    }

    @Override
    public <T extends Item> Supplier<T> registerItem(String name, Function<ResourceKey<Item>, T> factory) {
        return ITEMS.register(name, id -> factory.apply(ResourceKey.create(Registries.ITEM, id)));
    }

    @Override
    public <T> Supplier<DataComponentType<T>> registerDataComponent(String name, Supplier<DataComponentType<T>> factory) {
        final DeferredHolder<DataComponentType<?>, DataComponentType<T>> holder = DATA_COMPONENT_TYPES.register(name, factory);
        return holder;
    }

    @Override
    public <T extends Entity> Supplier<EntityType<T>> registerEntity(String name, Function<ResourceKey<EntityType<?>>, EntityType<T>> factory) {
        final DeferredHolder<EntityType<?>, EntityType<T>> holder = ENTITY_TYPES.register(name, id -> factory.apply(ResourceKey.create(Registries.ENTITY_TYPE, id)));
        return holder;
    }

    @Override
    public <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(String name, BlockEntityFactory<T> factory, List<Supplier<? extends Block>> validBlocks) {
        final DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> holder = BLOCK_ENTITY_TYPES.register(name, () -> new BlockEntityType<>(
            factory::create,
            validBlocks.stream().map(Supplier::get).collect(java.util.stream.Collectors.toUnmodifiableSet())
        ));
        return holder;
    }

    @Override
    public <T extends AbstractContainerMenu> Supplier<MenuType<T>> registerMenu(String name, MenuFactory<T> factory) {
        final DeferredHolder<MenuType<?>, MenuType<T>> holder = MENU_TYPES.register(name, () -> new MenuType<>(factory::create, FeatureFlags.VANILLA_SET));
        return holder;
    }

    @Override
    public <T extends Recipe<?>> Supplier<RecipeType<T>> registerRecipeType(String name) {
        final DeferredHolder<RecipeType<?>, RecipeType<T>> holder = RECIPE_TYPES.register(name, () -> new RecipeType<>() {
            @Override
            public String toString() {
                return NomenDubium.of(name).toString();
            }
            
        });
        return holder;
    }

    @Override
    public <T extends Recipe<?>> Supplier<RecipeSerializer<T>> registerRecipeSerializer(String name, Supplier<RecipeSerializer<T>> factory) {
        final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<T>> holder = RECIPE_SERIALIZERS.register(name, factory);
        return holder;
    }

    @Override
    public Supplier<CreativeModeTab> registerCreativeTab(String name, Component title, Supplier<ItemStack> icon, List<Supplier<ItemStack>> entries) {
        return CREATIVE_TABS.register(name,
            () -> CreativeModeTab.builder().title(title).icon(icon).displayItems((_, output) -> entries.forEach(entry -> output.accept(entry.get())))
                .build()
        );

    }

}
