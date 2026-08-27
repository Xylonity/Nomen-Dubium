package dev.xylonity.nomendubium.client.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xylonity.nomendubium.common.blockentity.CoaldenSignBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.StandingSignRenderer;
import net.minecraft.client.renderer.blockentity.state.StandingSignRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;

public final class CoaldenSignRenderer implements BlockEntityRenderer<CoaldenSignBlockEntity, StandingSignRenderState> {

    private final StandingSignRenderer delegate;

    public CoaldenSignRenderer(BlockEntityRendererProvider.Context context) {
        delegate = new StandingSignRenderer(context);
    }

    @Override
    public StandingSignRenderState createRenderState() {
        return delegate.createRenderState();
    }

    @Override
    public void extractRenderState(CoaldenSignBlockEntity blockEntity, StandingSignRenderState state, float partialTick, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        delegate.extractRenderState(blockEntity, state, partialTick, cameraPosition, crumblingOverlay);
    }

    @Override
    public void submit(StandingSignRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        delegate.submit(state, poseStack, submitNodeCollector, cameraRenderState);
    }

}
