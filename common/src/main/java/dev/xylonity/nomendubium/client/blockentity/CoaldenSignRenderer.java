package dev.xylonity.nomendubium.client.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xylonity.nomendubium.common.blockentity.CoaldenSignBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SignRenderer;

public final class CoaldenSignRenderer implements BlockEntityRenderer<CoaldenSignBlockEntity> {

    private final SignRenderer delegate;

    public CoaldenSignRenderer(BlockEntityRendererProvider.Context context) {
        this.delegate = new SignRenderer(context);
    }

    @Override
    public void render(CoaldenSignBlockEntity sign, float partialTick, PoseStack poses, MultiBufferSource buffers, int light, int overlay) {
        this.delegate.render(sign, partialTick, poses, buffers, light, overlay);
    }

}
