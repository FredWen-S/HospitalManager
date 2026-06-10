package com.rabbiter.hospital.service;

import com.rabbiter.hospital.dto.GuidanceRequest;
import com.rabbiter.hospital.pojo.GuidanceRule;

import java.util.HashMap;

public interface GuidanceService {
    HashMap<String, Object> analyze(GuidanceRequest request);

    HashMap<String, Object> history(Integer patientId);

    HashMap<String, Object> rules(String query);

    Boolean addRule(GuidanceRule rule);

    Boolean updateRule(Integer id, GuidanceRule rule);

    Boolean disableRule(Integer id);

    HashMap<String, Object> statistics();
}
