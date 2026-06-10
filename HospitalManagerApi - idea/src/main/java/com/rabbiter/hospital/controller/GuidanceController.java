package com.rabbiter.hospital.controller;

import com.rabbiter.hospital.dto.GuidanceRequest;
import com.rabbiter.hospital.pojo.GuidanceRule;
import com.rabbiter.hospital.service.GuidanceService;
import com.rabbiter.hospital.utils.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("guidance")
public class GuidanceController {
    @Autowired
    private GuidanceService guidanceService;

    @PostMapping("analyze")
    @ResponseBody
    public ResponseData analyze(@RequestBody GuidanceRequest request) {
        if (request == null || request.getSymptomText() == null || request.getSymptomText().trim().length() == 0) {
            return ResponseData.fail("请输入症状描述");
        }
        return ResponseData.success("智能导诊分析完成", guidanceService.analyze(request));
    }

    @GetMapping("history")
    public ResponseData history(Integer patientId) {
        return ResponseData.success("查询导诊历史成功", guidanceService.history(patientId));
    }

    @GetMapping("rules")
    public ResponseData rules(String query) {
        return ResponseData.success("查询导诊规则成功", guidanceService.rules(query));
    }

    @PostMapping("rule")
    @ResponseBody
    public ResponseData addRule(@RequestBody GuidanceRule rule) {
        if (guidanceService.addRule(rule)) {
            return ResponseData.success("新增导诊规则成功");
        }
        return ResponseData.fail("新增导诊规则失败");
    }

    @PutMapping("rule/{id}")
    @ResponseBody
    public ResponseData updateRule(@PathVariable Integer id, @RequestBody GuidanceRule rule) {
        if (guidanceService.updateRule(id, rule)) {
            return ResponseData.success("修改导诊规则成功");
        }
        return ResponseData.fail("修改导诊规则失败");
    }

    @DeleteMapping("rule/{id}")
    public ResponseData disableRule(@PathVariable Integer id) {
        if (guidanceService.disableRule(id)) {
            return ResponseData.success("禁用导诊规则成功");
        }
        return ResponseData.fail("禁用导诊规则失败");
    }

    @GetMapping("statistics")
    public ResponseData statistics() {
        return ResponseData.success("查询导诊统计成功", guidanceService.statistics());
    }

    @GetMapping("navigation")
    public ResponseData navigation(String department) {
        return ResponseData.success("查询科室导航成功", guidanceService.navigation(department));
    }
}
