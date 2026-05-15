<template>
  <div class="menu-page">
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
        <el-button type="primary" @click="handlePublish" :icon="Edit">发布菜单</el-button>
      </div>
      <div class="toolbar-right">
        <span class="dish-count">{{ menus.length }} 道菜品 · {{ selectedDate }}</span>
      </div>
    </div>

    <div v-if="menus.length > 0" class="menu-grid">
      <div v-for="menu in menus" :key="menu.id" class="menu-card">
        <el-image
          v-if="menu.dishImageUrl"
          :src="menu.dishImageUrl"
          class="menu-card-image"
          fit="cover"
        />
        <div v-else class="menu-card-image placeholder">
          <span>{{ menu.dishName.charAt(0) }}</span>
        </div>
        <div class="menu-card-body">
          <h4 class="menu-card-name">{{ menu.dishName }}</h4>
          <p class="menu-card-desc">{{ menu.dishDescription }}</p>
        </div>
        <div class="menu-card-footer">
          <el-tag size="small" effect="plain" round>{{ menu.dishId }}</el-tag>
        </div>
      </div>
    </div>

    <el-empty v-else description="该日期暂无菜单，点击「发布菜单」添加菜品" :image-size="80" />

    <el-dialog v-model="publishVisible" title="发布每日菜单" width="640px" destroy-on-close>
      <div class="publish-hint">选择 {{ selectedDate }} 要供应的菜品</div>
      <el-checkbox-group v-model="selectedDishIds" class="dish-checkbox-grid">
        <div v-for="dish in allDishes" :key="dish.id" class="dish-checkbox-item">
          <el-checkbox :value="dish.id" class="dish-checkbox-label">
            <div class="checkbox-content">
              <span class="checkbox-name">{{ dish.name }}</span>
              <el-tag size="small" effect="plain" round>{{ dish.category }}</el-tag>
            </div>
          </el-checkbox>
        </div>
      </el-checkbox-group>
      <template #footer>
        <el-button @click="publishVisible = false">取消</el-button>
        <el-button type="primary" @click="handlePublishSubmit">确认发布</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { Edit } from '@element-plus/icons-vue'
import dayjs from 'dayjs'
import { getDishes } from '@/api/dish'
import { getMenuByDate, publishMenu } from '@/api/menu'
import type { Dish, DailyMenu } from '@/api/types'

const selectedDate = ref(dayjs().format('YYYY-MM-DD'))
const menus = ref<DailyMenu[]>([])
const allDishes = ref<Dish[]>([])
const publishVisible = ref(false)
const selectedDishIds = ref<number[]>([])

onMounted(() => { loadMenu(); loadAllDishes() })
watch(selectedDate, () => { loadMenu() })

const loadMenu = async () => {
  try { menus.value = await getMenuByDate(selectedDate.value) }
  catch (e) { console.error(e) }
}

const loadAllDishes = async () => {
  try { allDishes.value = await getDishes() }
  catch (e) { console.error(e) }
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
  } catch (e) { console.error(e) }
}
</script>

<style scoped>
.menu-page { max-width: 1200px; }

.page-toolbar {
  display: flex; justify-content: space-between; align-items: center;
  margin-bottom: 24px;
  flex-wrap: wrap; gap: 12px;
}
.toolbar-left { display: flex; gap: 12px; align-items: center; }
.date-picker { width: 160px; }
.dish-count { font-size: 13px; color: var(--color-muted); }

.menu-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
}

.menu-card {
  background: #fff;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: var(--shadow-card);
  transition: var(--transition);
}
.menu-card:hover {
  box-shadow: var(--shadow-card-hover);
  transform: translateY(-2px);
}

.menu-card-image {
  width: 100%; height: 160px;
  display: block;
}
.menu-card-image.placeholder {
  background: linear-gradient(135deg, #A8B88E, #7A8B5E);
  display: flex; align-items: center; justify-content: center;
  font-size: 48px; color: #fff;
  font-family: 'Noto Serif SC', serif; font-weight: 700;
}

.menu-card-body { padding: 16px 16px 8px; }
.menu-card-name {
  font-family: 'Noto Serif SC', serif;
  margin: 0 0 6px;
  font-size: 16px; color: var(--color-brown);
}
.menu-card-desc {
  margin: 0;
  font-size: 13px; color: var(--color-muted);
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.menu-card-footer { padding: 8px 16px 14px; }

/* Publish dialog */
.publish-hint {
  font-size: 14px; color: var(--color-muted);
  margin-bottom: 16px;
}
.dish-checkbox-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 8px;
}
.dish-checkbox-item {
  padding: 8px 12px;
  border: 1px solid #F0E8E0;
  border-radius: 10px;
  transition: var(--transition);
}
.dish-checkbox-item:hover { border-color: var(--color-clay-light); background: #FFFDF8; }
.checkbox-content {
  display: flex; align-items: center; gap: 8px;
}
.checkbox-name { font-size: 14px; color: var(--color-brown); font-weight: 500; }
</style>
