package dev.xylonity.nomendubium.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class DescribedItem extends Item {

    public DescribedItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, tooltip, flag);
        appendDescription(stack, tooltip);
    }

    protected void appendDescription(ItemStack stack, Consumer<Component> tooltip) {
        final int lines = descriptionLineCount();
        for (int line = 1; line <= lines; line++) {
            final String suffix = lines == 1 ? ".tooltip" : ".tooltip." + line;
            tooltip.accept(Component.translatable(getDescriptionId() + suffix).withStyle(ChatFormatting.DARK_GRAY));
        }

    }

    protected int descriptionLineCount() {
        return 1;
    }

}
