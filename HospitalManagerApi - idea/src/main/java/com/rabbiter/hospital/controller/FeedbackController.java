package com.rabbiter.hospital.controller;

import com.rabbiter.hospital.pojo.Feedback;
import com.rabbiter.hospital.service.FeedbackService;
import com.rabbiter.hospital.utils.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("feedback")
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("submit")
    @ResponseBody
    public ResponseData submit(@RequestBody Feedback feedback) {
        if (feedback == null || feedback.getContent() == null || feedback.getContent().trim().length() == 0) {
            return ResponseData.fail("反馈内容不能为空");
        }
        if (feedbackService.submit(feedback)) {
            return ResponseData.success("提交反馈成功");
        }
        return ResponseData.fail("提交反馈失败");
    }

    @GetMapping("list")
    public ResponseData list(String status, String type, String query) {
        return ResponseData.success("查询反馈列表成功", feedbackService.list(status, type, query));
    }

    @PutMapping("{id}/reply")
    @ResponseBody
    public ResponseData reply(@PathVariable Integer id, @RequestBody(required = false) Feedback feedback) {
        if (feedbackService.reply(id, feedback)) {
            return ResponseData.success("回复反馈成功");
        }
        return ResponseData.fail("回复反馈失败");
    }

    @PutMapping("{id}/status")
    @ResponseBody
    public ResponseData updateStatus(@PathVariable Integer id, @RequestBody Feedback feedback) {
        if (feedbackService.updateStatus(id, feedback)) {
            return ResponseData.success("修改反馈状态成功");
        }
        return ResponseData.fail("修改反馈状态失败");
    }
}
