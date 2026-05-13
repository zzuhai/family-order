<template>
  <div class="order-page">
    <div class="page-header">
      <el-date-picker v-model="selectedDate" format="YYYY-MM-DD" value-format="YYYY-MM-DD" />
      <el-button type="primary" @click="loadOrders">查询</el-button>
    </div>

    <el-table :data="orders" stripe>
      <el-table-column prop="memberName" label="成员" width="120" />
      <el-table-column prop="menuDate" label="点餐日期" width="120" />
      <el-table-column label="点餐菜品">
        <template #default="{ row }">
          <el-tag v-for="item in row.items" :key="item.id" style="margin-right: 5px">
            {{ item.dishName }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="提交时间" width="180" />
    </el-table>

    <el-empty v-if="orders.length === 0" description="暂无订单" />

    <el-card style="margin-top: 20px">
      <template #header>
        <span>今日菜品汇总</span>
      </template>
      <div v-for="dish in dishSummary" :key="dish.name" class="summary-item">
        <span>{{ dish.name }}</span>
        <el-tag type="success">{{ dish.count }} 人选择</el-tag>
      </div>
      <el-empty v-if="dishSummary.length === 0" description="暂无数据" />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import dayjs from 'dayjs'
import { getOrdersByDate } from '@/api/menu'
import type { Order } from '@/api/types'

const selectedDate = ref(dayjs().format('YYYY-MM-DD'))
const orders = ref<Order[]>([])

const dishSummary = computed(() => {
  const summary: { name: string; count: number }[] = []
  const dishCount: Record<string, number> = {}

  orders.value.forEach(order => {
    order.items.forEach(item => {
      dishCount[item.dishName] = (dishCount[item.dishName] || 0) + 1
    })
  })

  for (const [name, count] of Object.entries(dishCount)) {
    summary.push({ name, count })
  }

  return summary.sort((a, b) => b.count - a.count)
})

onMounted(() => {
  loadOrders()
})

const loadOrders = async () => {
  try {
    orders.value = await getOrdersByDate(selectedDate.value)
  } catch (e) {
    console.error(e)
  }
}

const getStatusType = (status: string) => {
  switch (status) {
    case 'submitted': return 'warning'
    case 'preparing': return 'primary'
    case 'completed': return 'success'
    default: return 'info'
  }
}

const getStatusText = (status: string) => {
  switch (status) {
    case 'submitted': return '已提交'
    case 'preparing': return '准备中'
    case 'completed': return '已完成'
    default: return '未知'
  }
}
</script>

<style scoped>
.page-header {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.summary-item {
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
  border-bottom: 1px solid #eee;
}
</style>