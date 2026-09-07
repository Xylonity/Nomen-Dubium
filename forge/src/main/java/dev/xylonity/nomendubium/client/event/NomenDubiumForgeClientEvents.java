package dev.xylonity.nomendubium.client.event;

import dev.xylonity.nomendubium.NomenDubium;
import dev.xylonity.nomendubium.client.screen.PaleontologyTableScreen;
import dev.xylonity.nomendubium.client.screen.TreeOfLifeScreen;
import dev.xylonity.nomendubium.client.util.ChimeraModelLayers;
import dev.xylonity.nomendubium.client.util.ArrowModelLayers;
import dev.xylonity.nomendubium.client.entity.render.chimera.ChimeraRenderer;
import dev.xylonity.nomendubium.client.entity.model.TreeOfLifeModel;
import dev.xylonity.nomendubium.client.entity.render.TreeOfLifeRenderer;
import dev.xylonity.nomendubium.client.blockentity.CoaldenSignRenderer;
import dev.xylonity.nomendubium.client.projectile.renderer.SkeletonPartRenderer;
import dev.xylonity.nomendubium.client.projectile.renderer.FossilisedMawRenderer;
import dev.xylonity.nomendubium.client.projectile.renderer.HuntersArrowRenderer;
import dev.xylonity.nomendubium.client.projectile.renderer.PrehistoricMawRenderer;
import dev.xylonity.nomendubium.client.projectile.renderer.PrimitiveArrowRenderer;
import dev.xylonity.nomendubium.registry.NomenDubiumEntities;
import dev.xylonity.nomendubium.registry.NomenDubiumMenus;
import dev.xylonity.nomendubium.registry.NomenDubiumBlockEntities;
import dev.xylonity.nomendubium.client.util.SkeletonPartModelLayers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterMenuScreensEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = NomenDubium.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class NomenDubiumForgeClientEvents {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        ChimeraModelLayers.entries().forEach(entry -> event.registerLayerDefinition(entry.location(), entry.definition()));
        SkeletonPartModelLayers.entries().forEach(entry -> event.registerLayerDefinition(entry.location(), entry.definition()));
        ArrowModelLayers.entries().forEach(entry -> event.registerLayerDefinition(entry.location(), entry.definition()));
        event.registerLayerDefinition(TreeOfLifeModel.LAYER_LOCATION, TreeOfLifeModel::createLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(NomenDubiumEntities.CHIMERA.get(), ChimeraRenderer::new);
        event.registerEntityRenderer(NomenDubiumEntities.SKELETON_PART.get(), SkeletonPartRenderer::new);
        event.registerEntityRenderer(NomenDubiumEntities.HUNTERS_ARROW.get(), HuntersArrowRenderer::new);
        event.registerEntityRenderer(NomenDubiumEntities.PRIMITIVE_ARROW.get(), PrimitiveArrowRenderer::new);
        event.registerEntityRenderer(NomenDubiumEntities.PREHISTORIC_MAW.get(), PrehistoricMawRenderer::new);
        event.registerEntityRenderer(NomenDubiumEntities.FOSSILISED_MAW.get(), FossilisedMawRenderer::new);
        event.registerEntityRenderer(NomenDubiumEntities.TREE_OF_LIFE.get(), TreeOfLifeRenderer::new);
        event.registerBlockEntityRenderer(NomenDubiumBlockEntities.COALDEN_SIGN.get(), CoaldenSignRenderer::new);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(NomenDubiumMenus.PALEONTOLOGY_TABLE.get(), PaleontologyTableScreen::new);
        event.register(NomenDubiumMenus.TREE_OF_LIFE.get(), TreeOfLifeScreen::new);
    }

}
