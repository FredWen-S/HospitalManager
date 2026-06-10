import request from "@/utils/request.js";

export function analyzeGuidance(data) {
  return request.post("/guidance/analyze", data);
}

export function getGuidanceHistory(patientId) {
  return request.get("/guidance/history", {
    params: { patientId }
  });
}

export function getGuidanceRules(query) {
  return request.get("/guidance/rules", {
    params: { query }
  });
}

export function createGuidanceRule(data) {
  return request.post("/guidance/rule", data);
}

export function updateGuidanceRule(id, data) {
  return request.put("/guidance/rule/" + id, data);
}

export function disableGuidanceRule(id) {
  return request.delete("/guidance/rule/" + id);
}

export function getGuidanceStatistics() {
  return request.get("/guidance/statistics");
}
