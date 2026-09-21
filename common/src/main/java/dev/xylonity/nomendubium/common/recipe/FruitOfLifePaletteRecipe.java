package dev.xylonity.nomendubium.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraPaletteVariant;
import dev.xylonity.nomendubium.registry.NomenDubiumItems;
import dev.xylonity.nomendubium.registry.NomenDubiumRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public record FruitOfLifePaletteRecipe(
        ResourceLocation id,
        NonNullList<Ingredient> ingredients,
        ChimeraPaletteVariant palette
) implements CraftingRecipe {

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        final List<ItemStack> stacks = new ArrayList<>();
        for (int slot = 0; slot < container.getContainerSize(); slot++) {
            final ItemStack stack = container.getItem(slot);
            if (!stack.isEmpty()) {
                stacks.add(stack);
            }

        }

        return stacks.size() == this.ingredients.size() && matches(stacks, new boolean[stacks.size()], 0);
    }

    private boolean matches(List<ItemStack> stacks, boolean[] used, int ingredientIndex) {
        if (ingredientIndex == this.ingredients.size()) {
            return true;
        }

        final Ingredient ingredient = this.ingredients.get(ingredientIndex);
        for (int stackIndex = 0; stackIndex < stacks.size(); stackIndex++) {
            if (!used[stackIndex] && ingredient.test(stacks.get(stackIndex))) {
                used[stackIndex] = true;
                if (matches(stacks, used, ingredientIndex + 1)) {
                    return true;
                }

                used[stackIndex] = false;
            }

        }

        return false;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registries) {
        return NomenDubiumItems.FRUIT_OF_LIFE.get().createStack(this.palette);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= this.ingredients.size();
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registries) {
        return NomenDubiumItems.FRUIT_OF_LIFE.get().createStack(this.palette);
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
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
    public CraftingBookCategory category() {
        return CraftingBookCategory.MISC;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return NomenDubiumRecipes.FRUIT_OF_LIFE_PALETTE_SERIALIZER.get();
    }

    public static final class Serializer implements RecipeSerializer<FruitOfLifePaletteRecipe> {

        @Override
        public FruitOfLifePaletteRecipe fromJson(ResourceLocation id, JsonObject json) {
            final JsonArray array = GsonHelper.getAsJsonArray(json, "ingredients");
            final NonNullList<Ingredient> ingredients = NonNullList.create();
            for (JsonElement element : array) {
                final Ingredient ingredient = Ingredient.fromJson(element);
                if (!ingredient.isEmpty()) {
                    ingredients.add(ingredient);
                }

            }

            if (ingredients.isEmpty()) {
                throw new IllegalArgumentException("A Fruit of Life palette recipe must have at least one ingredient");
            }

            final String paletteName = GsonHelper.getAsString(json, "palette");
            final ChimeraPaletteVariant palette = ChimeraPaletteVariant.byName(paletteName);
            if (palette == null) {
                throw new IllegalArgumentException("Unknown chimera palette " + paletteName);
            }

            return new FruitOfLifePaletteRecipe(id, ingredients, palette);
        }

        @Override
        public FruitOfLifePaletteRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            final int size = buffer.readVarInt();
            final NonNullList<Ingredient> ingredients = NonNullList.withSize(size, Ingredient.EMPTY);
            ingredients.replaceAll(ignored -> Ingredient.fromNetwork(buffer));
            return new FruitOfLifePaletteRecipe(id, ingredients, ChimeraPaletteVariant.index(buffer.readVarInt()));
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, FruitOfLifePaletteRecipe recipe) {
            buffer.writeVarInt(recipe.ingredients.size());
            recipe.ingredients.forEach(ingredient -> ingredient.toNetwork(buffer));
            buffer.writeVarInt(recipe.palette.index());
        }

    }

}