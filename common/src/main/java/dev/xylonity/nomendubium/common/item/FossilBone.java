package dev.xylonity.nomendubium.common.item;

import dev.xylonity.nomendubium.registry.NomenDubiumItems;
import net.minecraft.world.item.Item;

public class FossilBone extends Item implements Restorable {

    public FossilBone(Properties properties) {
        super(properties);
    }

    @Override
    public Item getRestorableItem() {
        return NomenDubiumItems.REGENERATING_CHOP.get();
    }

}
