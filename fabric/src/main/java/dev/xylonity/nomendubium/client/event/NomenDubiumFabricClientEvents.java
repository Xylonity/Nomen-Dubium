package dev.xylonity.nomendubium.client.event;

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
import dev.xylonity.nomendubium.client.screen.PaleontologyTableScreen;
import dev.xylonity.nomendubium.client.screen.TreeOfLifeScreen;
import dev.xylonity.nomendubium.client.util.SkeletonPartModelLayers;
import dev.xylonity.nomendubium.registry.NomenDubiumEntities;
import dev.xylonity.nomendubium.registry.NomenDubiumMenus;
import dev.xylonity.nomendubium.registry.NomenDubiumBlockEntities;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;

public final class NomenDubiumFabricClientEvents {

    public static void init() {
        ChimeraModelLayers.entries().forEach(entry -> ModelLayerRegistry.registerModelLayer(entry.location(), entry.definition()::get));
        SkeletonPartModelLayers.entries().forEach(entry -> ModelLayerRegistry.registerModelLayer(entry.location(), entry.definition()::get));
        ArrowModelLayers.entries().forEach(entry -> ModelLayerRegistry.registerModelLayer(entry.location(), entry.definition()::get));
        ModelLayerRegistry.registerModelLayer(TreeOfLifeModel.LAYER_LOCATION, TreeOfLifeModel::createLayer);

        EntityRenderers.register(NomenDubiumEntities.CHIMERA.get(), ChimeraRenderer::new);
        EntityRenderers.register(NomenDubiumEntities.SKELETON_PART.get(), SkeletonPartRenderer::new);
        EntityRenderers.register(NomenDubiumEntities.HUNTERS_ARROW.get(), HuntersArrowRenderer::new);
        EntityRenderers.register(NomenDubiumEntities.PRIMITIVE_ARROW.get(), PrimitiveArrowRenderer::new);
        EntityRenderers.register(NomenDubiumEntities.PREHISTORIC_MAW.get(), PrehistoricMawRenderer::new);
        EntityRenderers.register(NomenDubiumEntities.FOSSILISED_MAW.get(), FossilisedMawRenderer::new);
        EntityRenderers.register(NomenDubiumEntities.TREE_OF_LIFE.get(), TreeOfLifeRenderer::new);
        BlockEntityRendererRegistry.register(NomenDubiumBlockEntities.COALDEN_SIGN.get(), CoaldenSignRenderer::new);
        MenuScreens.register(NomenDubiumMenus.PALEONTOLOGY_TABLE.get(), PaleontologyTableScreen::new);
        MenuScreens.register(NomenDubiumMenus.TREE_OF_LIFE.get(), TreeOfLifeScreen::new);
    }

}
