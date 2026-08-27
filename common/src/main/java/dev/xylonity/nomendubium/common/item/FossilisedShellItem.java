package dev.xylonity.nomendubium.common.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public final class FossilisedShellItem extends Item implements Restorable {

    public FossilisedShellItem(Properties properties) {
        super(properties);
    }

    @Override
    public Item getRestorableItem() {
        return Items.NAUTILUS_SHELL;
    }

}
