package com.rabbiter.hospital.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbiter.hospital.mapper.FeedbackMapper;
import com.rabbiter.hospital.pojo.Feedback;
import com.rabbiter.hospital.service.FeedbackService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;

@Service("FeedbackService")
public class FeedbackServiceImpl implements FeedbackService {
    @Resource
    private FeedbackMapper feedbackMapper;

    @Override
    public Boolean submit(Feedback feedback) {
        Date now = new Date();
        if (!hasText(feedback.getFeedbackType())) {
            feedback.setFeedbackType("其他");
        }
        feedback.setStatus("待处理");
        feedback.setCreateTime(now);
        feedback.setUpdateTime(now);
        return feedbackMapper.insert(feedback) > 0;
    }

    @Override
    public HashMap<String, Object> list(String status, String type, String query) {
        QueryWrapper<Feedback> wrapper = new QueryWrapper<>();
        if (hasText(status)) {
            wrapper.eq("status", status.trim());
        }
        if (hasText(type)) {
            wrapper.eq("feedback_type", type.trim());
        }
        if (hasText(query)) {
            String keyword = query.trim();
            wrapper.and(item -> item.like("content", keyword)
                    .or().like("contact", keyword)
                    .or().like("reply", keyword));
        }
        wrapper.orderByAsc("status").orderByDesc("create_time");
        HashMap<String, Object> map = new HashMap<>();
        map.put("feedbacks", feedbackMapper.selectList(wrapper));
        return map;
    }

    @Override
    public Boolean reply(Integer id, Feedback feedback) {
        Feedback update = new Feedback();
        update.setId(id);
        update.setReply(feedback == null ? "" : feedback.getReply());
        update.setStatus("已处理");
        update.setUpdateTime(new Date());
        return feedbackMapper.updateById(update) > 0;
    }

    @Override
    public Boolean updateStatus(Integer id, Feedback feedback) {
        Feedback update = new Feedback();
        update.setId(id);
        update.setStatus(feedback != null && hasText(feedback.getStatus()) ? feedback.getStatus().trim() : "待处理");
        update.setUpdateTime(new Date());
        return feedbackMapper.updateById(update) > 0;
    }

    private boolean hasText(String text) {
        return text != null && text.trim().length() > 0;
    }
}
