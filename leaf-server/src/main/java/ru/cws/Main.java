package ru.cws;

import com.mojang.logging.LogUtils;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import net.bettercombat.BetterCombatMod;
import net.bettercombat.utils.SoundHelper;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

public class Main {
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void onEnable(JavaPlugin plugin) {
        LOGGER.info("CWS Invoked \"OnEnable\"");
        BetterCombatMod.init(plugin);
    }

    public static void onRegistersInit() {
        LOGGER.info("CWS Invoked \"OnRegistersInit\"");
        SoundHelper.registerSounds();
    }

    public static void bootstrap(JavaPlugin plugin, PluginBootstrap bootstrap, BootstrapContext context) {
        // not work at last moment // need fix helper
        LOGGER.info("CWS Invoked \"Bootstrap\"");
    }
}
