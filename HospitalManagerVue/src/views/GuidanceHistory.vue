<template>
  <el-card>
    <div slot="header" class="header">
      <span>导诊历史</span>
      <el-button type="primary" size="small" @click="$router.push('/patient/guidance')">
        返回智能导诊
      </el-button>
    </div>
    <el-alert
      v-if="noPatientId"
      class="history-alert"
      title="暂未获取到患者登录信息，无法查询个人导诊历史。你仍可以返回智能导诊进行游客测试。"
      type="warning"
      show-icon
      :closable="false"
    ></el-alert>
    <el-table :data="records" border stripe v-loading="loading">
      <template slot="empty">暂无导诊历史记录</template>
      <el-table-column prop="createTime" label="时间" width="170"></el-table-column>
      <el-table-column prop="symptomText" label="症状输入" min-width="180" show-overflow-tooltip></el-table-column>
      <el-table-column prop="recommendedDepartment" label="建议科室" width="130"></el-table-column>
      <el-table-column prop="diseaseName" label="疑似方向" width="140"></el-table-column>
      <el-table-column prop="matchedKeywords" label="命中关键词" width="160"></el-table-column>
      <el-table-column prop="advice" label="导诊建议" min-width="220" show-overflow-tooltip></el-table-column>
      <el-table-column prop="score" label="匹配分数" width="90"></el-table-column>
      <el-table-column label="操作" width="110">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="primary"
            :disabled="!canRegister(scope.row)"
            @click="goRegister(scope.row)"
          >
            再次预约
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script>
import jwtDecode from "jwt-decode";
import { getGuidanceHistory } from "@/api/guidance.js";
import { buildGuidanceRegisterPath } from "@/utils/departmentAlias.js";
import { getToken } from "@/utils/storage.js";

export default {
  name: "GuidanceHistory",
  data() {
    return {
      records: [],
      loading: false,
      noPatientId: false
    };
  },
  methods: {
    tokenDecode(token) {
      if (token !== null) {
        try {
          return jwtDecode(token);
        } catch (e) {
          return {};
        }
      }
      return {};
    },
    loadHistory() {
      const tokenData = this.tokenDecode(getToken());
      if (!tokenData.pId) {
        this.noPatientId = true;
        this.records = [];
        return;
      }
      this.loading = true;
      getGuidanceHistory(tokenData.pId)
        .then((res) => {
          if (res.data.status === 200) {
            this.records = res.data.data.records || [];
          } else {
            this.records = [];
          }
        })
        .finally(() => {
          this.loading = false;
        });
    },
    canRegister(row) {
      return !!(row && row.recommendedDepartment);
    },
    goRegister(row) {
      const department = row && row.recommendedDepartment;
      if (!department) {
        this.$router.push("/orderOperate");
        return;
      }
      this.$router.push(buildGuidanceRegisterPath(department));
    }
  },
  created() {
    this.loadHistory();
  }
};
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.history-alert {
  margin-bottom: 12px;
}
</style>
