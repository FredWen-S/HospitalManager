package com.rabbiter.hospital.service;

import com.rabbiter.hospital.pojo.Feedback;

import java.util.HashMap;

public interface FeedbackService {
    Boolean submit(Feedback feedback);

    HashMap<String, Object> list(String status, String type, String query);

    Boolean reply(Integer id, Feedback feedback);

    Boolean updateStatus(Integer id, Feedback feedback);
}
