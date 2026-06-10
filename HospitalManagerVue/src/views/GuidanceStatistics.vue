<template>
  <div>
    <el-alert
      class="statistics-tip"
      title="导诊统计仅供运营和答辩展示参考，不能作为医疗诊断依据。"
      type="info"
      show-icon
      :closable="false"
    ></el-alert>
    <el-row :gutter="16">
      <el-col :span="6">
        <el-card>
          <div class="metric-label">导诊总次数</div>
          <div class="metric-value">{{ total }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="metric-label">已启用规则数</div>
          <div class="metric-value">{{ enabledRuleCount }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="metric-label">禁用规则数</div>
          <div class="metric-value muted">{{ disabledRuleCount }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="metric-label">覆盖科室数</div>
          <div class="metric-value">{{ coveredDepartmentCount }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="rank-cards">
      <el-col :span="8">
        <el-card>
          <div class="metric-label">热门推荐科室 Top 5</div>
          <div v-for="item in topDepartments" :key="item.name" class="rank-row">
            <span>{{ item.name }}</span>
            <b>{{ item.value }}</b>
          </div>
          <el-empty v-if="!topDepartments.length" description="暂无导诊数据" :image-size="60"></el-empty>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div class="metric-label">常见症状描述 Top 5</div>
          <div v-for="item in topSymptoms" :key="item.name" class="rank-row">
            <span>{{ item.name }}</span>
            <b>{{ item.value }}</b>
          </div>
          <el-empty v-if="!topSymptoms.length" description="暂无症状数据" :image-size="60"></el-empty>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div class="metric-label">常见命中关键词 Top 5</div>
          <div v-for="item in topKeywords" :key="item.name" class="rank-row">
            <span>{{ item.name }}</span>
            <b>{{ item.value }}</b>
          </div>
          <el-empty v-if="!topKeywords.length" description="暂无关键词数据" :image-size="60"></el-empty>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="recent-card">
      <div slot="header">最近导诊记录</div>
      <el-table :data="recentRecords" border stripe v-loading="loading">
        <template slot="empty">暂无导诊记录</template>
        <el-table-column prop="createTime" label="时间" width="170"></el-table-column>
        <el-table-column prop="patientId" label="患者ID" width="90"></el-table-column>
        <el-table-column prop="symptomText" label="症状描述" min-width="180" show-overflow-tooltip></el-table-column>
        <el-table-column prop="recommendedDepartment" label="推荐科室" width="130"></el-table-column>
        <el-table-column prop="diseaseName" label="疑似方向" width="140"></el-table-column>
        <el-table-column prop="matchedKeywords" label="命中关键词" width="150" show-overflow-tooltip></el-table-column>
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
      enabledRuleCount: 0,
      disabledRuleCount: 0,
      coveredDepartmentCount: 0,
      topDepartments: [],
      topSymptoms: [],
      topKeywords: [],
      recentRecords: [],
      loading: false
    };
  },
  methods: {
    loadStatistics() {
      this.loading = true;
      getGuidanceStatistics()
        .then((res) => {
          if (res.data.status === 200) {
            const data = res.data.data || {};
            this.total = data.total || 0;
            this.enabledRuleCount = data.enabledRuleCount || 0;
            this.disabledRuleCount = data.disabledRuleCount || 0;
            this.coveredDepartmentCount = data.coveredDepartmentCount || 0;
            this.topDepartments = data.topDepartments || [];
            this.topSymptoms = data.topSymptoms || [];
            this.topKeywords = data.topKeywords || [];
            this.recentRecords = data.recentRecords || [];
          }
        })
        .finally(() => {
          this.loading = false;
        });
    }
  },
  created() {
    this.loadStatistics();
  }
};
</script>

<style scoped>
.statistics-tip {
  margin-bottom: 16px;
}
.metric-label {
  color: #606266;
  margin-bottom: 12px;
}
.metric-value {
  font-size: 36px;
  font-weight: 600;
  color: #1f6fca;
}
.metric-value.muted {
  color: #909399;
}
.rank-cards {
  margin-top: 16px;
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
