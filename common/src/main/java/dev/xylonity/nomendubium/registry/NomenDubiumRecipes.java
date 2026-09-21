package dev.xylonity.nomendubium.registry;

import dev.xylonity.knightlib.api.registrar.ResourceDispatcher;
import dev.xylonity.knightlib.api.registrar.ResourceEntry;
import dev.xylonity.knightlib.api.registrar.ResourceRegistry;
import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.common.recipe.FruitOfLifePaletteRecipe;
import dev.xylonity.nomendubium.common.recipe.TreeOfLifeRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public final class NomenDubiumRecipes {

    public static final ResourceRegistry<RecipeType<?>> TYPES = ResourceDispatcher.create(BuiltInRegistries.RECIPE_TYPE, NomenDubium.MOD_ID);
    public static final ResourceRegistry<RecipeSerializer<?>> SERIALIZERS = ResourceDispatcher.create(BuiltInRegistries.RECIPE_SERIALIZER, NomenDubium.MOD_ID);

    public static final ResourceEntry<RecipeType<TreeOfLifeRecipe>> TREE_OF_LIFE_TYPE = TYPES.register("tree_of_life", () -> new RecipeType<>() {
        @Override public String toString() { return NomenDubium.MOD_ID + ":tree_of_life"; }
    });
    public static final ResourceEntry<RecipeSerializer<TreeOfLifeRecipe>> TREE_OF_LIFE_SERIALIZER = SERIALIZERS.register("tree_of_life", TreeOfLifeRecipe.Serializer::new);
    public static final ResourceEntry<RecipeSerializer<FruitOfLifePaletteRecipe>> FRUIT_OF_LIFE_PALETTE_SERIALIZER = SERIALIZERS.register("fruit_of_life_palette", FruitOfLifePaletteRecipe.Serializer::new);

}