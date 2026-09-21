package dev.xylonity.nomendubium.common.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraPaletteVariant;
import dev.xylonity.nomendubium.registry.NomenDubiumItems;
import dev.xylonity.nomendubium.registry.NomenDubiumRecipes;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public record FruitOfLifePaletteRecipe(
        NonNullList<Ingredient> ingredients,
        ChimeraPaletteVariant palette
) implements CraftingRecipe {

    @Override
    public boolean matches(CraftingInput input, Level level) {
        final List<ItemStack> stacks = new ArrayList<>();
        for (int slot = 0; slot < input.size(); slot++) {
            final ItemStack stack = input.getItem(slot);
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
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        return NomenDubiumItems.FRUIT_OF_LIFE.get().createStack(this.palette);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= this.ingredients.size();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
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
    public RecipeSerializer<?> getSerializer() {
        return NomenDubiumRecipes.FRUIT_OF_LIFE_PALETTE_SERIALIZER.get();
    }

    public static final class Serializer implements RecipeSerializer<FruitOfLifePaletteRecipe> {

        private static final Codec<NonNullList<Ingredient>> INGREDIENTS_CODEC = Ingredient.CODEC_NONEMPTY.listOf()
            .flatXmap(
                ingredients -> ingredients.isEmpty()
                    ? DataResult.error(() -> "A Fruit of Life palette recipe must have at least one ingredient")
                    : DataResult.success(NonNullList.of(Ingredient.EMPTY, ingredients.toArray(Ingredient[]::new))),
                ingredients -> DataResult.success(List.copyOf(ingredients))

            );

        private static final Codec<ChimeraPaletteVariant> PALETTE_CODEC = Codec.STRING.flatXmap(
            name -> {
                final ChimeraPaletteVariant palette = ChimeraPaletteVariant.byName(name);
                return palette == null
                    ? DataResult.error(() -> "Unknown chimera palette " + name)
                    : DataResult.success(palette);
            },
            palette -> DataResult.success(palette.parsedName())

        );

        private static final MapCodec<FruitOfLifePaletteRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            INGREDIENTS_CODEC.fieldOf("ingredients").forGetter(FruitOfLifePaletteRecipe::ingredients),
            PALETTE_CODEC.fieldOf("palette").forGetter(FruitOfLifePaletteRecipe::palette)
        ).apply(instance, FruitOfLifePaletteRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, FruitOfLifePaletteRecipe> STREAM_CODEC = StreamCodec.of(
            Serializer::toNetwork,
            Serializer::fromNetwork
        );

        @Override
        public MapCodec<FruitOfLifePaletteRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FruitOfLifePaletteRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static FruitOfLifePaletteRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            final int size = buffer.readVarInt();
            final NonNullList<Ingredient> ingredients = NonNullList.withSize(size, Ingredient.EMPTY);
            ingredients.replaceAll(ignored -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
            return new FruitOfLifePaletteRecipe(ingredients, ChimeraPaletteVariant.index(buffer.readVarInt()));
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, FruitOfLifePaletteRecipe recipe) {
            buffer.writeVarInt(recipe.ingredients.size());
            recipe.ingredients.forEach(ingredient -> Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient));
            buffer.writeVarInt(recipe.palette.index());
        }

    }

}