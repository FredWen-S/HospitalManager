<template>
  <div class="guidance-page">
    <el-card>
      <div slot="header" class="card-header">
        <span>智能导诊</span>
        <el-button type="text" @click="$router.push('/patient/guidance-history')">
          查看导诊历史
        </el-button>
      </div>
      <el-alert
        title="本功能根据症状关键词进行科室推荐，结果仅作为挂号参考，不能替代医生诊断。"
        type="info"
        show-icon
        :closable="false"
        class="notice"
      ></el-alert>
      <el-form @submit.native.prevent>
        <el-form-item>
          <el-input
            v-model="symptomText"
            type="textarea"
            :rows="4"
            maxlength="300"
            show-word-limit
            placeholder="请描述您的症状，例如：我咳嗽三天，有点发烧，喉咙痛"
          ></el-input>
        </el-form-item>
        <el-button type="primary" :loading="loading" @click="analyze">
          开始导诊
        </el-button>
      </el-form>
    </el-card>

    <el-row v-if="results.length" :gutter="16" class="result-row">
      <el-col v-for="item in results" :key="item.recommendedDepartment + item.diseaseName" :md="8" :sm="24">
        <el-card class="result-card">
          <div class="department">建议科室：{{ item.recommendedDepartment }}</div>
          <div class="disease">疑似方向：{{ item.diseaseName }}</div>
          <el-tag
            v-for="word in item.matchedKeywords"
            :key="word"
            size="mini"
            type="success"
            class="keyword"
          >
            {{ word }}
          </el-tag>
          <el-descriptions :column="1" size="small" border class="desc">
            <el-descriptions-item label="就诊建议">{{ item.advice }}</el-descriptions-item>
            <el-descriptions-item label="科室位置">{{ item.location }}</el-descriptions-item>
            <el-descriptions-item label="路线说明">{{ item.routeTip }}</el-descriptions-item>
            <el-descriptions-item label="匹配分数">{{ item.score }}</el-descriptions-item>
            <el-descriptions-item label="参考说明">导诊结果仅作为挂号参考，请以医生面诊意见为准。</el-descriptions-item>
          </el-descriptions>
          <div class="actions">
            <el-button size="small" @click="showDoctors(item)">查看该科室医生</el-button>
            <el-button size="small" @click="goNavigation(item.recommendedDepartment)">
              查看路线
            </el-button>
            <el-button size="small" type="primary" @click="goRegister(item.recommendedDepartment)">
              预约该科室
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card v-if="doctorList.length" class="doctor-card">
      <div slot="header">可预约医生</div>
      <el-table :data="doctorList" border stripe>
        <el-table-column prop="dName" label="姓名" width="100"></el-table-column>
        <el-table-column prop="dSection" label="科室" width="120"></el-table-column>
        <el-table-column prop="dPost" label="职位" width="120"></el-table-column>
        <el-table-column prop="dPrice" label="挂号费" width="100"></el-table-column>
        <el-table-column prop="dIntroduction" label="简介"></el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import jwtDecode from "jwt-decode";
import { analyzeGuidance } from "@/api/guidance.js";
import { buildGuidanceRegisterPath } from "@/utils/departmentAlias.js";
import { getToken } from "@/utils/storage.js";

export default {
  name: "SmartGuidance",
  data() {
    return {
      symptomText: "",
      loading: false,
      results: [],
      doctorList: []
    };
  },
  methods: {
    tokenDecode(token) {
      if (token !== null) return jwtDecode(token);
      return {};
    },
    analyze() {
      if (!this.symptomText.trim()) {
        this.$message.warning("请先输入症状描述");
        return;
      }
      const tokenData = this.tokenDecode(getToken());
      this.loading = true;
      analyzeGuidance({
        symptomText: this.symptomText,
        patientId: tokenData.pId
      })
        .then((res) => {
          if (res.data.status !== 200) {
            this.$message.error(res.data.msg || "导诊失败");
            return;
          }
          this.results = res.data.data.results || [];
          this.doctorList = (res.data.data.topResult && res.data.data.topResult.doctors) || [];
          this.$message.success("导诊分析完成");
        })
        .finally(() => {
          this.loading = false;
        });
    },
    showDoctors(item) {
      this.doctorList = item.doctors || [];
      if (!this.doctorList.length) {
        this.$message.info("该科室暂未维护可预约医生，可先进入挂号页查看排班");
      }
    },
    goRegister(department) {
      if (!department) {
        this.$router.push("/orderOperate");
        return;
      }
      this.$router.push(buildGuidanceRegisterPath(department));
    },
    goNavigation(department) {
      if (!department) {
        this.$router.push("/patient/navigation");
        return;
      }
      this.$router.push("/patient/navigation?department=" + encodeURIComponent(department));
    }
  }
};
</script>

<style scoped lang="scss">
.guidance-page {
  .card-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 18px;
    font-weight: 600;
  }
  .result-row,
  .doctor-card {
    margin-top: 16px;
  }
  .notice {
    margin-bottom: 14px;
  }
  .result-card {
    min-height: 360px;
  }
  .department {
    font-size: 22px;
    font-weight: 600;
    color: #1f6fca;
    margin-bottom: 8px;
  }
  .disease {
    color: #606266;
    margin-bottom: 10px;
  }
  .keyword {
    margin: 0 6px 6px 0;
  }
  .desc {
    margin-top: 8px;
  }
  .actions {
    margin-top: 12px;
    display: flex;
    justify-content: flex-end;
    gap: 8px;
  }
}
</style>
