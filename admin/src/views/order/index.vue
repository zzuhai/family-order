<template>
  <div class="order-page">
    <div class="page-toolbar">
      <div class="toolbar-left">
        <el-date-picker
          v-model="selectedDate"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
          type="date"
          placeholder="选择日期"
          class="date-picker"
        />
        <el-button type="primary" @click="loadOrders" :icon="Search">查询</el-button>
      </div>
      <div class="toolbar-right">
        <span class="order-count">{{ orders.length }} 人已点餐</span>
      </div>
    </div>

    <div class="order-content">
      <div class="order-list-section">
        <el-table :data="orders" stripe class="order-table" empty-text="该日期暂无订单">
          <el-table-column prop="memberName" label="成员" width="110" />
          <el-table-column label="点餐菜品" min-width="250">
            <template #default="{ row }">
              <div class="dish-tags">
                <el-tag
                  v-for="item in row.items"
                  :key="item.id"
                  effect="plain"
                  round
                  size="small"
                  class="dish-tag"
                >
                  {{ item.dishName }}
                </el-tag>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="110" align="center">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)" effect="plain" round>
                {{ getStatusText(row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createdAt" label="提交时间" width="170" />
        </el-table>
      </div>

      <div class="summary-section">
        <div class="summary-card">
          <div class="summary-header">
            <h3 class="summary-title">
              <el-icon style="margin-right: 6px"><TrendCharts /></el-icon>
              菜品热度统计
            </h3>
            <span class="summary-date">{{ selectedDate }}</span>
          </div>
          <div v-if="dishSummary.length > 0" class="summary-list">
            <div v-for="(dish, idx) in dishSummary" :key="dish.name" class="summary-row">
              <span class="summary-rank">{{ idx + 1 }}</span>
              <span class="summary-name">{{ dish.name }}</span>
              <div class="summary-bar-wrap">
                <div class="summary-bar" :style="{ width: getBarWidth(dish.count) + '%' }"></div>
              </div>
              <span class="summary-count">{{ dish.count }} 人</span>
            </div>
          </div>
          <el-empty v-else description="暂无数据" :image-size="50" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import dayjs from 'dayjs'
import { Search, TrendCharts } from '@element-plus/icons-vue'
import { getOrdersByDate } from '@/api/menu'
import type { Order } from '@/api/types'

const selectedDate = ref(dayjs().format('YYYY-MM-DD'))
const orders = ref<Order[]>([])

const dishSummary = computed(() => {
  const dishCount: Record<string, number> = {}
  orders.value.forEach(order => {
    order.items.forEach(item => {
      dishCount[item.dishName] = (dishCount[item.dishName] || 0) + 1
    })
  })
  return Object.entries(dishCount)
    .map(([name, count]) => ({ name, count }))
    .sort((a, b) => b.count - a.count)
})

const maxCount = computed(() => dishSummary.value[0]?.count || 1)
const getBarWidth = (count: number) => Math.round((count / maxCount.value) * 100)

onMounted(() => { loadOrders() })

const loadOrders = async () => {
  try { orders.value = await getOrdersByDate(selectedDate.value) }
  catch (e) { console.error(e) }
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
.order-page { max-width: 1200px; }

.page-toolbar {
  display: flex; justify-content: space-between; align-items: center;
  margin-bottom: 24px;
}
.toolbar-left { display: flex; gap: 12px; }
.date-picker { width: 160px; }
.order-count { font-size: 13px; color: var(--color-muted); }

.order-content {
  display: flex;
  gap: 24px;
  align-items: flex-start;
}
.order-list-section { flex: 1; min-width: 0; }
.order-table { border-radius: 12px; overflow: hidden; }

.dish-tags { display: flex; flex-wrap: wrap; gap: 4px; }
.dish-tag { margin: 2px; }

.summary-section { width: 340px; flex-shrink: 0; }
.summary-card {
  background: #fff;
  border-radius: 16px;
  padding: 20px;
  box-shadow: var(--shadow-card);
}
.summary-header {
  display: flex; justify-content: space-between; align-items: center;
  margin-bottom: 16px; padding-bottom: 12px;
  border-bottom: 1px solid #F0E8E0;
}
.summary-title {
  font-family: 'Noto Serif SC', serif;
  font-size: 15px; color: var(--color-brown);
  margin: 0; display: flex; align-items: center;
}
.summary-date { font-size: 12px; color: var(--color-muted); }

.summary-list { display: flex; flex-direction: column; gap: 10px; }
.summary-row {
  display: flex; align-items: center; gap: 10px;
}
.summary-rank {
  width: 22px; height: 22px;
  border-radius: 50%;
  display: flex; align-items: center; justify-content: center;
  font-size: 12px; font-weight: 700;
  flex-shrink: 0;
}
.summary-row:first-child .summary-rank { background: #E8B84A; color: #fff; }
.summary-row:nth-child(2) .summary-rank { background: #D4764A; color: #fff; }
.summary-row:nth-child(3) .summary-rank { background: #8A7A7B; color: #fff; }
.summary-row:nth-child(n+4) .summary-rank { background: #F0E8E0; color: var(--color-muted); }

.summary-name { flex: 1; font-size: 13px; color: var(--color-brown); min-width: 0; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.summary-bar-wrap {
  width: 60px; height: 6px;
  background: #F0E8E0;
  border-radius: 3px;
  overflow: hidden;
}
.summary-bar {
  height: 100%;
  background: linear-gradient(90deg, var(--color-clay-light), var(--color-clay));
  border-radius: 3px;
  transition: width 0.6s ease;
}
.summary-count { font-size: 12px; color: var(--color-muted); width: 30px; text-align: right; }

@media (max-width: 900px) {
  .order-content { flex-direction: column; }
  .summary-section { width: 100%; }
}
</style>
