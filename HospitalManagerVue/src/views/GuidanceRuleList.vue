<template>
  <el-card>
    <div slot="header" class="toolbar">
      <div>
        <el-input
          v-model="query"
          placeholder="搜索科室、疾病或关键词"
          clearable
          class="search"
          @clear="loadRules"
          @keyup.enter.native="loadRules"
        ></el-input>
        <el-button type="primary" @click="loadRules">查询</el-button>
      </div>
      <el-button type="success" @click="openDialog()">新增规则</el-button>
    </div>

    <el-table :data="rules" border stripe>
      <el-table-column prop="department" label="科室" width="120"></el-table-column>
      <el-table-column prop="diseaseName" label="疑似疾病" width="140"></el-table-column>
      <el-table-column prop="keywords" label="关键词"></el-table-column>
      <el-table-column prop="synonyms" label="同义词"></el-table-column>
      <el-table-column prop="priority" label="优先级" width="80"></el-table-column>
      <el-table-column label="状态" width="90">
        <template slot-scope="scope">
          <el-tag :type="scope.row.enabled === 1 ? 'success' : 'info'">
            {{ scope.row.enabled === 1 ? "启用" : "禁用" }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="170">
        <template slot-scope="scope">
          <el-button size="mini" @click="openDialog(scope.row)">编辑</el-button>
          <el-button
            size="mini"
            type="danger"
            :disabled="scope.row.enabled !== 1"
            @click="disableRule(scope.row.id)"
          >
            禁用
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="form.id ? '编辑导诊规则' : '新增导诊规则'" :visible.sync="dialogVisible" width="640px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="科室">
          <el-input v-model="form.department"></el-input>
        </el-form-item>
        <el-form-item label="疑似疾病">
          <el-input v-model="form.diseaseName"></el-input>
        </el-form-item>
        <el-form-item label="关键词">
          <el-input v-model="form.keywords" placeholder="咳嗽,发烧,喉咙痛"></el-input>
        </el-form-item>
        <el-form-item label="同义词">
          <el-input v-model="form.synonyms" placeholder="低烧,高烧,嗓子疼"></el-input>
        </el-form-item>
        <el-form-item label="就诊建议">
          <el-input v-model="form.advice" type="textarea" :rows="3"></el-input>
        </el-form-item>
        <el-form-item label="位置">
          <el-input v-model="form.location"></el-input>
        </el-form-item>
        <el-form-item label="路线">
          <el-input v-model="form.routeTip" type="textarea" :rows="2"></el-input>
        </el-form-item>
        <el-form-item label="优先级">
          <el-input-number v-model="form.priority" :min="0" :max="99"></el-input-number>
        </el-form-item>
        <el-form-item label="启用">
          <el-switch v-model="form.enabled" :active-value="1" :inactive-value="0"></el-switch>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveRule">保存</el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
import {
  createGuidanceRule,
  disableGuidanceRule,
  getGuidanceRules,
  updateGuidanceRule
} from "@/api/guidance.js";

const emptyForm = {
  id: null,
  department: "",
  diseaseName: "",
  keywords: "",
  synonyms: "",
  advice: "",
  location: "",
  routeTip: "",
  priority: 0,
  enabled: 1
};

export default {
  name: "GuidanceRuleList",
  data() {
    return {
      query: "",
      rules: [],
      dialogVisible: false,
      form: Object.assign({}, emptyForm)
    };
  },
  methods: {
    loadRules() {
      getGuidanceRules(this.query).then((res) => {
        if (res.data.status === 200) {
          this.rules = res.data.data.rules || [];
        }
      });
    },
    openDialog(row) {
      this.form = Object.assign({}, emptyForm, row || {});
      this.dialogVisible = true;
    },
    saveRule() {
      if (!this.form.department || !this.form.keywords) {
        this.$message.warning("科室和关键词不能为空");
        return;
      }
      const action = this.form.id
        ? updateGuidanceRule(this.form.id, this.form)
        : createGuidanceRule(this.form);
      action.then((res) => {
        if (res.data.status !== 200) {
          this.$message.error(res.data.msg || "保存失败");
          return;
        }
        this.$message.success("保存成功");
        this.dialogVisible = false;
        this.loadRules();
      });
    },
    disableRule(id) {
      this.$confirm("确认禁用该导诊规则吗？", "提示", { type: "warning" }).then(() => {
        disableGuidanceRule(id).then((res) => {
          if (res.data.status === 200) {
            this.$message.success("已禁用");
            this.loadRules();
          }
        });
      });
    }
  },
  created() {
    this.loadRules();
  }
};
</script>

<style scoped>
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.search {
  width: 260px;
  margin-right: 8px;
}
</style>
