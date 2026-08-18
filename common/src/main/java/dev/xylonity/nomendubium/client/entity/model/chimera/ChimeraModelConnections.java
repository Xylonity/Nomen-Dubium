package dev.xylonity.nomendubium.client.entity.model.chimera;

import net.minecraft.client.model.geom.PartPose;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class ChimeraModelConnections {

    public static PartPose alignToConnection(float connectionX, float connectionY, float connectionZ) {
        return PartPose.offset(-connectionX, -connectionY, -connectionZ);
    }

    public static PartPose alignToConnection(
        float connectionX,
        float connectionY,
        float connectionZ,
        float xRotation,
        float yRotation,
        float zRotation
    ) {
        final Vector3f offset = new Vector3f(connectionX, connectionY, connectionZ);
        // Negating the rotated connection vector so the connection point remains at the local origin and the extra parts align properly
        new Quaternionf().rotationZYX(zRotation, yRotation, xRotation).transform(offset).negate();
        return PartPose.offsetAndRotation(offset.x, offset.y, offset.z, xRotation, yRotation, zRotation);
    }

}