package ru.cws;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import net.bettercombat.BetterCombatMod;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Main {
    public static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void onEnable(JavaPlugin plugin) {
        LOGGER.info("CWS Invoked \"OnEnable\"");
        BetterCombatMod.init(plugin);
    }

    public static void bootstrap(JavaPlugin plugin, PluginBootstrap bootstrap, BootstrapContext context) {
        // not work at last moment // need fix helper
        LOGGER.info("CWS Invoked \"Bootstrap\"");
    }
}
