package dev.xylonity.nomendubium.client.render;

import dev.xylonity.nomendubium.common.entity.variant.ModularDinoBodyVariant;
import dev.xylonity.nomendubium.common.entity.variant.ModularDinoBackVariant;
import dev.xylonity.nomendubium.common.entity.variant.ModularDinoHeadVariant;
import dev.xylonity.nomendubium.common.entity.variant.ModularDinoPaletteVariant;
import dev.xylonity.nomendubium.common.entity.variant.ModularDinoTailVariant;
import net.minecraft.client.renderer.entity.state.EntityRenderState;


public final class ModularDinoRenderState extends EntityRenderState {
    public float yRot;
    public ModularDinoBodyVariant body = ModularDinoBodyVariant.HULKING;
    public ModularDinoHeadVariant head = ModularDinoHeadVariant.CRUNCHING;
    public ModularDinoTailVariant tail = ModularDinoTailVariant.SPIKED;
    public ModularDinoBackVariant back = ModularDinoBackVariant.NONE;
    public ModularDinoPaletteVariant palette = ModularDinoPaletteVariant.NORMAL;
}
