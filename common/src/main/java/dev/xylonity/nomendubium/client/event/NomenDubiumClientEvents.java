package dev.xylonity.nomendubium.client.event;

import dev.xylonity.knightlib.api.event.RegisterEvent;
import dev.xylonity.knightlib.api.event.impl.client.AdditionalModelsRegistrationEvent;
import dev.xylonity.knightlib.api.event.impl.client.BlockEntityRendererRegistrationEvent;
import dev.xylonity.knightlib.api.event.impl.client.ClientRenderGuiEvent;
import dev.xylonity.knightlib.api.event.impl.client.ClientTickEvent;
import dev.xylonity.knightlib.api.event.impl.client.EntityRendererRegistrationEvent;
import dev.xylonity.knightlib.api.event.impl.client.MenuScreenRegistrationEvent;
import dev.xylonity.knightlib.api.event.impl.client.RenderLayerRegistrationEvent;
import dev.xylonity.knightlib.api.event.impl.interop.TickPhase;
import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.client.AmberVisionClient;
import dev.xylonity.nomendubium.client.blockentity.CoaldenSignRenderer;
import dev.xylonity.nomendubium.client.entity.render.TreeOfLifeRenderer;
import dev.xylonity.nomendubium.client.entity.render.chimera.ChimeraRenderer;
import dev.xylonity.nomendubium.client.projectile.renderer.*;
import dev.xylonity.nomendubium.client.screen.PaleontologyTableScreen;
import dev.xylonity.nomendubium.client.screen.TreeOfLifeScreen;
import dev.xylonity.nomendubium.registry.NomenDubiumBlockEntities;
import dev.xylonity.nomendubium.registry.NomenDubiumBlocks;
import dev.xylonity.nomendubium.registry.NomenDubiumEntities;
import dev.xylonity.nomendubium.registry.NomenDubiumMenus;
import net.minecraft.client.resources.model.ModelResourceLocation;

public final class NomenDubiumClientEvents {

    @RegisterEvent
    public static void registerEntityRenderers(EntityRendererRegistrationEvent event) {
        event.register(NomenDubiumEntities.CHIMERA, ChimeraRenderer::new);
        event.register(NomenDubiumEntities.SKELETON_PART, SkeletonPartRenderer::new);
        event.register(NomenDubiumEntities.HUNTERS_ARROW, HuntersArrowRenderer::new);
        event.register(NomenDubiumEntities.PRIMITIVE_ARROW, PrimitiveArrowRenderer::new);
        event.register(NomenDubiumEntities.PREHISTORIC_MAW, PrehistoricMawRenderer::new);
        event.register(NomenDubiumEntities.FOSSILISED_MAW, FossilisedMawRenderer::new);
        event.register(NomenDubiumEntities.TREE_OF_LIFE, TreeOfLifeRenderer::new);
    }

    @RegisterEvent
    public static void registerBlockEntityRenderers(BlockEntityRendererRegistrationEvent event) {
        event.register(NomenDubiumBlockEntities.COALDEN_SIGN, CoaldenSignRenderer::new);
    }

    @RegisterEvent
    public static void registerMenuScreens(MenuScreenRegistrationEvent event) {
        event.register(NomenDubiumMenus.PALEONTOLOGY_TABLE, PaleontologyTableScreen::new);
        event.register(NomenDubiumMenus.TREE_OF_LIFE, TreeOfLifeScreen::new);
    }

    @RegisterEvent
    public static void registerAdditionalModels(AdditionalModelsRegistrationEvent event) {
        registerItemModel(event, "prehistoric_maw_held");
        registerItemModel(event, "prehistoric_maw_item");
        registerItemModel(event, "fossilised_maw_held");
        registerItemModel(event, "fossilised_maw_item");
    }

    private static void registerItemModel(AdditionalModelsRegistrationEvent event, String name) {
        event.register(new ModelResourceLocation(NomenDubium.of(name), "inventory"));
    }

    @RegisterEvent
    public static void registerRenderLayers(RenderLayerRegistrationEvent event) {
        event.setCutout(NomenDubiumBlocks.ROOT_OF_LIFE.get());
    }

    @RegisterEvent
    public static void clientTick(ClientTickEvent event) {
        if (event.getPhase() == TickPhase.END) {
            AmberVisionClient.tick();
        }

    }

    @RegisterEvent
    public static void renderGui(ClientRenderGuiEvent event) {
        if (event.client().screen == null) {
            AmberVisionClient.renderOverlay(event.guiGraphics());
        }

    }

}
