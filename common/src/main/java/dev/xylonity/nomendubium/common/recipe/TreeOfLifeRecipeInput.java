package dev.xylonity.nomendubium.common.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jspecify.annotations.NonNull;

public record TreeOfLifeRecipeInput(
        ItemStack ingredient,
        ItemStack rootOfLife
) implements RecipeInput {

    @Override
    public @NonNull ItemStack getItem(int index) {
        return switch (index) {
            case 0 -> this.ingredient;
            case 1 -> this.rootOfLife;
            default -> throw new IllegalArgumentException("[Nomen Dubium] No item for index " + index);
        };

    }

    @Override
    public int size() {
        return 2;
    }

}