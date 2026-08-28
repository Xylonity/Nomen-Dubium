package dev.xylonity.nomendubium.client.entity.model.chimera.normal.body;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xylonity.nomendubium.client.entity.render.chimera.ChimeraRenderState;
import java.util.Arrays;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import org.joml.Matrix4fc;
import org.joml.Quaternionf;

/// A chunk of the implementation is based off citadel's helpers for procedural animation handling (specially for wings motion)
public abstract class ChimeraBodyModel extends EntityModel<ChimeraRenderState> {

    private final ModelPart body;
    private final ModelPart torso;
    private final ModelPart headConnection;
    private final ModelPart tailConnection;
    private final ModelPart backConnection;
    private final Gait gait;
    private final LegRig[] legs;

    protected ChimeraBodyModel(ModelPart root, Gait gait, LegSpec... legSpecs) {
        super(root);
        this.body = root.getChild("body");
        this.torso = this.body.getChild("torso");
        this.headConnection = this.body.getChild("head_connection");
        this.tailConnection = this.body.getChild("tail_connection");
        this.backConnection = this.body.getChild("extra_connection");
        this.gait = gait;
        this.legs = Arrays.stream(legSpecs)
            .map(spec -> spec.resolve(this.body))
            .toArray(LegRig[]::new);
    }

    @Override
    public void setupAnim(ChimeraRenderState state) {
        super.setupAnim(state);

        final float movement = Mth.clamp(state.walkAnimationSpeed, 0.0F, 1.0F);
        final float idleWeight = 1.0F - movement;
        final float walkPhase = state.walkAnimationPos * gait.frequency;
        final float idlePhase = state.ageInTicks * 0.09F;

        // Small whole body translation inherited by every part
        this.body.y += Mth.sin(idlePhase) * gait.idleBob * idleWeight;
        this.body.y += Mth.abs(Mth.sin(walkPhase)) * gait.walkBob * movement;

        // Torso only rotations that don't affect other parts
        this.torso.xRot += Mth.sin(idlePhase * 0.55F) * gait.idlePitch * idleWeight;
        this.torso.xRot += Mth.cos(walkPhase * 2.0F) * gait.walkPitch * movement;
        this.torso.zRot += Mth.sin(walkPhase) * gait.walkRoll * movement;

        for (final LegRig leg : this.legs) {
            animateLeg(leg, walkPhase, movement);
        }

    }

    private void animateLeg(LegRig leg, float walkPhase, float movement) {
        final float phase = walkPhase + leg.phase;
        final float swing = Mth.cos(phase) * this.gait.stride * movement;
        leg.upper.xRot += swing;

        if (leg.lower == null) {
            return;
        }

        final float recovery = Math.max(0.0F, Mth.sin(phase)) * movement;
        final float knee = recovery * this.gait.kneeBend - swing * this.gait.kneeFollow;
        leg.lower.xRot += knee;

        if (leg.foot != null) {
            leg.foot.xRot -= knee * this.gait.footCompensation;
            leg.foot.xRot -= swing * 0.12F;
        }

    }

    public void moveToHead(PoseStack poseStack) {
        this.moveTo(this.headConnection, poseStack);
    }

    public void moveToTail(PoseStack poseStack) {
        this.moveTo(this.tailConnection, poseStack);
    }

    public void moveToBack(PoseStack poseStack) {
        this.moveTo(this.backConnection, poseStack);
    }

    public void moveToRider(PoseStack poseStack) {
        final PoseStack riderPose = new PoseStack();
        this.body.translateAndRotate(riderPose);
        this.torso.translateAndRotate(riderPose);
        riderPose.translate(
            (this.backConnection.x - this.torso.x) / 16f,
            (this.backConnection.y - this.torso.y) / 16f,
            (this.backConnection.z - this.torso.z) / 16f
        );

        final Matrix4fc transform = riderPose.last().pose();
        poseStack.translate(transform.m30(), transform.m31(), transform.m32());

        // Inherits the animated rotation without carrying authored base rotations into the player model
        final Quaternionf bodyRotation = new Quaternionf().rotationZYX(this.body.zRot, this.body.yRot, this.body.xRot);
        final Quaternionf initialBodyRotation = new Quaternionf().rotationZYX(
            this.body.getInitialPose().zRot(),
            this.body.getInitialPose().yRot(),
            this.body.getInitialPose().xRot()
        );
        final Quaternionf initialTorsoRotation = new Quaternionf().rotationZYX(
            this.torso.getInitialPose().zRot(),
            this.torso.getInitialPose().yRot(),
            this.torso.getInitialPose().xRot()
        );

        final Quaternionf torsoAnimation = new Quaternionf().rotationZYX(this.torso.zRot, this.torso.yRot, this.torso.xRot)
            .mul(initialTorsoRotation.conjugate());

        poseStack.mulPose(new Quaternionf(bodyRotation).mul(torsoAnimation).mul(initialBodyRotation.conjugate()));
    }

    private void moveTo(ModelPart connection, PoseStack poseStack) {
        final PoseStack connectionPose = new PoseStack();
        this.body.translateAndRotate(connectionPose);
        connection.translateAndRotate(connectionPose);
        final Matrix4fc transform = connectionPose.last().pose();
        poseStack.translate(transform.m30(), transform.m31(), transform.m32());
    }

    protected static LegSpec leg(String upper, float phase) {
        return new LegSpec(upper, null, null, phase);
    }

    protected static LegSpec leg(String upper, String lower, String foot, float phase) {
        return new LegSpec(upper, lower, foot, phase);
    }

    protected enum Gait {
        HULKING(0.62F, 0.48F, 0.0F, 0.0F, 0.0F, 0.13F, 0.45F, 0.012F, 0.020F, 0.018F),
        SHELLED(0.52F, 0.32F, 0.0F, 0.0F, 0.0F, 0.08F, 0.28F, 0.008F, 0.012F, 0.010F),
        AVIAN(0.76F, 0.72F, 0.70F, 0.22F, 0.72F, 0.18F, 0.55F, 0.018F, 0.032F, 0.026F),
        LANKY(0.58F, 0.38F, 0.0F, 0.0F, 0.0F, 0.16F, 0.72F, 0.015F, 0.025F, 0.020F),
        PUFFY(0.82F, 0.52F, 0.0F, 0.0F, 0.0F, 0.24F, 0.38F, 0.020F, 0.035F, 0.030F);

        private final float frequency;
        private final float stride;
        private final float kneeBend;
        private final float kneeFollow;
        private final float footCompensation;
        private final float idleBob;
        private final float walkBob;
        private final float idlePitch;
        private final float walkPitch;
        private final float walkRoll;

        Gait(float frequency, float stride, float kneeBend, float kneeFollow, float footCompensation, float idleBob, float walkBob, float idlePitch, float walkPitch, float walkRoll) {
            this.frequency = frequency;
            this.stride = stride;
            this.kneeBend = kneeBend;
            this.kneeFollow = kneeFollow;
            this.footCompensation = footCompensation;
            this.idleBob = idleBob;
            this.walkBob = walkBob;
            this.idlePitch = idlePitch;
            this.walkPitch = walkPitch;
            this.walkRoll = walkRoll;
        }

    }

    protected record LegSpec(
            String upper,
            String lower,
            String foot,
            float phase
    ) {

        private LegRig resolve(ModelPart body) {
            final ModelPart upperPart = body.getChild(this.upper);
            final ModelPart lowerPart = this.lower == null ? null : upperPart.getChild(this.lower);
            final ModelPart footPart = this.foot == null ? null : lowerPart.getChild(this.foot);
            return new LegRig(upperPart, lowerPart, footPart, this.phase);
        }

    }

    private record LegRig(
            ModelPart upper,
            ModelPart lower,
            ModelPart foot,
            float phase
    ) {
        ;;
    }

}
