package dev.xylonity.nomendubium.registry;

import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.common.recipe.TreeOfLifeRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Supplier;

public final class NomenDubiumRecipes {

    public static void init() {
        ;;
    }

    public static final Supplier<RecipeType<TreeOfLifeRecipe>> TREE_OF_LIFE_TYPE = NomenDubium.PLATFORM.registerRecipeType("tree_of_life");

    public static final Supplier<RecipeSerializer<TreeOfLifeRecipe>> TREE_OF_LIFE_SERIALIZER = NomenDubium.PLATFORM.registerRecipeSerializer("tree_of_life",
            () -> new RecipeSerializer<>(TreeOfLifeRecipe.MAP_CODEC, TreeOfLifeRecipe.STREAM_CODEC)
    );

}
