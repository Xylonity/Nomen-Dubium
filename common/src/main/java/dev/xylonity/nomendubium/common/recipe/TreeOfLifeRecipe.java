package dev.xylonity.nomendubium.common.recipe;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xylonity.nomendubium.registry.NomenDubiumItems;
import dev.xylonity.nomendubium.registry.NomenDubiumRecipes;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record TreeOfLifeRecipe(
        Ingredient ingredient,
        int rootOfLifeCount,
        int processingTime,
        List<ItemStack> results
) implements Recipe<TreeOfLifeRecipeInput> {

    public TreeOfLifeRecipe {
        results = List.copyOf(results);
        if (results.isEmpty()) {
            throw new IllegalArgumentException("A Tree of Life recipe must have a result");
        }

    }

    @Override
    public boolean matches(TreeOfLifeRecipeInput input, Level level) {
        return isIngredient(input.ingredient()) && input.rootOfLife().is(NomenDubiumItems.ROOT_OF_LIFE.get()) && input.rootOfLife().getCount() >= this.rootOfLifeCount;
    }

    public boolean isIngredient(ItemStack stack) {
        return this.ingredient.test(stack);
    }

    @Override
    public ItemStack assemble(TreeOfLifeRecipeInput input, HolderLookup.Provider registries) {
        return this.results.getFirst().copy();
    }

    public ItemStack random(RandomSource random) {
        return this.results.get(random.nextInt(this.results.size())).copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return this.results.getFirst().copy();
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
    public RecipeSerializer<?> getSerializer() {
        return NomenDubiumRecipes.TREE_OF_LIFE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return NomenDubiumRecipes.TREE_OF_LIFE_TYPE.get();
    }

    public static final class Serializer implements RecipeSerializer<TreeOfLifeRecipe> {

        private static final Codec<Ingredient> INGREDIENT_CODEC = Codec.either(Codec.STRING, Ingredient.CODEC_NONEMPTY)
            .xmap(either -> either.map(Serializer::ingredientFromString, ingredient -> ingredient), Either::right);

        private static final MapCodec<TreeOfLifeRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            INGREDIENT_CODEC.fieldOf("ingredient").forGetter(TreeOfLifeRecipe::ingredient),
            Codec.INT.optionalFieldOf("root_of_life_count", 1).forGetter(TreeOfLifeRecipe::rootOfLifeCount),
            Codec.INT.optionalFieldOf("processing_time", 200).forGetter(TreeOfLifeRecipe::processingTime),
            ItemStack.STRICT_CODEC.listOf().fieldOf("results").forGetter(TreeOfLifeRecipe::results)
        ).apply(instance, TreeOfLifeRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, TreeOfLifeRecipe> STREAM_CODEC = StreamCodec.of(
            Serializer::toNetwork,
            Serializer::fromNetwork
        );

        @Override
        public MapCodec<TreeOfLifeRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, TreeOfLifeRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static TreeOfLifeRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            final Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            final int roots = buffer.readVarInt();
            final int time = buffer.readVarInt();
            final int size = buffer.readVarInt();
            final List<ItemStack> results = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                results.add(ItemStack.STREAM_CODEC.decode(buffer));
            }

            return new TreeOfLifeRecipe(ingredient, roots, time, results);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, TreeOfLifeRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient);
            buffer.writeVarInt(recipe.rootOfLifeCount);
            buffer.writeVarInt(recipe.processingTime);
            buffer.writeVarInt(recipe.results.size());
            recipe.results.forEach(stack -> ItemStack.STREAM_CODEC.encode(buffer, stack));
        }

        private static Ingredient ingredientFromString(String value) {
            if (value.startsWith("#")) {
                return Ingredient.of(TagKey.create(Registries.ITEM, ResourceLocation.parse(value.substring(1))));
            }

            return Ingredient.of(requireItem(ResourceLocation.parse(value)));
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