package net.bettercombat;

import com.mojang.logging.LogUtils;
import net.bettercombat.config.FallbackConfig;
import net.bettercombat.config.ServerConfig;
import net.bettercombat.logic.WeaponAttributesFallback;
import net.bettercombat.logic.WeaponRegistry;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;

public class BetterCombatMod {  // todo: normal config loading
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String ID = "bettercombat";
    public static ServerConfig config = new ServerConfig();
    private static FallbackConfig fallbackDefault = FallbackConfig.createDefault();
    public static FallbackConfig fallbackConfig = new FallbackConfig();

    public static ServerConfig getConfig() {
        return config;
    }

    public static void loadWeaponAttributes(MinecraftServer server) {
        WeaponRegistry.loadAttributes(server.getResourceManager());
        if (config.fallback_compatibility_enabled) {
            WeaponAttributesFallback.initialize();
        }
        WeaponRegistry.encodeRegistry();
    }
}
