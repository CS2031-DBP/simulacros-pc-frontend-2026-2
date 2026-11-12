package edu.simulacros;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;

final class CaseRules {
    private CaseRules() {}

    static String owner(int id) { return null; }

    static ObjectNode seed(ObjectMapper json, int id) {
        ObjectNode state = json.createObjectNode().put("id", id);
        state.put("name", "Sala " + id).put("campus", id % 2 == 0 ? "Norte" : "Central")
            .put("capacity", 6).put("availableSlots", 2);
        state.putArray("slots").addObject().put("id", 1).put("startsAt", "09:00").put("available", true);
        state.withArray("slots").addObject().put("id", 2).put("startsAt", "11:00").put("available", true);
        return state;
    }

    static ObjectNode detail(ObjectNode state) {
        return state;
    }

    static ObjectNode summary(ObjectNode state) {
        state.remove("slots");
        return detail(state);
    }

    static ObjectNode apply(ObjectNode state, JsonNode input, String email) {
        ObjectNode slot = find(state, "slots", integer(input, "slotId", 1, 2));
        int attendees = integer(input, "attendees", 1, state.path("capacity").asInt());
        if (!slot.path("available").asBoolean()) {
            throw Failure.conflict("SLOT_TAKEN", "El turno ya no está disponible.");
        }
        slot.put("available", false);
        state.put("availableSlots", state.path("availableSlots").asInt() - 1);
        return state.objectNode().put("slotId", slot.path("id").asInt()).put("attendees", attendees);
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
