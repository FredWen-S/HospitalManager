<template>
  <el-card>
    <div slot="header" class="header">意见反馈</div>
    <el-alert
      class="notice"
      title="欢迎提交使用中的问题和建议，医院工作人员会在后台查看并处理。"
      type="info"
      show-icon
      :closable="false"
    ></el-alert>
    <el-form :model="form" label-width="90px" class="feedback-form">
      <el-form-item label="反馈类型">
        <el-select v-model="form.feedbackType" placeholder="请选择反馈类型">
          <el-option label="导诊建议" value="导诊建议"></el-option>
          <el-option label="挂号问题" value="挂号问题"></el-option>
          <el-option label="系统问题" value="系统问题"></el-option>
          <el-option label="其他" value="其他"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="反馈内容" required>
        <el-input
          v-model="form.content"
          type="textarea"
          :rows="5"
          maxlength="500"
          show-word-limit
          placeholder="请描述你的问题或建议"
        ></el-input>
      </el-form-item>
      <el-form-item label="联系方式">
        <el-input v-model="form.contact" maxlength="80" placeholder="手机号、邮箱或其他联系方式，可不填"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="loading" @click="submit">提交反馈</el-button>
        <el-button @click="reset">清空</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
import jwtDecode from "jwt-decode";
import { submitFeedback } from "@/api/feedback.js";
import { getToken } from "@/utils/storage.js";

export default {
  name: "FeedbackSubmit",
  data() {
    return {
      loading: false,
      form: {
        patientId: null,
        feedbackType: "导诊建议",
        content: "",
        contact: ""
      }
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
    submit() {
      if (!this.form.content.trim()) {
        this.$message.warning("反馈内容不能为空");
        return;
      }
      const tokenData = this.tokenDecode(getToken());
      const data = Object.assign({}, this.form, {
        patientId: tokenData.pId || null,
        content: this.form.content.trim(),
        contact: this.form.contact.trim()
      });
      this.loading = true;
      submitFeedback(data)
        .then((res) => {
          if (res.data.status !== 200) {
            this.$message.error(res.data.msg || "提交失败");
            return;
          }
          this.$message.success("反馈已提交");
          this.reset();
        })
        .finally(() => {
          this.loading = false;
        });
    },
    reset() {
      this.form.content = "";
      this.form.contact = "";
      this.form.feedbackType = "导诊建议";
    }
  }
};
</script>

<style scoped>
.header {
  font-size: 18px;
  font-weight: 600;
}
.notice {
  margin-bottom: 16px;
}
.feedback-form {
  max-width: 720px;
}
</style>
