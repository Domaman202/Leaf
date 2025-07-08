package net.bettercombat;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;

public class PlatformImpl {
    public static FriendlyByteBuf createByteBuffer() {
        return new FriendlyByteBuf(Unpooled.buffer());
    }

    public static Collection<ServerPlayer> tracking(ServerPlayer player) {
        return PlayerLookup.tracking(player);
    }

    public static Collection<ServerPlayer> around(ServerLevel world, Vec3 origin, double distance) {
        return PlayerLookup.around(world, origin, distance);
    }

    public static boolean networkS2C_CanSend(ServerPlayer player, ResourceLocation packetId) {
        return ServerPlayNetworking.canSend(player, packetId); // todo: DomamaN202
    }

    public static void networkS2C_Send(ServerPlayer player, CustomPacketPayload payload) {
        ServerPlayNetworking.send(player, payload);  // todo: DomamaN202
    }
}
