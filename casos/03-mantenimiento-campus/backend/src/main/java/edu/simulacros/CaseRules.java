package edu.simulacros;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;

final class CaseRules {
    private CaseRules() {}

    static String owner(int id) { return null; }

    static ObjectNode seed(ObjectMapper json, int id) {
        ObjectNode state = json.createObjectNode().put("id", id);
        state.put("name", "Aula " + id).put("building", "Edificio A").put("floor", 1 + id % 4)
            .put("openReports", 0).put("acceptsReports", id != 2);
        state.putArray("reports");
        return state;
    }

    static ObjectNode detail(ObjectNode state) {
        return state;
    }

    static ObjectNode summary(ObjectNode state) {
        state.remove("reports");
        return detail(state);
    }

    static ObjectNode apply(ObjectNode state, JsonNode input, String email) {
        String category = choice(input, "category", "LIGHTING", "FURNITURE", "CLEANING");
        String description = text(input, "description", 10, 200);
        if (!state.path("acceptsReports").asBoolean()) {
            throw Failure.conflict("LOCATION_CLOSED", "El espacio está cerrado y no admite reportes.");
        }
        ArrayNode reports = state.withArray("reports");
        ObjectNode report = reports.addObject().put("id", reports.size())
            .put("category", category).put("description", description);
        state.put("openReports", reports.size());
        return report;
    }

    private static String choice(JsonNode input, String field, String... choices) {
        String value = input.path(field).asText();
        if (!java.util.List.of(choices).contains(value)) throw Failure.invalid();
        return value;
    }

    private static String text(JsonNode input, String field, int min, int max) {
        if (!input.path(field).isTextual()) throw Failure.invalid();
        String value = input.path(field).asText().strip();
        if (value.length() < min || value.length() > max) throw Failure.invalid();
        return value;
    }
}
