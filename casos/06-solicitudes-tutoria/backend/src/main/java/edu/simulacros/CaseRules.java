package edu.simulacros;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;

final class CaseRules {
    private CaseRules() {}

    static String owner(int id) { return id <= 11 ? Lab.STUDENT : Lab.PARTNER; }

    static ObjectNode seed(ObjectMapper json, int id) {
        ObjectNode state = json.createObjectNode().put("id", id);
        state.put("course", "Cálculo " + id).put("topic", "Repaso de integrales")
            .put("status", id == 2 ? "CONFIRMED" : "PENDING")
            .put("preferredSlotId", 1).put("preferredTime", "09:00");
        state.putArray("availableSlots").addObject().put("id", 1).put("startsAt", "09:00");
        state.withArray("availableSlots").addObject().put("id", 2).put("startsAt", "15:00");
        return state;
    }

    static ObjectNode detail(ObjectNode state) {
        return state;
    }

    static ObjectNode summary(ObjectNode state) {
        state.remove("availableSlots");
        return detail(state);
    }

    static ObjectNode apply(ObjectNode state, JsonNode input, String email) {
        String topic = text(input, "topic", 5, 100);
        int slotId = integer(input, "preferredSlotId", 1, 2);
        ObjectNode slot = find(state, "availableSlots", slotId);
        if (state.path("status").asText().equals("CONFIRMED")) {
            throw Failure.conflict("REQUEST_CONFIRMED", "La solicitud ya fue confirmada y no admite edición.");
        }
        return state.put("topic", topic).put("preferredSlotId", slotId)
            .put("preferredTime", slot.path("startsAt").asText());
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

    private static String text(JsonNode input, String field, int min, int max) {
        if (!input.path(field).isTextual()) throw Failure.invalid();
        String value = input.path(field).asText().strip();
        if (value.length() < min || value.length() > max) throw Failure.invalid();
        return value;
    }
}
