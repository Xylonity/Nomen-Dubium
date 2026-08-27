package dev.xylonity.nomendubium.common.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xylonity.nomendubium.registry.NomenDubiumItems;
import dev.xylonity.nomendubium.registry.NomenDubiumRecipes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public record TreeOfLifeRecipe(
    Ingredient ingredient,
    int rootOfLifeCount,
    int processingTime,
    ItemStackTemplate result
) implements Recipe<TreeOfLifeRecipeInput> {

    public static final MapCodec<TreeOfLifeRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Ingredient.CODEC.fieldOf("ingredient").forGetter(TreeOfLifeRecipe::ingredient),
        Codec.intRange(1, 64).fieldOf("root_of_life_count").forGetter(TreeOfLifeRecipe::rootOfLifeCount),
        Codec.intRange(1, Integer.MAX_VALUE).fieldOf("processing_time").forGetter(TreeOfLifeRecipe::processingTime),
        ItemStackTemplate.CODEC.fieldOf("result").forGetter(TreeOfLifeRecipe::result)
    ).apply(instance, TreeOfLifeRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, TreeOfLifeRecipe> STREAM_CODEC = StreamCodec.composite(
        Ingredient.CONTENTS_STREAM_CODEC,
        TreeOfLifeRecipe::ingredient,
        ByteBufCodecs.VAR_INT,
        TreeOfLifeRecipe::rootOfLifeCount,
        ByteBufCodecs.VAR_INT,
        TreeOfLifeRecipe::processingTime,
        ItemStackTemplate.STREAM_CODEC,
        TreeOfLifeRecipe::result,
        TreeOfLifeRecipe::new
    );

    @Override
    public boolean matches(TreeOfLifeRecipeInput input, @NonNull Level level) {
        return this.isIngredient(input.ingredient()) && input.rootOfLife().is(NomenDubiumItems.ROOT_OF_LIFE.get()) && input.rootOfLife().getCount() >= this.rootOfLifeCount;
    }

    public boolean isIngredient(ItemStack stack) {
        return this.ingredient.test(stack);
    }

    @Override
    public @NonNull ItemStack assemble(TreeOfLifeRecipeInput input) {
        return this.result.create();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public @NonNull String group() {
        return "";
    }

    @Override
    public @NonNull RecipeSerializer<TreeOfLifeRecipe> getSerializer() {
        return NomenDubiumRecipes.TREE_OF_LIFE_SERIALIZER.get();
    }

    @Override
    public @NonNull RecipeType<TreeOfLifeRecipe> getType() {
        return NomenDubiumRecipes.TREE_OF_LIFE_TYPE.get();
    }

    @Override
    public @NonNull PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public @NonNull RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

}