<template>
  <div class="navigation-page">
    <el-card>
      <div slot="header" class="header">
        <span>科室导航</span>
      </div>
      <el-alert
        class="notice"
        title="本功能基于院内科室位置配置提供路线提示，仅作为到院指引参考。"
        type="info"
        show-icon
        :closable="false"
      ></el-alert>
      <el-form inline @submit.native.prevent>
        <el-form-item label="科室">
          <el-input
            v-model="department"
            clearable
            placeholder="请输入科室，例如：呼吸内科"
            @clear="loadNavigation"
          ></el-input>
        </el-form-item>
        <el-button type="primary" :loading="loading" @click="search">查询路线</el-button>
        <el-button @click="reset">查看全部</el-button>
      </el-form>
    </el-card>

    <el-row :gutter="16" class="nav-list" v-loading="loading">
      <el-col v-for="item in items" :key="item.department" :md="8" :sm="24">
        <el-card class="nav-card">
          <div class="department">{{ item.department }}</div>
          <el-descriptions :column="1" size="small" border>
            <el-descriptions-item label="科室位置">{{ item.location }}</el-descriptions-item>
            <el-descriptions-item label="路线说明">{{ item.routeTip }}</el-descriptions-item>
            <el-descriptions-item v-if="item.matchedRules" label="相关规则">{{ item.matchedRules }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
      <el-empty v-if="!loading && !items.length" description="暂无科室导航数据"></el-empty>
    </el-row>
  </div>
</template>

<script>
import { getGuidanceNavigation } from "@/api/guidance.js";
import { getGuidanceDepartment } from "@/utils/departmentAlias.js";

export default {
  name: "PatientNavigation",
  data() {
    return {
      department: "",
      items: [],
      loading: false
    };
  },
  methods: {
    loadNavigation(department) {
      this.loading = true;
      getGuidanceNavigation(department)
        .then((res) => {
          if (res.data.status === 200) {
            const data = res.data.data || {};
            this.items = data.items || [];
          } else {
            this.items = [];
          }
        })
        .finally(() => {
          this.loading = false;
        });
    },
    search() {
      const query = this.department.trim();
      this.loadNavigation(query ? getGuidanceDepartment(query) : "");
    },
    reset() {
      this.department = "";
      this.loadNavigation();
    }
  },
  created() {
    const department = this.$route.query.department || "";
    this.department = department;
    this.loadNavigation(department ? getGuidanceDepartment(department) : "");
  }
};
</script>

<style scoped>
.header {
  font-size: 18px;
  font-weight: 600;
}
.notice {
  margin-bottom: 14px;
}
.nav-list {
  margin-top: 16px;
}
.nav-card {
  min-height: 220px;
  margin-bottom: 16px;
}
.department {
  font-size: 20px;
  font-weight: 600;
  color: #1f6fca;
  margin-bottom: 12px;
}
</style>
