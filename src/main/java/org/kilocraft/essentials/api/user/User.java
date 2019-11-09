package org.kilocraft.essentials.api.user;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.kilocraft.essentials.api.feature.FeatureType;
import org.kilocraft.essentials.api.feature.UserProvidedFeature;

import java.util.Date;
import java.util.UUID;

public interface User {
    UUID getUuid();

    String getUsername();

    boolean isOnline();

    @Nullable
    String getNickname();

    void setNickname(String name);

    void clearNickname();

    @Nullable
    Identifier getBackDimId();

    @Nullable
    Vec3d getBackPos();

    void setBackPos(Vec3d position);

    default void setBackPos(BlockPos blockPos) {
        setBackPos(new Vec3d(blockPos));
    }

    default void setBackPos(double x, double y, double z) {
        setBackPos(new Vec3d(x, y, z));
    }

    void setBackDim(Identifier dim);

    Identifier getPosDim();

    @Nullable
    Vec3d getPos();

    boolean canFly();

    void setFlight(boolean set);

    boolean hasJoinedBefore();

    @Nullable
    Date getFirstJoin();

    boolean isInvulnerable();

    void setInvulnerable(boolean set);

    int getRTPsLeft();

    void setRTPsLeft(int amount);

    @Nullable
    UUID getLastPrivateMessageSender();

    @Nullable
    String getLastPrivateMessage();

    void setLastMessageSender(UUID uuid);

    <F extends UserProvidedFeature> F feature(FeatureType<F> type);
}