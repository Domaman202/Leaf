package net.bettercombat.network;

import net.minecraft.network.protocol.ProtocolCodecBuilder;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;

public class DmNServerPlayNetworking {
    public static final Map<ResourceLocation, PlayPayloadHandler<? extends CustomPacketPayload>> PLAY_HANDLERS = new HashMap<>();
    public static final Map<ResourceLocation, ConfigPayloadHandler<? extends CustomPacketPayload>> CONFIG_HANDLERS = new HashMap<>();

    public static <T extends CustomPacketPayload> void registerGlobalReceiver(Type<T> type, PlayPayloadHandler<T> handler) {
        PLAY_HANDLERS.put(type.id(), handler);
    }


    public static <T extends CustomPacketPayload> void registerGlobalConfigReceiver(Type<T> type, ConfigPayloadHandler<T> handler) {
        CONFIG_HANDLERS.put(type.id(), handler);
    }

    @FunctionalInterface
    public interface PlayPayloadHandler<T extends CustomPacketPayload> {
        void receive(T payload, ServerPlayer sender);
    }


    @FunctionalInterface
    public interface ConfigPayloadHandler<T extends CustomPacketPayload> {
        void receive(T payload, ServerPlayer sender);
    }
}
