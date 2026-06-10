import request from "@/utils/request.js";

export function submitFeedback(data) {
  return request.post("/feedback/submit", data);
}

export function getFeedbackList(params) {
  return request.get("/feedback/list", { params });
}

export function replyFeedback(id, data) {
  return request.put("/feedback/" + id + "/reply", data);
}

export function updateFeedbackStatus(id, data) {
  return request.put("/feedback/" + id + "/status", data);
}
