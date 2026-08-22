package dev.xylonity.nomendubium.common.item;

import net.minecraft.world.item.Item;

public interface Restorable {

    Item getRestorableItem();

    default int getRestorableItemCount() {
        return 1;
    }

}