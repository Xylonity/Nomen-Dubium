package dev.xylonity.nomendubium.common.recipe;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

public final class TreeOfLifeRecipeInput extends SimpleContainer {

    public TreeOfLifeRecipeInput(ItemStack ingredient, ItemStack rootOfLife) {
        super(ingredient, rootOfLife);
    }

    public ItemStack ingredient() {
        return this.getItem(0);
    }

    public ItemStack rootOfLife() {
        return this.getItem(1);
    }

}