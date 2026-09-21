package dev.xylonity.nomendubium.client.item;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xylonity.nomendubium.NomenDubium;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public final class MawItemRenderer extends BlockEntityWithoutLevelRenderer {

    private final ItemRenderer itemRenderer;
    private final ModelResourceLocation heldModel;
    private final ModelResourceLocation iconModel;

    public MawItemRenderer(String name) {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
        this.heldModel = model(name + "_held");
        this.iconModel = model(name + "_item");
    }

    @Override
    public void renderByItem(@NonNull ItemStack stack, @NonNull ItemDisplayContext displayContext, @NonNull PoseStack poseStack, @NonNull MultiBufferSource buffers, int packedLight, int packedOverlay) {
        final BakedModel model = Minecraft.getInstance().getModelManager().getModel(
            usesSmallIcon(displayContext) ? this.iconModel : this.heldModel
        );

        poseStack.pushPose();

        poseStack.translate(0.5F, 0.5F, 0.5F);

        this.itemRenderer.render(stack, displayContext, isLeftHand(displayContext), poseStack, buffers, packedLight, packedOverlay, model);

        poseStack.popPose();
    }

    private static ModelResourceLocation model(String name) {
        return new ModelResourceLocation(NomenDubium.of("item/" + name), "standalone");
    }

    private static boolean usesSmallIcon(ItemDisplayContext displayContext) {
        return displayContext == ItemDisplayContext.GUI || displayContext == ItemDisplayContext.GROUND;
    }

    private static boolean isLeftHand(ItemDisplayContext displayContext) {
        return displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || displayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
    }

}
