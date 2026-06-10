<template>
  <div>
    <el-card>
      <!-- 添加加载状态 -->
      <el-table
        v-loading="loading"
        :data="filteredOrders"
        stripe
        style="width: 100%"
        border
      >
        <!-- 移除非必要字段 -->
        <el-table-column
          prop="oId"
          label="挂号单号"
          width="120"
          align="center"
        ></el-table-column>

        <el-table-column
          prop="dName"
          label="医生"
          width="150"
        ></el-table-column>

        <el-table-column
          prop="oStart"
          label="挂号时间"
          width="180"
          sortable
        ></el-table-column>

        <el-table-column
          prop="oTotalPrice"
          label="费用（元）"
          width="120"
          align="center"
        >
          <template slot-scope="scope">
            ￥{{ scope.row.oTotalPrice.toFixed(2) }}
          </template>
        </el-table-column>

        <!-- 增强支付状态显示 -->
        <el-table-column
          label="缴费状态"
          width="150"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag
              v-if="scope.row.oPriceState === 1"
              type="success"
              effect="dark"
            >已缴费</el-tag>
            <el-button
              v-else
              type="warning"
              :loading="scope.row.paying"
              :disabled="scope.row.oState !== 1 || scope.row.paying"
              @click="initiatePayment(scope.row)"
            >立即支付</el-button>
          </template>
        </el-table-column>

        <!-- 订单状态优化 -->
        <el-table-column
          label="订单状态"
          width="120"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag
              :type="getStatusType(scope.row.oState)"
              effect="dark"
            >{{ orderStatusMap[scope.row.oState] }}</el-tag>
          </template>
        </el-table-column>

        <!-- 添加操作列 -->
        <el-table-column
          label="操作"
          width="180"
          align="center"
        >
          <template slot-scope="scope">
            <el-button
              type="text"
              @click="viewDetails(scope.row)"
            >查看详情</el-button>
            <el-button
              type="text"
              v-if="scope.row.oPriceState === 1"
              @click="downloadInvoice(scope.row)"
            >下载发票</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 支付弹窗 -->
    <payment-dialog
      :visible="paymentVisible"
      :order="selectedOrder"
      @success="handlePaymentSuccess"
      @close="paymentVisible = false"
    />
  </div>
</template>

<script>
import PaymentDialog from './PaymentDialog.vue'

export default {
  components: { PaymentDialog },

  data() {
    return {
      loading: false,
      paymentVisible: false,
      selectedOrder: null,
      orderData: [],
      orderStatusMap: {
        0: '已取消',
        1: '待支付',
        2: '已完成',
        3: '已过期'
      }
    }
  },

  computed: {
    // 只显示当前患者的有效订单
    filteredOrders() {
      return this.orderData.filter(
        order => order.oState !== 0 // 过滤已取消订单
      )
    }
  },

  created() {
    this.fetchOrders()
  },

  methods: {
    async fetchOrders() {
      try {
        this.loading = true
        const { data } = await this.$http.get('/patient/orders', {
          params: {
            patientId: this.$store.state.user.id
          }
        })
        this.orderData = data.map(order => ({
          ...order,
          paying: false // 添加支付状态
        }))
      } catch (error) {
        this.$message.error('获取订单失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    getStatusType(state) {
      const typeMap = {
        0: 'info',
        1: 'warning',
        2: 'success',
        3: 'danger'
      }
      return typeMap[state] || 'info'
    },

    async initiatePayment(order) {
      this.selectedOrder = order
      this.paymentVisible = true
    },

    async handlePaymentSuccess(paidOrder) {
      // 更新本地数据
      const index = this.orderData.findIndex(o => o.oId === paidOrder.oId)
      if (index > -1) {
        this.$set(this.orderData, index, {
          ...paidOrder,
          oPriceState: 1
        })
      }
      this.$message.success('支付成功！')
      this.paymentVisible = false
    },

    viewDetails(order) {
      this.$router.push({
        name: 'OrderDetail',
        params: { orderId: order.oId }
      })
    },

    async downloadInvoice(order) {
      try {
        const { data } = await this.$http.get(`/orders/${order.oId}/invoice`)
        // 创建隐藏的下载链接
        const link = document.createElement('a')
        link.href = data.invoiceUrl
        link.download = `发票-${order.oId}.pdf`
        link.click()
      } catch (error) {
        this.$message.error('下载发票失败：' + error.message)
      }
    }
  }
}
</script>

<style scoped>
.el-card {
  margin: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.el-table {
  margin-top: 15px;
}

.el-button--text {
  padding: 0;
  min-height: auto;
}
</style>
