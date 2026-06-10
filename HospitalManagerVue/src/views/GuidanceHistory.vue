<template>
  <el-card>
    <div slot="header" class="header">
      <span>导诊历史</span>
      <el-button type="primary" size="small" @click="$router.push('/patient/guidance')">
        返回智能导诊
      </el-button>
    </div>
    <el-table :data="records" border stripe>
      <el-table-column prop="createTime" label="时间" width="170"></el-table-column>
      <el-table-column prop="symptomText" label="症状描述"></el-table-column>
      <el-table-column prop="recommendedDepartment" label="推荐科室" width="130"></el-table-column>
      <el-table-column prop="diseaseName" label="疑似疾病" width="140"></el-table-column>
      <el-table-column prop="matchedKeywords" label="命中关键词" width="160"></el-table-column>
      <el-table-column prop="score" label="分数" width="70"></el-table-column>
      <el-table-column label="操作" width="110">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="primary"
            :disabled="!scope.row.recommendedDepartment"
            @click="goRegister(scope.row.recommendedDepartment)"
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
      records: []
    };
  },
  methods: {
    tokenDecode(token) {
      if (token !== null) return jwtDecode(token);
      return {};
    },
    loadHistory() {
      const tokenData = this.tokenDecode(getToken());
      getGuidanceHistory(tokenData.pId).then((res) => {
        if (res.data.status === 200) {
          this.records = res.data.data.records || [];
        }
      });
    },
    goRegister(department) {
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
</style>
