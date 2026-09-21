package dev.xylonity.nomendubium.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.xylonity.nomendubium.registry.NomenDubiumItems;
import dev.xylonity.nomendubium.registry.NomenDubiumRecipes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public record TreeOfLifeRecipe(
        ResourceLocation id,
        Ingredient ingredient,
        int rootOfLifeCount,
        int processingTime,
        List<ItemStack> results
) implements Recipe<TreeOfLifeRecipeInput> {

    public TreeOfLifeRecipe {
        results = List.copyOf(results);
    }

    @Override
    public boolean matches(TreeOfLifeRecipeInput input, Level level) {
        return isIngredient(input.ingredient()) && input.rootOfLife().is(NomenDubiumItems.ROOT_OF_LIFE.get()) && input.rootOfLife().getCount() >= this.rootOfLifeCount;
    }

    public boolean isIngredient(ItemStack stack) { return this.ingredient.test(stack); }

    @Override
    public ItemStack assemble(TreeOfLifeRecipeInput input, RegistryAccess registries) {
        return this.results.isEmpty() ? ItemStack.EMPTY : this.results.get(0).copy();
    }

    public ItemStack random(RandomSource random) {
        return this.results.isEmpty() ? ItemStack.EMPTY : this.results.get(random.nextInt(this.results.size())).copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registries) {
        return this.results.isEmpty() ? ItemStack.EMPTY : this.results.get(0).copy();
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
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return NomenDubiumRecipes.TREE_OF_LIFE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return NomenDubiumRecipes.TREE_OF_LIFE_TYPE.get();
    }

    public static final class Serializer implements RecipeSerializer<TreeOfLifeRecipe> {

        @Override
        public TreeOfLifeRecipe fromJson(ResourceLocation id, JsonObject json) {
            final JsonElement ingredientJson = json.get("ingredient");
            final Ingredient ingredient;
            if (ingredientJson.isJsonPrimitive()) {
                final String value = ingredientJson.getAsString();
                ingredient = value.startsWith("#") ? Ingredient.of(TagKey.create(Registries.ITEM, new ResourceLocation(value.substring(1)))) : Ingredient.of(requireItem(new ResourceLocation(value)));
            }
            else {
                ingredient = Ingredient.fromJson(ingredientJson);
            }

            final int roots = GsonHelper.getAsInt(json, "root_of_life_count", 1);
            final int time = GsonHelper.getAsInt(json, "processing_time", 200);
            final List<ItemStack> results = new ArrayList<>();

            final JsonArray resultArray = GsonHelper.getAsJsonArray(json, "results");
            for (JsonElement element : resultArray) {
                final JsonObject result = element.getAsJsonObject();
                results.add(new ItemStack(requireItem(new ResourceLocation(GsonHelper.getAsString(result, "id"))), GsonHelper.getAsInt(result, "count", 1)));
            }
            if (results.isEmpty()) {
                throw new IllegalArgumentException("A Tree of Life recipe must have a result");
            }

            return new TreeOfLifeRecipe(id, ingredient, roots, time, results);
        }

        @Override
        public TreeOfLifeRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            final Ingredient ingredient = Ingredient.fromNetwork(buffer);
            final int roots = buffer.readVarInt();
            final int time = buffer.readVarInt();
            final int size = buffer.readVarInt();
            final List<ItemStack> results = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                results.add(buffer.readItem());
            }

            return new TreeOfLifeRecipe(id, ingredient, roots, time, results);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, TreeOfLifeRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeVarInt(recipe.rootOfLifeCount);
            buffer.writeVarInt(recipe.processingTime);
            buffer.writeVarInt(recipe.results.size());
            recipe.results.forEach(buffer::writeItem);
        }

        private static Item requireItem(ResourceLocation id) {
            final Item item = BuiltInRegistries.ITEM.get(id);
            if (item == null || !BuiltInRegistries.ITEM.containsKey(id)) {
                throw new IllegalArgumentException("Unknown item " + id);
            }

            return item;
        }

    }

}