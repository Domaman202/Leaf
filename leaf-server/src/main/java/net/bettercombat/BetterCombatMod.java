package net.bettercombat;

import com.mojang.logging.LogUtils;
import net.bettercombat.config.FallbackConfig;
import net.bettercombat.config.FullConfig;
import net.bettercombat.config.ServerConfig;
import net.bettercombat.logic.WeaponAttributesFallback;
import net.bettercombat.logic.WeaponRegistry;
import net.minecraft.server.MinecraftServer;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

public class BetterCombatMod {  // todo: normal config loading
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String ID = "bettercombat";
    public static final FullConfig config = new FullConfig();

    public static void init(JavaPlugin plugin) {
        loadFallbackConfig(plugin);
        loadWeaponAttributes(MinecraftServer.getServer());
    }

    private static void loadWeaponAttributes(MinecraftServer server) {
        WeaponRegistry.loadAttributes(server.getResourceManager());
        if (config.server.fallback_compatibility_enabled) {
            WeaponAttributesFallback.initialize();
        }
        WeaponRegistry.encodeRegistry();
    }

    private static void loadFallbackConfig(JavaPlugin plugin) {
        var defaultConfig = FallbackConfig.createDefault();
        config.load(plugin);
        if (config.fallback.schema_version < defaultConfig.schema_version)
            config.fallback = FallbackConfig.migrate(config.fallback, defaultConfig);
        config.save(plugin);
    }
}
