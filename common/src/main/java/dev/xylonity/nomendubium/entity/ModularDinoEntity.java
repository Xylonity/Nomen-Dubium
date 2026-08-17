package dev.xylonity.nomendubium.entity;

import dev.xylonity.nomendubium.common.entity.variant.ModularDinoBodyVariant;
import dev.xylonity.nomendubium.common.entity.variant.ModularDinoBackVariant;
import dev.xylonity.nomendubium.common.entity.variant.ModularDinoHeadVariant;
import dev.xylonity.nomendubium.common.entity.variant.ModularDinoPaletteVariant;
import dev.xylonity.nomendubium.common.entity.variant.ModularDinoTailVariant;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public final class ModularDinoEntity extends PathfinderMob {

    private static final EntityDataAccessor<Integer> BODY = SynchedEntityData.defineId(ModularDinoEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> HEAD = SynchedEntityData.defineId(ModularDinoEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TAIL = SynchedEntityData.defineId(ModularDinoEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> BACK = SynchedEntityData.defineId(ModularDinoEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PALETTE = SynchedEntityData.defineId(ModularDinoEntity.class, EntityDataSerializers.INT);

    public ModularDinoEntity(EntityType<? extends ModularDinoEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NonNull Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(BODY, ModularDinoBodyVariant.HULKING.index());
        entityData.define(HEAD, ModularDinoHeadVariant.CRUNCHING.index());
        entityData.define(TAIL, ModularDinoTailVariant.SPIKED.index());
        entityData.define(BACK, ModularDinoBackVariant.NONE.index());
        entityData.define(PALETTE, ModularDinoPaletteVariant.NORMAL.index());
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
        // TODO: remove
        final ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem.is(Items.DIAMOND)) {
            if (level().isClientSide()) {
                return InteractionResult.SUCCESS;
            }

            setBodyVariant(getBodyVariant().next());

            return InteractionResult.SUCCESS_SERVER;
        }

        if (heldItem.is(Items.IRON_INGOT)) {
            if (level().isClientSide()) {
                return InteractionResult.SUCCESS;
            }

            setHeadVariant(getHeadVariant().next());

            return InteractionResult.SUCCESS_SERVER;
        }

        if (heldItem.is(Items.GOLD_INGOT)) {
            if (level().isClientSide()) {
                return InteractionResult.SUCCESS;
            }

            setTailVariant(getTailVariant().next());

            return InteractionResult.SUCCESS_SERVER;
        }

        if (heldItem.is(Items.COPPER_INGOT)) {
            if (level().isClientSide()) {
                return InteractionResult.SUCCESS;
            }

            setPaletteVariant(getPaletteVariant().next());

            return InteractionResult.SUCCESS_SERVER;
        }

        if (heldItem.is(Items.NETHERITE_INGOT)) {
            if (level().isClientSide()) {
                return InteractionResult.SUCCESS;
            }

            setBackVariant(getBackVariant().next());

            return InteractionResult.SUCCESS_SERVER;
        }

        return super.interact(player, hand, location);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        setBodyVariant(ModularDinoBodyVariant.index(input.getIntOr("bodyvariant", ModularDinoBodyVariant.HULKING.index())));
        setHeadVariant(ModularDinoHeadVariant.index(input.getIntOr("headvariant", ModularDinoHeadVariant.CRUNCHING.index())));
        setTailVariant(ModularDinoTailVariant.index(input.getIntOr("tailvariant", ModularDinoTailVariant.SPIKED.index())));
        setBackVariant(ModularDinoBackVariant.index(input.getIntOr("backvariant", ModularDinoBackVariant.NONE.index())));
        final int legacyPalette = input.getBooleanOr("junglepalette", false) ? ModularDinoPaletteVariant.JUNGLE.index() : ModularDinoPaletteVariant.NORMAL.index();
        setPaletteVariant(ModularDinoPaletteVariant.index(input.getIntOr("palettevariant", legacyPalette)));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.putInt("bodyvariant", getBodyVariant().index());
        output.putInt("headvariant", getHeadVariant().index());
        output.putInt("tailvariant", getTailVariant().index());
        output.putInt("backvariant", getBackVariant().index());
        output.putInt("palettevariant", getPaletteVariant().index());
    }

    public ModularDinoBodyVariant getBodyVariant() {
        return ModularDinoBodyVariant.index(entityData.get(BODY));
    }

    public void setBodyVariant(ModularDinoBodyVariant variant) {
        entityData.set(BODY, variant.index());
    }

    public ModularDinoHeadVariant getHeadVariant() {
        return ModularDinoHeadVariant.index(entityData.get(HEAD));
    }

    public void setHeadVariant(ModularDinoHeadVariant variant) {
        entityData.set(HEAD, variant.index());
    }

    public ModularDinoTailVariant getTailVariant() {
        return ModularDinoTailVariant.index(entityData.get(TAIL));
    }

    public void setTailVariant(ModularDinoTailVariant variant) {
        entityData.set(TAIL, variant.index());
    }

    public ModularDinoBackVariant getBackVariant() {
        return ModularDinoBackVariant.index(entityData.get(BACK));
    }

    public void setBackVariant(ModularDinoBackVariant variant) {
        entityData.set(BACK, variant.index());
    }

    public ModularDinoPaletteVariant getPaletteVariant() {
        return ModularDinoPaletteVariant.index(entityData.get(PALETTE));
    }

    public void setPaletteVariant(ModularDinoPaletteVariant variant) {
        entityData.set(PALETTE, variant.index());
    }

}