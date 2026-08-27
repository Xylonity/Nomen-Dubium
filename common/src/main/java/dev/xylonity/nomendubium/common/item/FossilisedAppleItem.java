package dev.xylonity.nomendubium.common.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Random;

public final class FossilisedAppleItem extends Item implements Restorable {

    public FossilisedAppleItem(Properties properties) {
        super(properties);
    }

    @Override
    public Item getRestorableItem() {
        return new Item[]{Items.APPLE, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE}[new Random().nextInt(3)];
    }

}
