package io.luck.DotaTrainerBackend.providers.opendota.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.DatabindException;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

@Builder
public record OpenDotaItem(
        List<Ability> abilities,

        List<String> hint,

        Long id,

        String img,

        @JsonProperty("dname")
        String displayName,

        @JsonProperty("qual")
        String quality,

        Integer cost,

        @JsonDeserialize(using = StringListDeserializer.class)
        List<String> behavior,

        @JsonProperty("dmg_type")
        String damageType,

        @JsonProperty("target_team")
        @JsonDeserialize(using = StringListDeserializer.class)
        List<String> targetTeam,

        @JsonProperty("target_type")
        @JsonDeserialize(using = StringListDeserializer.class)
        List<String> targetType,

        String notes,

        @JsonProperty("attrib")
        List<Attribute> attributes,

        @JsonProperty("mc")
        @JsonDeserialize(using = FalseAsNullIntegerDeserializer.class)
        Integer manaCost,

        @JsonProperty("hc")
        @JsonDeserialize(using = FalseAsNullIntegerDeserializer.class)
        Integer healthCost,

        @JsonProperty("cd")
        @JsonDeserialize(using = FalseAsNullIntegerDeserializer.class)
        Integer cooldown,

        String lore,

        List<String> components,

        Boolean created,

        @JsonDeserialize(using = FalseAsNullIntegerDeserializer.class)
        Integer charges,

        @JsonProperty("dispellable")
        String dispellable,

        @JsonProperty("bkbpierce")
        String blackKingBarPierce,

        Integer tier
) {
    @Builder
    public record Ability(
            String type,
            String title,
            String description
    ) {
    }

    @Builder
    public record Attribute(
            String key,
            String header,
            String display,
            String value,
            String footer
    ) {
    }

    public static class StringListDeserializer extends ValueDeserializer<List<String>> {
        @Override
        public List<String> deserialize(JsonParser parser, DeserializationContext context) throws JacksonException {
            JsonToken token = parser.currentToken();
            if (token == JsonToken.VALUE_FALSE) {
                return List.of();
            }
            if (token == JsonToken.VALUE_STRING) {
                return List.of(parser.getString());
            }
            if (token == JsonToken.START_ARRAY) {
                List<String> values = new ArrayList<>();
                while (parser.nextToken() != JsonToken.END_ARRAY) {
                    if (parser.currentToken() != JsonToken.VALUE_STRING) {
                        throw DatabindException.from(parser, "Expected an array of strings.");
                    }
                    values.add(parser.getString());
                }
                return List.copyOf(values);
            }

            throw DatabindException.from(parser, "Expected a string, an array of strings, or false.");
        }
    }

    public static class FalseAsNullIntegerDeserializer extends ValueDeserializer<Integer> {
        @Override
        public Integer deserialize(JsonParser parser, DeserializationContext context) throws JacksonException {
            JsonToken token = parser.currentToken();
            if (token == JsonToken.VALUE_FALSE) {
                return null;
            }
            if (token == JsonToken.VALUE_NUMBER_INT) {
                return parser.getIntValue();
            }

            throw DatabindException.from(parser, "Expected an integer or false.");
        }
    }
}
