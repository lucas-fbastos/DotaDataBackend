package io.luck.AghsLabs.providers.opendota.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record OpenDotaHero(
        Long id,

        String name,

        @JsonProperty("primary_attr")
        String primaryAttribute,

        @JsonProperty("attack_type")
        String attackType,

        List<String> roles,

        String img,

        String icon,

        @JsonProperty("base_health")
        Integer baseHealth,

        @JsonProperty("base_health_regen")
        Double baseHealthRegen,

        @JsonProperty("base_mana")
        Integer baseMana,

        @JsonProperty("base_mana_regen")
        Double baseManaRegen,

        @JsonProperty("base_armor")
        Double baseArmor,

        @JsonProperty("base_mr")
        Double baseMagicResistance,

        @JsonProperty("base_attack_min")
        Integer baseAttackMin,

        @JsonProperty("base_attack_max")
        Integer baseAttackMax,

        @JsonProperty("base_str")
        Integer baseStrength,

        @JsonProperty("base_agi")
        Integer baseAgility,

        @JsonProperty("base_int")
        Integer baseIntelligence,

        @JsonProperty("str_gain")
        Double strengthGain,

        @JsonProperty("agi_gain")
        Double agilityGain,

        @JsonProperty("int_gain")
        Double intelligenceGain,

        @JsonProperty("attack_range")
        Integer attackRange,

        @JsonProperty("projectile_speed")
        Integer projectileSpeed,

        @JsonProperty("attack_rate")
        Double attackRate,

        @JsonProperty("base_attack_time")
        Integer baseAttackTime,

        @JsonProperty("attack_point")
        Double attackPoint,

        @JsonProperty("move_speed")
        Integer moveSpeed,

        @JsonProperty("turn_rate")
        Double turnRate,

        @JsonProperty("cm_enabled")
        Boolean captainModeEnabled,

        Integer legs,

        @JsonProperty("day_vision")
        Integer dayVision,

        @JsonProperty("night_vision")
        Integer nightVision,

        @JsonProperty("localized_name")
        String localizedName
) {
}
