package dev.xylonity.nomendubium.common.recipe;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public final class TreeOfLifeRecipeInput extends SimpleContainer implements RecipeInput {

    public TreeOfLifeRecipeInput(ItemStack ingredient, ItemStack rootOfLife) {
        super(ingredient, rootOfLife);
    }

    public ItemStack ingredient() {
        return this.getItem(0);
    }

    public ItemStack rootOfLife() {
        return this.getItem(1);
    }

    @Override
    public int size() {
        return this.getContainerSize();
    }

}