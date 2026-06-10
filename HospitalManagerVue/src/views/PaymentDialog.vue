<template>
  <el-dialog
    title="挂号缴费"
    :visible="visible"
    width="420px"
    @close="$emit('close')"
  >
    <div v-if="order">
      <el-descriptions :column="1" border size="small">
        <el-descriptions-item label="挂号单号">{{ order.oId }}</el-descriptions-item>
        <el-descriptions-item label="医生">{{ order.dName }}</el-descriptions-item>
        <el-descriptions-item label="挂号时间">{{ order.oStart }}</el-descriptions-item>
        <el-descriptions-item label="费用">{{ formatPrice(order.oTotalPrice) }} 元</el-descriptions-item>
      </el-descriptions>
    </div>
    <div slot="footer">
      <el-button @click="$emit('close')">取消</el-button>
      <el-button type="primary" @click="confirmPayment">确认缴费</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: "PaymentDialog",
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    order: {
      type: Object,
      default: null
    }
  },
  methods: {
    formatPrice(price) {
      const value = Number(price || 0);
      return value.toFixed(2);
    },
    confirmPayment() {
      if (!this.order) {
        this.$emit("close");
        return;
      }
      this.$emit("success", Object.assign({}, this.order, { oPriceState: 1 }));
    }
  }
};
</script>
