package dev.xylonity.nomendubium.common.item;

import dev.xylonity.nomendubium.registry.NomenDubiumItems;
import net.minecraft.world.item.Item;

public class AmberItem extends Item implements Restorable {

    public AmberItem(Properties properties) {
        super(properties);
    }

    @Override
    public Item getRestorableItem() {
        return NomenDubiumItems.SAP_OF_LIFE.get();
    }

}
