package dev.xylonity.nomendubium.entity;

import dev.xylonity.nomendubium.common.entity.variant.ChimeraBodyVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraBackVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraHeadVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraPaletteVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraTailVariant;
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

public final class ChimeraEntity extends PathfinderMob {

    private static final EntityDataAccessor<Integer> BODY = SynchedEntityData.defineId(ChimeraEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> HEAD = SynchedEntityData.defineId(ChimeraEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TAIL = SynchedEntityData.defineId(ChimeraEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> BACK = SynchedEntityData.defineId(ChimeraEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PALETTE = SynchedEntityData.defineId(ChimeraEntity.class, EntityDataSerializers.INT);

    public ChimeraEntity(EntityType<? extends ChimeraEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NonNull Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(BODY, ChimeraBodyVariant.HULKING.index());
        entityData.define(HEAD, ChimeraHeadVariant.CRUNCHING.index());
        entityData.define(TAIL, ChimeraTailVariant.SPIKED.index());
        entityData.define(BACK, ChimeraBackVariant.NONE.index());
        entityData.define(PALETTE, ChimeraPaletteVariant.NORMAL.index());
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
        setBodyVariant(ChimeraBodyVariant.index(input.getIntOr("bodyvariant", ChimeraBodyVariant.HULKING.index())));
        setHeadVariant(ChimeraHeadVariant.index(input.getIntOr("headvariant", ChimeraHeadVariant.CRUNCHING.index())));
        setTailVariant(ChimeraTailVariant.index(input.getIntOr("tailvariant", ChimeraTailVariant.SPIKED.index())));
        setBackVariant(ChimeraBackVariant.index(input.getIntOr("backvariant", ChimeraBackVariant.NONE.index())));
        final int legacyPalette = input.getBooleanOr("junglepalette", false) ? ChimeraPaletteVariant.JUNGLE.index() : ChimeraPaletteVariant.NORMAL.index();
        setPaletteVariant(ChimeraPaletteVariant.index(input.getIntOr("palettevariant", legacyPalette)));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.putInt("bodyvariant", getBodyVariant().index());
        output.putInt("headvariant", getHeadVariant().index());
        output.putInt("tailvariant", getTailVariant().index());
        output.putInt("backvariant", getBackVariant().index());
        output.putInt("palettevariant", getPaletteVariant().index());
    }

    public ChimeraBodyVariant getBodyVariant() {
        return ChimeraBodyVariant.index(entityData.get(BODY));
    }

    public void setBodyVariant(ChimeraBodyVariant variant) {
        entityData.set(BODY, variant.index());
    }

    public ChimeraHeadVariant getHeadVariant() {
        return ChimeraHeadVariant.index(entityData.get(HEAD));
    }

    public void setHeadVariant(ChimeraHeadVariant variant) {
        entityData.set(HEAD, variant.index());
    }

    public ChimeraTailVariant getTailVariant() {
        return ChimeraTailVariant.index(entityData.get(TAIL));
    }

    public void setTailVariant(ChimeraTailVariant variant) {
        entityData.set(TAIL, variant.index());
    }

    public ChimeraBackVariant getBackVariant() {
        return ChimeraBackVariant.index(entityData.get(BACK));
    }

    public void setBackVariant(ChimeraBackVariant variant) {
        entityData.set(BACK, variant.index());
    }

    public ChimeraPaletteVariant getPaletteVariant() {
        return ChimeraPaletteVariant.index(entityData.get(PALETTE));
    }

    public void setPaletteVariant(ChimeraPaletteVariant variant) {
        entityData.set(PALETTE, variant.index());
    }

}