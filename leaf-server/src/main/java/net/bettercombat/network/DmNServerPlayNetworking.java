package net.bettercombat.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;

public class DmNServerPlayNetworking {
    public static final Map<Type<?>, PlayPayloadHandler<? extends CustomPacketPayload>> HANDLERS = new HashMap<>();

    public static <T extends CustomPacketPayload> void registerGlobalReceiver(Type<T> type, PlayPayloadHandler<T> handler) {
        HANDLERS.put(type, handler);
    }

    @FunctionalInterface
    public interface PlayPayloadHandler<T extends CustomPacketPayload> {
        void receive(T payload, ServerPlayer sender);
    }
}
