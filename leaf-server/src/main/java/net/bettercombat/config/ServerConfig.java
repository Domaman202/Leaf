package net.bettercombat.config;

import net.bettercombat.logic.TargetHelper;

import java.util.LinkedHashMap;

public class ServerConfig { // todo: normal config class
    public float upswing_multiplier = 0.5F;
    public boolean allow_fast_attacks = true;
    public boolean allow_attacking_mount = false;
    public int attack_interval_cap = 2;
    public String[] hostile_player_vehicles = {"alexsmobs:crocodile"};
    public boolean allow_vanilla_sweeping = false;
    public boolean allow_reworked_sweeping = true;
    public int reworked_sweeping_extra_target_count = 4;
    public float reworked_sweeping_maximum_damage_penalty = 0.5F;
    public boolean reworked_sweeping_plays_sound = true;
    public boolean reworked_sweeping_emits_particles = true;
    public boolean reworked_sweeping_sound_and_particles_only_for_swords = true;
    public boolean allow_attacking_thru_walls = false;
    public float movement_speed_while_attacking = 0.5F;
    public boolean movement_speed_applied_smoothly = true;
    public boolean movement_speed_effected_while_mounting = false;
    public boolean knockback_reduced_for_fast_attacks = true;
    public float knockback_reduction_threshold = 12.5F;
    public Curve knockback_reduction_curve = Curve.HALF_SQUARE;
    public enum Curve { LINEAR, SQUARE, HALF_SQUARE }
    public float combo_reset_rate = 3F;
    public float target_search_range_multiplier = 2F;
    public boolean server_target_range_validation = false;
    public float dual_wielding_attack_speed_multiplier = 1.2F;
    public float dual_wielding_main_hand_damage_multiplier = 1F;
    public float dual_wielding_off_hand_damage_multiplier = 1F;
    public LinkedHashMap<String, TargetHelper.Relation> player_relations = new LinkedHashMap<>() {{
        put("minecraft:player", TargetHelper.Relation.NEUTRAL);
        put("minecraft:villager", TargetHelper.Relation.NEUTRAL);
        put("minecraft:iron_golem", TargetHelper.Relation.NEUTRAL);
        put("guardvillagers:guard", TargetHelper.Relation.NEUTRAL);
    }};
    public LinkedHashMap<String, TargetHelper.Relation> player_relation_tags = new LinkedHashMap<>() {{
        put("minecraft:undead", TargetHelper.Relation.HOSTILE);
    }};
    public TargetHelper.Relation player_relation_to_self_and_pets = TargetHelper.Relation.NEUTRAL;
    public TargetHelper.Relation player_relation_to_teammates = TargetHelper.Relation.NEUTRAL;
    public TargetHelper.Relation player_relation_to_passives = TargetHelper.Relation.HOSTILE;
    public TargetHelper.Relation player_relation_to_hostiles = TargetHelper.Relation.HOSTILE;
    public TargetHelper.Relation player_relation_to_other = TargetHelper.Relation.HOSTILE;
    public boolean fallback_compatibility_enabled = true;
    public boolean weapon_registry_logging = false;
    public boolean weapon_registry_compression = true;

    public float getUpswingMultiplier() {
        return Math.max(0.2F, Math.min(1, upswing_multiplier));
    }
}
