package dev.xylonity.nomendubium.common.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public final class ShatteredDiamondItem extends Item implements Restorable {

    public ShatteredDiamondItem(Properties properties) {
        super(properties);
    }

    @Override
    public Item getRestorableItem() {
        return Items.DIAMOND;
    }

}
