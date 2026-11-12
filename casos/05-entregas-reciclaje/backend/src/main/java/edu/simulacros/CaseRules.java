package edu.simulacros;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;

final class CaseRules {
    private CaseRules() {}

    static String owner(int id) { return null; }

    static ObjectNode seed(ObjectMapper json, int id) {
        ObjectNode state = json.createObjectNode().put("id", id);
        state.put("name", "Punto de acopio " + id).put("zone", "Sector " + (1 + id % 3))
            .put("status", "OPEN");
        state.putArray("materials").addObject().put("id", 1).put("name", "Papel").put("active", true);
        state.withArray("materials").addObject().put("id", 2).put("name", "Vidrio").put("active", false);
        state.putArray("deliveries");
        return state;
    }

    static ObjectNode detail(ObjectNode state) {
        state.remove("deliveries");
        return state;
    }

    static ObjectNode summary(ObjectNode state) {
        return detail(state);
    }

    static ObjectNode apply(ObjectNode state, JsonNode input, String email) {
        ObjectNode material = find(state, "materials", integer(input, "materialId", 1, 2));
        JsonNode weight = input.path("weightKg");
        if (!weight.isNumber() || weight.decimalValue().signum() <= 0) throw Failure.invalid();
        if (!material.path("active").asBoolean()) {
            throw Failure.conflict("MATERIAL_SUSPENDED", "El punto dejó de recibir ese material.");
        }
        ArrayNode deliveries = state.withArray("deliveries");
        return deliveries.addObject().put("id", deliveries.size()).put("materialId", material.path("id").asInt())
            .put("material", material.path("name").asText()).put("weightKg", weight.decimalValue());
    }

    private static int integer(JsonNode input, String field, int min, int max) {
        JsonNode value = input.path(field);
        if (!value.isIntegralNumber() || !value.canConvertToInt()
            || value.intValue() < min || value.intValue() > max) throw Failure.invalid();
        return value.intValue();
    }

    private static ObjectNode find(ObjectNode state, String field, int id) {
        for (JsonNode option : state.path(field)) {
            if (option.path("id").asInt() == id) return (ObjectNode) option;
        }
        throw Failure.invalid();
    }
}
