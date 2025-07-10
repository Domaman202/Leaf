package net.bettercombat;

import com.mojang.logging.LogUtils;
import net.bettercombat.config.FallbackConfig;
import net.bettercombat.config.FullConfig;
import net.bettercombat.logic.WeaponAttributesFallback;
import net.bettercombat.logic.WeaponRegistry;
import net.bettercombat.network.ServerNetwork;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

public class BetterCombatMod {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String ID = "bettercombat";
    public static final FullConfig config = new FullConfig();

    public static void init(JavaPlugin plugin) {
        ServerNetwork.init();
        loadConfig(plugin);
        loadWeaponAttributes(plugin);
    }

    private static void loadWeaponAttributes(JavaPlugin plugin) {
        WeaponRegistry.loadAttributes(plugin);
        if (config.server.fallback_compatibility_enabled) {
            WeaponAttributesFallback.initialize();
        }
        WeaponRegistry.encodeRegistry();
    }

    private static void loadConfig(JavaPlugin plugin) {
        var defaultConfig = FallbackConfig.createDefault();
        config.load(plugin);
        if (config.fallback.schema_version < defaultConfig.schema_version) {
            config.fallback = FallbackConfig.migrate(config.fallback, defaultConfig);
            config.save(plugin, false, true);
        }
    }
}
