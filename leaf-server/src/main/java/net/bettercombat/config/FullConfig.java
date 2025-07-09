package net.bettercombat.config;

import net.bettercombat.BetterCombatMod;
import net.bettercombat.logic.TargetHelper;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class FullConfig {
    public ServerConfig server;
    public FallbackConfig fallback;

    public void load(JavaPlugin plugin) {
        try {
            this.server = new ServerConfig();
            this.fallback = FallbackConfig.createDefault();
            // Проверка директории
            if (!new File(plugin.getDataFolder(), "config/bettercombat").exists()) {
                this.save(plugin);
                return;
            }
            // Server
            var sc = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config/bettercombat/server.yml"));
            this.server.upswing_multiplier = (float) sc.getDouble("upswing_multiplier", this.server.upswing_multiplier);
            this.server.allow_fast_attacks = sc.getBoolean("allow_fast_attacks", this.server.allow_fast_attacks);
            this.server.allow_attacking_mount = sc.getBoolean("allow_attacking_mount", this.server.allow_attacking_mount);
            this.server.attack_interval_cap = sc.getInt("attack_interval_cap", this.server.attack_interval_cap);
            this.server.hostile_player_vehicles = getStringArray(sc, "hostile_player_vehicles", this.server.hostile_player_vehicles);
            this.server.allow_vanilla_sweeping = sc.getBoolean("allow_vanilla_sweeping", this.server.allow_vanilla_sweeping);
            this.server.allow_reworked_sweeping = sc.getBoolean("allow_reworked_sweeping", this.server.allow_reworked_sweeping);
            this.server.reworked_sweeping_extra_target_count = sc.getInt("reworked_sweeping_extra_target_count", this.server.reworked_sweeping_extra_target_count);
            this.server.reworked_sweeping_maximum_damage_penalty = (float) sc.getDouble("reworked_sweeping_maximum_damage_penalty", this.server.reworked_sweeping_maximum_damage_penalty);
            this.server.reworked_sweeping_plays_sound = sc.getBoolean("reworked_sweeping_plays_sound", this.server.reworked_sweeping_plays_sound);
            this.server.reworked_sweeping_emits_particles = sc.getBoolean("reworked_sweeping_emits_particles", this.server.reworked_sweeping_emits_particles);
            this.server.reworked_sweeping_sound_and_particles_only_for_swords = sc.getBoolean("reworked_sweeping_sound_and_particles_only_for_swords", this.server.reworked_sweeping_sound_and_particles_only_for_swords);
            this.server.allow_attacking_thru_walls = sc.getBoolean("allow_attacking_thru_walls", this.server.allow_attacking_thru_walls);
            this.server.movement_speed_while_attacking = (float) sc.getDouble("movement_speed_while_attacking", this.server.movement_speed_while_attacking);
            this.server.movement_speed_applied_smoothly = sc.getBoolean("movement_speed_applied_smoothly", this.server.movement_speed_applied_smoothly);
            this.server.movement_speed_effected_while_mounting = sc.getBoolean("movement_speed_effected_while_mounting", this.server.movement_speed_effected_while_mounting);
            this.server.knockback_reduced_for_fast_attacks = sc.getBoolean("knockback_reduced_for_fast_attacks", this.server.knockback_reduced_for_fast_attacks);
            this.server.knockback_reduction_threshold = (float) sc.getDouble("knockback_reduction_threshold", this.server.knockback_reduction_threshold);
            this.server.knockback_reduction_curve = getEnum(sc, "knockback_reduction_curve", this.server.knockback_reduction_curve, ServerConfig.Curve.class);
            this.server.combo_reset_rate = (float) sc.getDouble("combo_reset_rate", this.server.combo_reset_rate);
            this.server.target_search_range_multiplier = (float) sc.getDouble("target_search_range_multiplier", this.server.target_search_range_multiplier);
            this.server.server_target_range_validation = sc.getBoolean("server_target_range_validation", this.server.server_target_range_validation);
            this.server.dual_wielding_attack_speed_multiplier = (float) sc.getDouble("dual_wielding_attack_speed_multiplier", this.server.dual_wielding_attack_speed_multiplier);
            this.server.dual_wielding_main_hand_damage_multiplier = (float) sc.getDouble("dual_wielding_main_hand_damage_multiplier", this.server.dual_wielding_main_hand_damage_multiplier);
            this.server.dual_wielding_off_hand_damage_multiplier = (float) sc.getDouble("dual_wielding_off_hand_damage_multiplier", this.server.dual_wielding_off_hand_damage_multiplier);
            this.server.player_relations = getRelationMap(sc, "player_relations", this.server.player_relations);
            this.server.player_relation_tags = getRelationMap(sc, "player_relation_tags", this.server.player_relation_tags);
            this.server.player_relation_to_self_and_pets = getEnum(sc, "player_relation_to_self_and_pets", this.server.player_relation_to_self_and_pets, TargetHelper.Relation.class);
            this.server.player_relation_to_teammates = getEnum(sc, "player_relation_to_teammates", this.server.player_relation_to_teammates, TargetHelper.Relation.class);
            this.server.player_relation_to_passives = getEnum(sc, "player_relation_to_passives", this.server.player_relation_to_passives, TargetHelper.Relation.class);
            this.server.player_relation_to_hostiles = getEnum(sc, "player_relation_to_hostiles", this.server.player_relation_to_hostiles, TargetHelper.Relation.class);
            this.server.player_relation_to_other = getEnum(sc, "player_relation_to_other", this.server.player_relation_to_other, TargetHelper.Relation.class);
            this.server.fallback_compatibility_enabled = sc.getBoolean("fallback_compatibility_enabled", this.server.fallback_compatibility_enabled);
            this.server.weapon_registry_logging = sc.getBoolean("weapon_registry_logging", this.server.weapon_registry_logging);
            this.server.weapon_registry_compression = sc.getBoolean("weapon_registry_compression", this.server.weapon_registry_compression);
            // Fallback
            var fc = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config/bettercombat/fallback.yml"));
            this.fallback.schema_version = fc.getInt("schema_version", this.fallback.schema_version);
            this.fallback.blacklist_item_id_regex = fc.getString("blacklist_item_id_regex", this.fallback.blacklist_item_id_regex);
            this.fallback.fallback_compatibility = getCompatibilitySpecifierArray(fc, "fallback_compatibility", this.fallback.fallback_compatibility);
            this.fallback.ranged_weapons = getCompatibilitySpecifierArray(fc, "ranged_weapons", this.fallback.ranged_weapons);
            // Сохраняем на всякий случай
            this.save(plugin);
        }  catch (Exception e) {
            BetterCombatMod.LOGGER.trace("Config loading failed", e);
        }
    }

    public void save(JavaPlugin plugin) {
        try {
            new File(plugin.getDataFolder(), "config/bettercombat").mkdirs();
            var sc = new YamlConfiguration();
            var fc = new YamlConfiguration();
            save(sc, fc);
            sc.save(new File(plugin.getDataFolder(), "config/bettercombat/server.yml"));
            fc.save(new File(plugin.getDataFolder(), "config/bettercombat/fallback.yml"));
        } catch (Exception e) {
            BetterCombatMod.LOGGER.trace("Config saving failed", e);
        }
    }

    public void save(YamlConfiguration sc, YamlConfiguration fc) {
        // Server
        sc.set("upswing_multiplier", (double) this.server.upswing_multiplier);
        sc.set("allow_fast_attacks", this.server.allow_fast_attacks);
        sc.set("allow_attacking_mount", this.server.allow_attacking_mount);
        sc.set("attack_interval_cap", this.server.attack_interval_cap);
        setStringArray(sc, "hostile_player_vehicles", this.server.hostile_player_vehicles);
        sc.set("allow_vanilla_sweeping", this.server.allow_vanilla_sweeping);
        sc.set("allow_reworked_sweeping", this.server.allow_reworked_sweeping);
        sc.set("reworked_sweeping_extra_target_count", this.server.reworked_sweeping_extra_target_count);
        sc.set("reworked_sweeping_maximum_damage_penalty", (double) this.server.reworked_sweeping_maximum_damage_penalty);
        sc.set("reworked_sweeping_plays_sound", this.server.reworked_sweeping_plays_sound);
        sc.set("reworked_sweeping_emits_particles", this.server.reworked_sweeping_emits_particles);
        sc.set("reworked_sweeping_sound_and_particles_only_for_swords", this.server.reworked_sweeping_sound_and_particles_only_for_swords);
        sc.set("allow_attacking_thru_walls", this.server.allow_attacking_thru_walls);
        sc.set("movement_speed_while_attacking", (double) this.server.movement_speed_while_attacking);
        sc.set("movement_speed_applied_smoothly", this.server.movement_speed_applied_smoothly);
        sc.set("movement_speed_effected_while_mounting", this.server.movement_speed_effected_while_mounting);
        sc.set("knockback_reduced_for_fast_attacks", this.server.knockback_reduced_for_fast_attacks);
        sc.set("knockback_reduction_threshold", (double) this.server.knockback_reduction_threshold);
        setEnum(sc, "knockback_reduction_curve", this.server.knockback_reduction_curve);
        sc.set("combo_reset_rate", (double) this.server.combo_reset_rate);
        sc.set("target_search_range_multiplier", (double) this.server.target_search_range_multiplier);
        sc.set("server_target_range_validation", this.server.server_target_range_validation);
        sc.set("dual_wielding_attack_speed_multiplier", (double) this.server.dual_wielding_attack_speed_multiplier);
        sc.set("dual_wielding_main_hand_damage_multiplier", (double) this.server.dual_wielding_main_hand_damage_multiplier);
        sc.set("dual_wielding_off_hand_damage_multiplier", (double) this.server.dual_wielding_off_hand_damage_multiplier);
        setRelationMap(sc, "player_relations", this.server.player_relations);
        setRelationMap(sc, "player_relation_tags", this.server.player_relation_tags);
        setEnum(sc, "player_relation_to_self_and_pets", this.server.player_relation_to_self_and_pets);
        setEnum(sc, "player_relation_to_teammates", this.server.player_relation_to_teammates);
        setEnum(sc, "player_relation_to_passives", this.server.player_relation_to_passives);
        setEnum(sc, "player_relation_to_hostiles", this.server.player_relation_to_hostiles);
        setEnum(sc, "player_relation_to_other", this.server.player_relation_to_other);
        sc.set("fallback_compatibility_enabled", this.server.fallback_compatibility_enabled);
        sc.set("weapon_registry_logging", this.server.weapon_registry_logging);
        sc.set("weapon_registry_compression", this.server.weapon_registry_compression);
        // Fallback
        fc.set("schema_version", this.fallback.schema_version);
        fc.set("blacklist_item_id_regex", this.fallback.blacklist_item_id_regex);
        setCompatibilitySpecifierArray(fc, "fallback_compatibility", this.fallback.fallback_compatibility);
        setCompatibilitySpecifierArray(fc, "ranged_weapons", this.fallback.ranged_weapons);
    }

    private static String[] getStringArray(YamlConfiguration config, String path, String[] def) {
        return config.contains(path) ? config.getStringList(path).toArray(String[]::new) : def;
    }

    private static void setStringArray(YamlConfiguration config, String path, String[] def) {
        config.set(path, Arrays.asList(def));
    }

    private static <T extends Enum<T>> T getEnum(YamlConfiguration config, String path, T def, Class<T> clazz) {
        if (!config.contains(path))
            return def;
        return getEnum(clazz, config.getString(path).toUpperCase());
    }

    private static <T extends Enum<T>> T getEnum(Class<T> clazz, String name) {
        for (var e : clazz.getEnumConstants()) {
            if (e.name().toUpperCase().equals(name)) {
                return e;
            }
        }
        throw new IllegalArgumentException("No such enum constant " + name);
    }

    private static <T extends Enum<T>> void setEnum(YamlConfiguration config, String path, T def) {
        config.set(path, def.name().toUpperCase());
    }

    private static LinkedHashMap<String, TargetHelper.Relation> getRelationMap(YamlConfiguration config, String path, LinkedHashMap<String, TargetHelper.Relation> def) {
        if (!config.contains(path))
            return def;
        var map = new LinkedHashMap<String, TargetHelper.Relation>();
        for (var pair : config.getMapList(path)) {
            var id = Objects.requireNonNull((String) pair.get("id"));
            var relation = Objects.requireNonNull((String) pair.get("relation"));
            map.put(id, getEnum(TargetHelper.Relation.class, relation));
        }
        return map;
    }

    private static void setRelationMap(YamlConfiguration config, String path, LinkedHashMap<String, TargetHelper.Relation> def) {
        var list = new ArrayList<Map<String, String>>();
        if (def != null) {
            for (var pair : def.entrySet()) {
                var map = new HashMap<String, String>();
                map.put("id", pair.getKey());
                map.put("relation", pair.getValue().name().toUpperCase());
                list.add(map);
            }
        }
        config.set(path, list);
    }

    private static FallbackConfig.CompatibilitySpecifier[] getCompatibilitySpecifierArray(YamlConfiguration config, String path, FallbackConfig.CompatibilitySpecifier[] def) {
        if (!config.contains(path))
            return def;
        var list = new ArrayList<FallbackConfig.CompatibilitySpecifier>();
        for (var pair : config.getMapList(path)) {
            var item_id_regex = Objects.requireNonNull((String) pair.get("item_id_regex"));
            var weapon_attributes = Objects.requireNonNull((String) pair.get("weapon_attributes"));
            list.add(new FallbackConfig.CompatibilitySpecifier(item_id_regex, weapon_attributes));
        }
        return list.toArray(FallbackConfig.CompatibilitySpecifier[]::new);
    }

    private static void setCompatibilitySpecifierArray(YamlConfiguration config, String path, FallbackConfig.CompatibilitySpecifier[] def) {
        var list = new ArrayList<Map<String, String>>();
        if (def != null) {
            for (var pair : def) {
                var map = new HashMap<String, String>();
                map.put("item_id_regex", pair.item_id_regex);
                map.put("weapon_attributes", pair.weapon_attributes);
                list.add(map);
            }
        }
        config.set(path, list);
    }
}
