package edu.simulacros;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;

final class CaseRules {
    private CaseRules() {}

    static String owner(int id) { return null; }

    static ObjectNode seed(ObjectMapper json, int id) {
        ObjectNode state = json.createObjectNode().put("id", id);
        state.put("name", id == 1 ? "Ajedrez" : "Juego " + id)
            .put("category", "Estrategia").put("recommendedPlayers", 2).put("availableUnits", id == 1 ? 1 : 3);
        return state;
    }

    static ObjectNode detail(ObjectNode state) {
        return state;
    }

    static ObjectNode summary(ObjectNode state) {
        return detail(state);
    }

    static ObjectNode apply(ObjectNode state, JsonNode input, String email) {
        int days = integer(input, "days", 1, 3);
        int units = state.path("availableUnits").asInt();
        if (units == 0) throw Failure.conflict("OUT_OF_STOCK", "El juego se quedó sin unidades disponibles.");
        state.put("availableUnits", units - 1);
        return state.objectNode().put("gameId", state.path("id").asLong()).put("days", days)
            .put("availableUnits", units - 1);
    }

    private static int integer(JsonNode input, String field, int min, int max) {
        JsonNode value = input.path(field);
        if (!value.isIntegralNumber() || !value.canConvertToInt()
            || value.intValue() < min || value.intValue() > max) throw Failure.invalid();
        return value.intValue();
    }
}
