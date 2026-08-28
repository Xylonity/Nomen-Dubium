package dev.xylonity.nomendubium.common.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xylonity.nomendubium.registry.NomenDubiumItems;
import dev.xylonity.nomendubium.registry.NomenDubiumRecipes;
import java.util.List;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
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
    List<ItemStackTemplate> results
) implements Recipe<TreeOfLifeRecipeInput> {

    private static final Codec<List<ItemStackTemplate>> RESULTS_CODEC = ItemStackTemplate.CODEC.listOf().validate(results ->
        results.isEmpty() ? DataResult.error(() -> "A Tree of Life recipe must have at least one result") : DataResult.success(results)
    );

    public static final MapCodec<TreeOfLifeRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Ingredient.CODEC.fieldOf("ingredient").forGetter(TreeOfLifeRecipe::ingredient),
        Codec.intRange(1, 64).fieldOf("root_of_life_count").forGetter(TreeOfLifeRecipe::rootOfLifeCount),
        Codec.intRange(1, Integer.MAX_VALUE).fieldOf("processing_time").forGetter(TreeOfLifeRecipe::processingTime),
        RESULTS_CODEC.fieldOf("results").forGetter(TreeOfLifeRecipe::results)
    ).apply(instance, TreeOfLifeRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, TreeOfLifeRecipe> STREAM_CODEC = StreamCodec.composite(
        Ingredient.CONTENTS_STREAM_CODEC,
        TreeOfLifeRecipe::ingredient,
        ByteBufCodecs.VAR_INT,
        TreeOfLifeRecipe::rootOfLifeCount,
        ByteBufCodecs.VAR_INT,
        TreeOfLifeRecipe::processingTime,
        ItemStackTemplate.STREAM_CODEC.apply(ByteBufCodecs.list(64)),
        TreeOfLifeRecipe::results,
        TreeOfLifeRecipe::new
    );

    public TreeOfLifeRecipe {
        results = List.copyOf(results);
    }

    @Override
    public boolean matches(TreeOfLifeRecipeInput input, @NonNull Level level) {
        return this.isIngredient(input.ingredient()) && input.rootOfLife().is(NomenDubiumItems.ROOT_OF_LIFE.get()) && input.rootOfLife().getCount() >= this.rootOfLifeCount;
    }

    public boolean isIngredient(ItemStack stack) {
        return this.ingredient.test(stack);
    }

    @Override
    public @NonNull ItemStack assemble(TreeOfLifeRecipeInput input) {
        return this.results.isEmpty() ? ItemStack.EMPTY : this.results.getFirst().create();
    }

    public ItemStack random(RandomSource random) {
        return this.results.isEmpty() ? ItemStack.EMPTY : this.results.get(random.nextInt(this.results.size())).create();
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
