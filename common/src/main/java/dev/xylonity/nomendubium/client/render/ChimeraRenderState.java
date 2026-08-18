package dev.xylonity.nomendubium.client.render;

import dev.xylonity.nomendubium.common.entity.variant.ChimeraBodyVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraBackVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraHeadVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraPaletteVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraTailVariant;
import net.minecraft.client.renderer.entity.state.EntityRenderState;


public final class ChimeraRenderState extends EntityRenderState {
    public float yRot;
    public ChimeraBodyVariant body = ChimeraBodyVariant.HULKING;
    public ChimeraHeadVariant head = ChimeraHeadVariant.CRUNCHING;
    public ChimeraTailVariant tail = ChimeraTailVariant.SPIKED;
    public ChimeraBackVariant back = ChimeraBackVariant.NONE;
    public ChimeraPaletteVariant palette = ChimeraPaletteVariant.NORMAL;
}
