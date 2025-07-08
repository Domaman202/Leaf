package net.bettercombat.network;

import com.google.gson.Gson;
import net.bettercombat.BetterCombatMod;
import net.bettercombat.config.ServerConfig;
import net.bettercombat.logic.AnimatedHand;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Packets {
    public record C2S_AttackRequest(int comboCount, boolean isSneaking, int selectedSlot, int cursorTarget, int[] entityIds) implements CustomPacketPayload {
        public static ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(BetterCombatMod.ID, "c2s_request_attack");
        public static final Type<C2S_AttackRequest> PACKET_ID = new Type<>(ID);
        public static final StreamCodec<RegistryFriendlyByteBuf, C2S_AttackRequest> CODEC = StreamCodec.ofMember(C2S_AttackRequest::write, C2S_AttackRequest::read);

        public static boolean UseVanillaPacket = true;
        public void write(FriendlyByteBuf buffer) {
            buffer.writeInt(comboCount);
            buffer.writeBoolean(isSneaking);
            buffer.writeInt(selectedSlot);
            buffer.writeInt(cursorTarget);
            buffer.writeVarIntArray(entityIds);
        }

        public static C2S_AttackRequest read(FriendlyByteBuf buffer) {
            int comboCount = buffer.readInt();
            boolean isSneaking = buffer.readBoolean();
            int selectedSlot = buffer.readInt();
            int cursorTarget = buffer.readInt();
            int[] ids = buffer.readVarIntArray();
            return new C2S_AttackRequest(comboCount, isSneaking, selectedSlot, cursorTarget, ids);
        }

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return PACKET_ID;
        }
    }

    public record AttackAnimation(int playerId, AnimatedHand animatedHand, String animationName, float length, float upswing) implements CustomPacketPayload {
        public static ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(BetterCombatMod.ID, "attack_animation");
        public static final Type<AttackAnimation> PACKET_ID = new Type<>(ID);
        public static final StreamCodec<RegistryFriendlyByteBuf, AttackAnimation> CODEC = StreamCodec.ofMember(AttackAnimation::write, AttackAnimation::read);

        public static String StopSymbol = "!STOP!";
        public static AttackAnimation stop(int playerId, int length) { return new AttackAnimation(playerId, AnimatedHand.MAIN_HAND, StopSymbol, length, 0); }

        public void write(FriendlyByteBuf buffer) {
            buffer.writeInt(playerId);
            buffer.writeInt(animatedHand.ordinal());
            buffer.writeUtf(animationName);
            buffer.writeFloat(length);
            buffer.writeFloat(upswing);
        }

        public static AttackAnimation read(FriendlyByteBuf buffer) {
            int playerId = buffer.readInt();
            var animatedHand = AnimatedHand.values()[buffer.readInt()];
            String animationName = buffer.readUtf();
            float length = buffer.readFloat();
            float upswing = buffer.readFloat();
            return new AttackAnimation(playerId, animatedHand, animationName, length, upswing);
        }

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return PACKET_ID;
        }
    }

    public record AttackSound(double x, double y, double z, String soundId, float volume, float pitch, long seed) implements CustomPacketPayload {
        public static ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(BetterCombatMod.ID, "attack_sound");
        public static final Type<AttackSound> PACKET_ID = new Type<>(ID);
        public static final StreamCodec<RegistryFriendlyByteBuf, AttackSound> CODEC = StreamCodec.ofMember(AttackSound::write, AttackSound::read);

        public void write(FriendlyByteBuf buffer) {
            buffer.writeDouble(x);
            buffer.writeDouble(y);
            buffer.writeDouble(z);
            buffer.writeUtf(soundId);
            buffer.writeFloat(volume);
            buffer.writeFloat(pitch);
            buffer.writeLong(seed);
        }

        public static AttackSound read(FriendlyByteBuf buffer) {
            var x = buffer.readDouble();
            var y = buffer.readDouble();
            var z = buffer.readDouble();
            var soundId = buffer.readUtf();
            var volume = buffer.readFloat();
            var pitch = buffer.readFloat();
            var seed = buffer.readLong();
            return new AttackSound(x, y, z, soundId, volume, pitch, seed);
        }

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return PACKET_ID;
        }
    }

    public record WeaponRegistrySync(boolean compressed, List<String> chunks) implements CustomPacketPayload {
        public static ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(BetterCombatMod.ID, "weapon_registry");
        public static final Type<WeaponRegistrySync> PACKET_ID = new Type<>(ID);
        public static final StreamCodec<FriendlyByteBuf, WeaponRegistrySync> CODEC = StreamCodec.ofMember(WeaponRegistrySync::write, WeaponRegistrySync::read);

        public void write(FriendlyByteBuf buffer) {
            buffer.writeBoolean(compressed);
            buffer.writeInt(chunks.size());
            for (var chunk: chunks) {
                buffer.writeUtf(chunk);
            }
        }

        public static WeaponRegistrySync read(FriendlyByteBuf buffer) {
            var compressed = buffer.readBoolean();
            var chunkCount = buffer.readInt();
            var chunks = new ArrayList<String>();
            for (int i = 0; i < chunkCount; ++i) {
                chunks.add(buffer.readUtf());
            }
            return new WeaponRegistrySync(compressed, chunks);
        }

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return PACKET_ID;
        }
    }

    public record C2S_BlockHit(BlockPos pos) implements CustomPacketPayload {
        public static ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(BetterCombatMod.ID, "block_hit");
        public static final Type<C2S_BlockHit> PACKET_ID = new Type<>(ID);
        public static final StreamCodec<FriendlyByteBuf, C2S_BlockHit> CODEC = new StreamCodec<FriendlyByteBuf, C2S_BlockHit>() {
            @Override
            public C2S_BlockHit decode(FriendlyByteBuf buffer) {
                return new C2S_BlockHit(BlockPos.STREAM_CODEC.decode(buffer));
            }

            @Override
            public void encode(FriendlyByteBuf buffer, C2S_BlockHit value) {
                BlockPos.STREAM_CODEC.encode(buffer, value.pos);
            }
        };

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return PACKET_ID;
        }
    }

    public record ConfigSync(String json) implements CustomPacketPayload {
        public static ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(BetterCombatMod.ID, "config_sync");
        public static final Type<ConfigSync> PACKET_ID = new Type<>(ID);
        public static final StreamCodec<FriendlyByteBuf, ConfigSync> CODEC = StreamCodec.ofMember(ConfigSync::write, ConfigSync::read);

        private static final Gson gson = new Gson();
        public static String serialize(ServerConfig config) {
            return gson.toJson(config);
        }

        public void write(FriendlyByteBuf buffer) {
            buffer.writeUtf(json);
        }

        public static ConfigSync read(FriendlyByteBuf buffer) {
            var json = buffer.readUtf();
            return new ConfigSync(json);
        }

        public ServerConfig deserialized() {
            return gson.fromJson(json, ServerConfig.class);
        }

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return PACKET_ID;
        }
    }

    public record Ack(String code) implements CustomPacketPayload {
        public static ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(BetterCombatMod.ID, "ack");
        public static final Type<Ack> PACKET_ID = new Type<>(ID);
        public static final StreamCodec<FriendlyByteBuf, Ack> CODEC = StreamCodec.ofMember(Ack::write, Ack::read);

        public void write(FriendlyByteBuf buffer) {
            buffer.writeUtf(code);
        }

        public static Ack read(FriendlyByteBuf buffer) {
            var code = buffer.readUtf();
            return new Ack(code);
        }

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return PACKET_ID;
        }
    }
}
