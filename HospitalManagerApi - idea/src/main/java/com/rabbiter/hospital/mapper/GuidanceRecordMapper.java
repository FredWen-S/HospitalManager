package com.rabbiter.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rabbiter.hospital.pojo.GuidanceRecord;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface GuidanceRecordMapper extends BaseMapper<GuidanceRecord> {
    @Select("select recommended_department as name, count(*) as value from guidance_record group by recommended_department order by value desc limit 5")
    List<Map<String, Object>> countTopDepartments();

    @Select("select symptom_text as name, count(*) as value from guidance_record group by symptom_text order by value desc limit 5")
    List<Map<String, Object>> countTopSymptoms();
}
