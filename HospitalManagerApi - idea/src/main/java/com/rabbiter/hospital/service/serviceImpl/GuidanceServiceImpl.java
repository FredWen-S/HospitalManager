package com.rabbiter.hospital.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbiter.hospital.dto.GuidanceRequest;
import com.rabbiter.hospital.dto.GuidanceResult;
import com.rabbiter.hospital.mapper.DoctorMapper;
import com.rabbiter.hospital.mapper.GuidanceRecordMapper;
import com.rabbiter.hospital.mapper.GuidanceRuleMapper;
import com.rabbiter.hospital.pojo.Doctor;
import com.rabbiter.hospital.pojo.GuidanceRecord;
import com.rabbiter.hospital.pojo.GuidanceRule;
import com.rabbiter.hospital.service.GuidanceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("GuidanceService")
public class GuidanceServiceImpl implements GuidanceService {
    @Resource
    private GuidanceRuleMapper guidanceRuleMapper;
    @Resource
    private GuidanceRecordMapper guidanceRecordMapper;
    @Resource
    private DoctorMapper doctorMapper;

    @Override
    public HashMap<String, Object> analyze(GuidanceRequest request) {
        String symptomText = request == null ? "" : request.getSymptomText();
        String cleanedText = clean(symptomText);
        List<GuidanceRule> rules = guidanceRuleMapper.selectList(
                new QueryWrapper<GuidanceRule>().eq("enabled", 1).orderByDesc("priority")
        );
        List<GuidanceResult> results = new ArrayList<>();
        for (GuidanceRule rule : rules) {
            GuidanceResult result = matchRule(cleanedText, rule);
            if (result.getScore() != null && result.getScore() > 0) {
                result.setDoctors(findDoctors(result.getRecommendedDepartment()));
                results.add(result);
            }
        }
        Collections.sort(results, new Comparator<GuidanceResult>() {
            @Override
            public int compare(GuidanceResult o1, GuidanceResult o2) {
                return o2.getScore().compareTo(o1.getScore());
            }
        });
        if (results.size() > 3) {
            results = new ArrayList<>(results.subList(0, 3));
        }
        if (results.isEmpty()) {
            GuidanceResult defaultResult = defaultResult();
            defaultResult.setDoctors(findDoctors(defaultResult.getRecommendedDepartment()));
            results.add(defaultResult);
        }

        GuidanceResult topResult = results.get(0);
        saveRecord(request == null ? null : request.getPatientId(), symptomText, topResult);

        HashMap<String, Object> map = new HashMap<>();
        map.put("topResult", topResult);
        map.put("results", results);
        return map;
    }

    @Override
    public HashMap<String, Object> history(Integer patientId) {
        QueryWrapper<GuidanceRecord> wrapper = new QueryWrapper<>();
        if (patientId != null) {
            wrapper.eq("patient_id", patientId);
        }
        wrapper.orderByDesc("create_time");
        HashMap<String, Object> map = new HashMap<>();
        map.put("records", guidanceRecordMapper.selectList(wrapper));
        return map;
    }

    @Override
    public HashMap<String, Object> rules(String query) {
        QueryWrapper<GuidanceRule> wrapper = new QueryWrapper<>();
        if (query != null && query.trim().length() > 0) {
            wrapper.like("department", query.trim())
                    .or().like("disease_name", query.trim())
                    .or().like("keywords", query.trim());
        }
        wrapper.orderByDesc("enabled").orderByDesc("priority").orderByDesc("update_time");
        HashMap<String, Object> map = new HashMap<>();
        map.put("rules", guidanceRuleMapper.selectList(wrapper));
        return map;
    }

    @Override
    public Boolean addRule(GuidanceRule rule) {
        Date now = new Date();
        if (rule.getEnabled() == null) {
            rule.setEnabled(1);
        }
        if (rule.getPriority() == null) {
            rule.setPriority(0);
        }
        rule.setCreateTime(now);
        rule.setUpdateTime(now);
        return guidanceRuleMapper.insert(rule) > 0;
    }

    @Override
    public Boolean updateRule(Integer id, GuidanceRule rule) {
        rule.setId(id);
        rule.setUpdateTime(new Date());
        return guidanceRuleMapper.updateById(rule) > 0;
    }

    @Override
    public Boolean disableRule(Integer id) {
        GuidanceRule rule = new GuidanceRule();
        rule.setId(id);
        rule.setEnabled(0);
        rule.setUpdateTime(new Date());
        return guidanceRuleMapper.updateById(rule) > 0;
    }

    @Override
    public HashMap<String, Object> statistics() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", guidanceRecordMapper.selectCount(null));
        map.put("topDepartments", guidanceRecordMapper.countTopDepartments());
        map.put("topSymptoms", guidanceRecordMapper.countTopSymptoms());
        map.put("recentRecords", guidanceRecordMapper.selectList(
                new QueryWrapper<GuidanceRecord>().orderByDesc("create_time").last("limit 10")
        ));
        return map;
    }

    private GuidanceResult matchRule(String cleanedText, GuidanceRule rule) {
        GuidanceResult result = new GuidanceResult();
        result.setRecommendedDepartment(rule.getDepartment());
        result.setDiseaseName(rule.getDiseaseName());
        result.setAdvice(rule.getAdvice());
        result.setLocation(rule.getLocation());
        result.setRouteTip(rule.getRouteTip());

        int score = 0;
        Set<String> matched = new HashSet<>();
        score += scoreTerms(cleanedText, rule.getKeywords(), 3, matched);
        score += scoreTerms(cleanedText, rule.getSynonyms(), 2, matched);
        if (contains(cleanedText, rule.getDepartment())) {
            score += 2;
            matched.add(rule.getDepartment());
        }
        if (contains(cleanedText, rule.getDiseaseName())) {
            score += 2;
            matched.add(rule.getDiseaseName());
        }
        Integer priority = rule.getPriority();
        if (score > 0 && priority != null && priority > 0) {
            score += Math.min(priority, 10) / 5;
        }
        result.setMatchedKeywords(new ArrayList<>(matched));
        result.setScore(score);
        return result;
    }

    private int scoreTerms(String text, String terms, int scorePerHit, Set<String> matched) {
        int score = 0;
        if (terms == null || terms.trim().length() == 0) {
            return score;
        }
        String normalized = terms.replace("，", ",");
        String[] array = normalized.split(",");
        for (String item : array) {
            String term = clean(item);
            if (term.length() == 0) {
                continue;
            }
            if (text.contains(term)) {
                score += scorePerHit;
                matched.add(item.trim());
            }
        }
        return score;
    }

    private String clean(String text) {
        if (text == null) {
            return "";
        }
        return text.trim()
                .toLowerCase()
                .replaceAll("[\\s,，。.!！?？;；:：、\\-_/\\\\()（）\\[\\]【】\"'“”‘’]", "");
    }

    private boolean contains(String text, String keyword) {
        return keyword != null && keyword.trim().length() > 0 && text.contains(clean(keyword));
    }

    private GuidanceResult defaultResult() {
        GuidanceResult result = new GuidanceResult();
        result.setRecommendedDepartment("全科门诊");
        result.setDiseaseName("症状待明确");
        result.setAdvice("暂未匹配到明确专科，建议前往全科门诊或普通内科进一步咨询。如出现胸痛、呼吸困难、意识不清等急症，请立即前往急诊。");
        result.setLocation("门诊楼 1 层 咨询台旁");
        result.setRouteTip("从一楼大厅进入后，前往导诊台右侧全科门诊候诊区。");
        result.setMatchedKeywords(new ArrayList<String>());
        result.setScore(0);
        return result;
    }

    private List<Doctor> findDoctors(String department) {
        return doctorMapper.selectList(new QueryWrapper<Doctor>()
                .select("d_id", "d_name", "d_gender", "d_post", "d_section", "d_introduction", "d_price", "d_avg_star")
                .eq("d_state", 1)
                .eq("d_section", department));
    }

    private void saveRecord(Integer patientId, String symptomText, GuidanceResult topResult) {
        GuidanceRecord record = new GuidanceRecord();
        record.setPatientId(patientId);
        record.setSymptomText(symptomText);
        record.setRecommendedDepartment(topResult.getRecommendedDepartment());
        record.setDiseaseName(topResult.getDiseaseName());
        record.setMatchedKeywords(join(topResult.getMatchedKeywords()));
        record.setScore(topResult.getScore());
        record.setAdvice(topResult.getAdvice());
        record.setCreateTime(new Date());
        guidanceRecordMapper.insert(record);
    }

    private String join(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (String item : list) {
            if (builder.length() > 0) {
                builder.append(",");
            }
            builder.append(item);
        }
        return builder.toString();
    }
}
