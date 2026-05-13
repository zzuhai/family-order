<template>
  <div class="menu-page">
    <div class="page-header">
      <el-date-picker v-model="selectedDate" format="YYYY-MM-DD" value-format="YYYY-MM-DD" />
      <el-button type="primary" @click="handlePublish">发布菜单</el-button>
    </div>

    <el-card>
      <template #header>
        <span>{{ selectedDate }} 每日菜单</span>
      </template>
      <el-row :gutter="20">
        <el-col v-for="menu in menus" :key="menu.id" :span="6">
          <el-card shadow="hover">
            <el-image v-if="menu.dishImageUrl" :src="menu.dishImageUrl" style="width: 100%; height: 150px" fit="cover" />
            <div style="padding: 10px">
              <h4>{{ menu.dishName }}</h4>
              <p>{{ menu.dishDescription }}</p>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-empty v-if="menus.length === 0" description="暂无菜单" />
    </el-card>

    <el-dialog v-model="publishVisible" title="发布每日菜单" width="600px">
      <el-checkbox-group v-model="selectedDishIds">
        <el-row :gutter="20">
          <el-col v-for="dish in allDishes" :key="dish.id" :span="6">
            <el-checkbox :value="dish.id" class="dish-checkbox">
              <div class="dish-option">
                <span>{{ dish.name }}</span>
                <el-tag size="small">{{ dish.category }}</el-tag>
              </div>
            </el-checkbox>
          </el-col>
        </el-row>
      </el-checkbox-group>
      <template #footer>
        <el-button @click="publishVisible = false">取消</el-button>
        <el-button type="primary" @click="handlePublishSubmit">发布</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import dayjs from 'dayjs'
import { getDishes } from '@/api/dish'
import { getMenuByDate, publishMenu } from '@/api/menu'
import type { Dish, DailyMenu } from '@/api/types'

const selectedDate = ref(dayjs().format('YYYY-MM-DD'))
const menus = ref<DailyMenu[]>([])
const allDishes = ref<Dish[]>([])
const publishVisible = ref(false)
const selectedDishIds = ref<number[]>([])

onMounted(() => {
  loadMenu()
  loadAllDishes()
})

watch(selectedDate, () => {
  loadMenu()
})

const loadMenu = async () => {
  try {
    menus.value = await getMenuByDate(selectedDate.value)
  } catch (e) {
    console.error(e)
  }
}

const loadAllDishes = async () => {
  try {
    allDishes.value = await getDishes()
  } catch (e) {
    console.error(e)
  }
}

const handlePublish = () => {
  selectedDishIds.value = menus.value.map(m => m.dishId)
  publishVisible.value = true
}

const handlePublishSubmit = async () => {
  if (selectedDishIds.value.length === 0) {
    ElMessage.warning('请选择至少一道菜品')
    return
  }

  try {
    await publishMenu(selectedDate.value, selectedDishIds.value)
    ElMessage.success('发布成功')
    publishVisible.value = false
    loadMenu()
  } catch (e) {
    console.error(e)
  }
}
</script>

<style scoped>
.page-header {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.dish-checkbox {
  margin-bottom: 10px;
}

.dish-option {
  display: flex;
  align-items: center;
  gap: 10px;
}
</style>