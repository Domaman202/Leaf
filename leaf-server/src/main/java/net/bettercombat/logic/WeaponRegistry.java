package net.bettercombat.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.mojang.logging.LogUtils;
import net.bettercombat.BetterCombatMod;
import net.bettercombat.PlatformImpl;
import net.bettercombat.api.AttributesContainer;
import net.bettercombat.api.WeaponAttributes;
import net.bettercombat.api.WeaponAttributesHelper;
import net.bettercombat.api.component.BetterCombatDataComponents;
import net.bettercombat.network.Packets;
import net.bettercombat.utils.CompressionHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeaponRegistry {
    static final Logger LOGGER = LogUtils.getLogger();
    // Actual attributes to weapon assignments
    static Map<ResourceLocation, WeaponAttributes> registrations = new HashMap<>();
    static Map<ResourceLocation, AttributesContainer> containers = new HashMap<>();

    public static void register(ResourceLocation itemId, WeaponAttributes attributes) {
        registrations.put(itemId, attributes);
    }

    static WeaponAttributes getAttributes(ResourceLocation itemId) {
        return registrations.get(itemId);
    }

    public static WeaponAttributes getAttributes(ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }
//        var attributes = WeaponAttributesHelper.readFromNBT(itemStack);
//        if (attributes != null) {
//            return attributes;
//        }

        var component = itemStack.get(BetterCombatDataComponents.WEAPON_PRESET_ID);
        if (component != null) {
            var container = containers.get(component);
            if (container != null) {
                return container.attributes();
            }
        }

        Item item = itemStack.getItem();
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        return WeaponRegistry.getAttributes(id);
    }

    // LOADING

    public static void loadAttributes(JavaPlugin plugin) {
        // Копирование и загрузка данных из конфига
        if (!new File(plugin.getDataFolder(), "config/bettercombat/attributes").exists())
            copyData(plugin);
        loadContainers(plugin);

        // Resolving parents
        containers.forEach((itemId, container) -> {
            if (!BuiltInRegistries.ITEM.containsKey(itemId)) {
                return;
            }
            resolveAndRegisterAttributes(itemId, container);
        });
    }

    private static void copyData(JavaPlugin plugin) {
        BetterCombatMod.LOGGER.info("Saving \"attributes\" config");
        for (var entry : MinecraftServer.getServer().getResourceManager().listResources("weapon_attributes", fileName -> fileName.getPath().endsWith(".json")).entrySet()) {
            var file = entry.getKey().toString();
            var i = file.indexOf(":");
            file = plugin.getDataFolder().getAbsolutePath() + "/config/bettercombat/attributes/" + file.substring(0, i) + "/" + file.substring(file.indexOf("/", i) + 1);
            new File(file.substring(0, file.lastIndexOf("/"))).mkdirs();
            try (var output = new FileOutputStream(file)) {
                try (var input = entry.getValue().open()) {
                    output.write(input.readAllBytes());
                }
            } catch (IOException e) {
                BetterCombatMod.LOGGER.trace("Config loading failed", e);
            }
        }
    }

    private static void loadContainers(JavaPlugin plugin) {
        Map<ResourceLocation, AttributesContainer> containers = new HashMap<>();

//        // Reading all attribute files
//        for (var entry : MinecraftServer.getServer().getResourceManager().listResources("weapon_attributes", fileName -> fileName.getPath().endsWith(".json")).entrySet()) {
//            var identifier = entry.getKey();
//            var resource = entry.getValue();
//            try {
////                LOGGER.info("Checking resource: " + identifier);
//                JsonReader reader = new JsonReader(new InputStreamReader(resource.open()));
//                AttributesContainer container = WeaponAttributesHelper.decode(reader);
//                var id = identifier.toString().replace("weapon_attributes/", "");
//                id = id.substring(0, id.lastIndexOf('.'));
//                containers.put(ResourceLocation.parse(id), container);
//                LOGGER.info("Loaded container: {}", id);
//            } catch (Exception e) {
//                LOGGER.error("Failed to parse: {}", identifier);
//                e.printStackTrace();
//            }
//        }

        // Reading all attribute files
        for (var dir : new File(plugin.getDataFolder().getAbsolutePath(), "config/bettercombat/attributes").listFiles()) {
            for (var file : dir.listFiles()) {
                var path = file.getAbsolutePath();
                var i = path.lastIndexOf("/");
                var identifier = path.substring(path.lastIndexOf("/", i - 1) + 1, i);
                var resource = path.substring(i + 1, path.lastIndexOf("."));
                try {
//                    LOGGER.info("Checking resource: " + identifier);
                    JsonReader reader = new JsonReader(new FileReader(file));
                    AttributesContainer container = WeaponAttributesHelper.decode(reader);
                    var id = ResourceLocation.fromNamespaceAndPath(identifier, resource);
                    containers.put(id, container);
                    LOGGER.info("Loaded container: {}", id);
                } catch (Exception e) {
                    LOGGER.error("Failed to parse: {}", identifier);
                    e.printStackTrace();
                }
            }
        }

        // Do not remove this
        WeaponRegistry.containers = containers;
        // The following container resolution will use these containers

        Map<ResourceLocation, AttributesContainer> resolvedContainers = new HashMap<>();
        for (var entry : containers.entrySet()) {
            var id = entry.getKey();
            var container = entry.getValue();
            if (container.parent() != null) {
                var resolvedAttributes = resolveAttributes(id, container);
                if (resolvedAttributes != null) {
                    container = new AttributesContainer(null, resolvedAttributes);
                }
            }
            resolvedContainers.put(id, container);
        }

        WeaponRegistry.containers = resolvedContainers;
    }

    public static WeaponAttributes resolveAttributes(ResourceLocation itemId, AttributesContainer container) {
        try {
            ArrayList<WeaponAttributes> resolutionChain = new ArrayList<>();
            AttributesContainer current = container;
            while (current != null) {
                resolutionChain.add(0, current.attributes());
                if (current.parent() != null) {
                    current = containers.get(ResourceLocation.parse(current.parent()));
                } else {
                    current = null;
                }
            }

            var empty = WeaponAttributes.empty();
            var resolvedAttributes = resolutionChain
                    .stream()
                    .reduce(empty, (a, b) -> {
                        if (b == null) { // I'm not sure why null can enter as `b`
                            return a;
                        }
                        return WeaponAttributesHelper.override(a, b);
                    });

            WeaponAttributesHelper.validate(resolvedAttributes);
            return resolvedAttributes;
        } catch (Exception e) {
            LOGGER.error("Failed to resolve weapon attributes for: " + itemId + ". Reason: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static void resolveAndRegisterAttributes(ResourceLocation itemId, AttributesContainer container) {
        var resolvedAttributes = resolveAttributes(itemId, container);
        if (resolvedAttributes != null) {
            register(itemId, resolvedAttributes);
        }
    }

    // NETWORK SYNC

    private static Encoded encodedRegistrations = new Encoded(true, List.of());
    public record Encoded(boolean compressed, List<String> chunks) {}
    private static final int CHUNK_SIZE = 10000;
    private static final Gson gson = new GsonBuilder().create();
    public static class SyncFormat {
        public Map<String, AttributesContainer> attributes = new HashMap<>();
        public Map<String, WeaponAttributes> registrations = new HashMap<>();
    }

    public static void encodeRegistry() {
        var compressed = BetterCombatMod.config.server.weapon_registry_compression;
        List<String> chunks = new ArrayList<>();
        var syncContent = new SyncFormat();
        containers.forEach((key, value) -> {
            syncContent.attributes.put(key.toString(), value);
        });
        registrations.forEach((key, value) -> {
            syncContent.registrations.put(key.toString(), value);
        });

        var json = gson.toJson(syncContent);
        if (compressed) {
            json = CompressionHelper.gzipCompress(json);
        }
        if (BetterCombatMod.config.server.weapon_registry_logging) {
            LOGGER.info("Weapon Attribute assignments loaded: " + json);
        }
        for (int i = 0; i < json.length(); i += CHUNK_SIZE) {
            chunks.add(json.substring(i, Math.min(json.length(), i + CHUNK_SIZE)));
        }

        encodedRegistrations = new Encoded(compressed, chunks);

        var referencePacket = new Packets.WeaponRegistrySync(compressed, chunks);
        var buffer = PlatformImpl.createByteBuffer();
        referencePacket.write(buffer);
        LOGGER.info("Encoded Weapon Attribute registry size (with package overhead): " + buffer.readableBytes()
                + " bytes (in " + chunks.size() + " string chunks with the size of "  + CHUNK_SIZE + ")");
    }

    public static Encoded getEncodedRegistry() {
        return encodedRegistrations;
    }
}
