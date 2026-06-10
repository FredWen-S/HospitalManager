<template>
  <el-card>
    <div slot="header" class="toolbar">
      <div>
        <el-select v-model="filters.status" clearable placeholder="处理状态" class="filter">
          <el-option label="待处理" value="待处理"></el-option>
          <el-option label="已处理" value="已处理"></el-option>
        </el-select>
        <el-select v-model="filters.type" clearable placeholder="反馈类型" class="filter">
          <el-option label="导诊建议" value="导诊建议"></el-option>
          <el-option label="挂号问题" value="挂号问题"></el-option>
          <el-option label="系统问题" value="系统问题"></el-option>
          <el-option label="其他" value="其他"></el-option>
        </el-select>
        <el-input
          v-model="filters.query"
          clearable
          placeholder="搜索内容、联系方式或回复"
          class="search"
          @keyup.enter.native="loadFeedbacks"
        ></el-input>
        <el-button type="primary" @click="loadFeedbacks">查询</el-button>
      </div>
    </div>

    <el-table :data="feedbacks" border stripe v-loading="loading">
      <template slot="empty">暂无用户反馈</template>
      <el-table-column prop="id" label="编号" width="70"></el-table-column>
      <el-table-column prop="patientId" label="患者ID" width="90"></el-table-column>
      <el-table-column prop="feedbackType" label="类型" width="110"></el-table-column>
      <el-table-column prop="content" label="反馈内容" min-width="220" show-overflow-tooltip></el-table-column>
      <el-table-column prop="contact" label="联系方式" width="140" show-overflow-tooltip></el-table-column>
      <el-table-column label="状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '已处理' ? 'success' : 'warning'">
            {{ scope.row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="reply" label="管理员回复" min-width="180" show-overflow-tooltip></el-table-column>
      <el-table-column prop="createTime" label="提交时间" width="170"></el-table-column>
      <el-table-column label="操作" width="190">
        <template slot-scope="scope">
          <el-button size="mini" @click="openReply(scope.row)">回复</el-button>
          <el-button
            size="mini"
            type="success"
            :disabled="scope.row.status === '已处理'"
            @click="markHandled(scope.row)"
          >
            标记已处理
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="处理反馈" :visible.sync="dialogVisible" width="560px">
      <el-form label-width="90px">
        <el-form-item label="反馈内容">
          <div class="content-text">{{ current.content }}</div>
        </el-form-item>
        <el-form-item label="管理员回复">
          <el-input v-model="reply" type="textarea" :rows="4" placeholder="可填写处理说明，也可留空直接标记已处理"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReply">保存处理结果</el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
import { getFeedbackList, replyFeedback, updateFeedbackStatus } from "@/api/feedback.js";

export default {
  name: "FeedbackList",
  data() {
    return {
      loading: false,
      feedbacks: [],
      filters: {
        status: "",
        type: "",
        query: ""
      },
      dialogVisible: false,
      current: {},
      reply: ""
    };
  },
  methods: {
    loadFeedbacks() {
      this.loading = true;
      getFeedbackList(this.filters)
        .then((res) => {
          if (res.data.status === 200) {
            this.feedbacks = (res.data.data && res.data.data.feedbacks) || [];
          } else {
            this.feedbacks = [];
          }
        })
        .finally(() => {
          this.loading = false;
        });
    },
    openReply(row) {
      this.current = row || {};
      this.reply = this.current.reply || "";
      this.dialogVisible = true;
    },
    submitReply() {
      replyFeedback(this.current.id, { reply: this.reply }).then((res) => {
        if (res.data.status !== 200) {
          this.$message.error(res.data.msg || "保存失败");
          return;
        }
        this.$message.success("已处理反馈");
        this.dialogVisible = false;
        this.loadFeedbacks();
      });
    },
    markHandled(row) {
      updateFeedbackStatus(row.id, { status: "已处理" }).then((res) => {
        if (res.data.status === 200) {
          this.$message.success("已标记处理");
          this.loadFeedbacks();
        }
      });
    }
  },
  created() {
    this.loadFeedbacks();
  }
};
</script>

<style scoped>
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.filter {
  width: 130px;
  margin-right: 8px;
}
.search {
  width: 260px;
  margin-right: 8px;
}
.content-text {
  color: #303133;
  line-height: 22px;
  word-break: break-all;
}
</style>
