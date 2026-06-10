<template>
  <div>
    <el-row :gutter="16">
      <el-col :span="8">
        <el-card>
          <div class="metric-label">导诊总次数</div>
          <div class="metric-value">{{ total }}</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div class="metric-label">热门推荐科室 Top 5</div>
          <div v-for="item in topDepartments" :key="item.name" class="rank-row">
            <span>{{ item.name }}</span>
            <b>{{ item.value }}</b>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div class="metric-label">热门症状 Top 5</div>
          <div v-for="item in topSymptoms" :key="item.name" class="rank-row">
            <span>{{ item.name }}</span>
            <b>{{ item.value }}</b>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-card class="recent-card">
      <div slot="header">最近导诊记录</div>
      <el-table :data="recentRecords" border stripe>
        <el-table-column prop="createTime" label="时间" width="170"></el-table-column>
        <el-table-column prop="patientId" label="患者ID" width="90"></el-table-column>
        <el-table-column prop="symptomText" label="症状描述"></el-table-column>
        <el-table-column prop="recommendedDepartment" label="推荐科室" width="130"></el-table-column>
        <el-table-column prop="score" label="分数" width="70"></el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { getGuidanceStatistics } from "@/api/guidance.js";

export default {
  name: "GuidanceStatistics",
  data() {
    return {
      total: 0,
      topDepartments: [],
      topSymptoms: [],
      recentRecords: []
    };
  },
  methods: {
    loadStatistics() {
      getGuidanceStatistics().then((res) => {
        if (res.data.status === 200) {
          const data = res.data.data || {};
          this.total = data.total || 0;
          this.topDepartments = data.topDepartments || [];
          this.topSymptoms = data.topSymptoms || [];
          this.recentRecords = data.recentRecords || [];
        }
      });
    }
  },
  created() {
    this.loadStatistics();
  }
};
</script>

<style scoped>
.metric-label {
  color: #606266;
  margin-bottom: 12px;
}
.metric-value {
  font-size: 36px;
  font-weight: 600;
  color: #1f6fca;
}
.rank-row {
  display: flex;
  justify-content: space-between;
  line-height: 28px;
}
.recent-card {
  margin-top: 16px;
}
</style>
