package dev.xylonity.nomendubium.registry;

import dev.xylonity.nomendubium.mixin.WoodTypeAccessor;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public final class NomenDubiumWoodTypes {

    public static final WoodType COALDEN = WoodTypeAccessor.nomendubium$register(new WoodType("nomendubium_coalden", BlockSetType.OAK));

}