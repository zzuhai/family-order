<template>
  <div class="dish-page">
    <div class="page-toolbar">
      <div class="toolbar-left">
        <el-button type="primary" @click="handleAdd" :icon="Plus">添加菜品</el-button>
        <el-button
          type="danger"
          :disabled="selectedIds.length === 0"
          @click="handleBatchDelete"
          plain
        >
          批量删除{{ selectedIds.length ? ` (${selectedIds.length})` : '' }}
        </el-button>
      </div>
      <div class="toolbar-right">
        <span class="dish-count">共 {{ dishes.length }} 道菜品</span>
      </div>
    </div>

    <div class="search-bar">
      <el-input
        v-model="searchName"
        placeholder="菜品名称"
        clearable
        class="search-input"
        @keyup.enter="handleSearch"
      />
      <el-select v-model="searchCategory" placeholder="菜品分类" clearable class="search-select">
        <el-option label="荤菜" value="荤菜" />
        <el-option label="素菜" value="素菜" />
        <el-option label="汤羹" value="汤羹" />
        <el-option label="家常菜" value="家常菜" />
        <el-option label="凉菜" value="凉菜" />
        <el-option label="主食" value="主食" />
      </el-select>
      <el-select v-model="searchStatus" placeholder="状态" clearable class="search-select">
        <el-option label="上架" :value="1" />
        <el-option label="下架" :value="0" />
      </el-select>
      <el-button type="primary" @click="handleSearch">查询</el-button>
      <el-button @click="handleReset">重置</el-button>
    </div>

    <el-table
      :data="dishes"
      stripe
      @selection-change="handleSelectionChange"
      class="dish-table"
      empty-text="暂无菜品"
    >
      <el-table-column type="selection" width="50" />
      <el-table-column label="菜品" width="220">
        <template #default="{ row }">
          <div class="dish-cell">
            <el-image
              v-if="row.imageUrl"
              :src="row.imageUrl"
              class="dish-thumb"
              fit="cover"
              :preview-src-list="[row.imageUrl]"
            />
            <div v-else class="dish-thumb placeholder">
              <span>{{ row.name.charAt(0) }}</span>
            </div>
            <div class="dish-info">
              <span class="dish-name">{{ row.name }}</span>
              <span class="dish-category">{{ row.category }}</span>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="描述" min-width="200" />
      <el-table-column label="状态" width="90" align="center">
        <template #default="{ row }">
          <el-tag
            :type="row.status === 1 ? 'success' : 'info'"
            effect="plain"
            round
          >
            {{ row.status === 1 ? '上架' : '下架' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" align="center" fixed="right">
        <template #default="{ row }">
          <el-button size="small" text type="primary" @click="handleDetail(row)">详情</el-button>
          <el-button size="small" text @click="handleEdit(row)">编辑</el-button>
          <el-button size="small" text type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑菜品' : '添加菜品'"
      width="720px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <el-form :model="form" label-position="top">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="菜品名称" required>
              <el-input v-model="form.name" placeholder="请输入菜品名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分类" required>
              <el-select v-model="form.category" placeholder="请选择分类" style="width: 100%">
                <el-option label="荤菜" value="荤菜" />
                <el-option label="素菜" value="素菜" />
                <el-option label="汤羹" value="汤羹" />
                <el-option label="家常菜" value="家常菜" />
                <el-option label="凉菜" value="凉菜" />
                <el-option label="主食" value="主食" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入菜品描述" />
        </el-form-item>

        <el-form-item label="菜品图片">
          <div class="image-upload-section">
            <el-upload
              class="custom-upload"
              :show-file-list="false"
              :before-upload="handleImageUpload"
              accept="image/*"
            >
              <template v-if="form.imageUrl">
                <el-image :src="form.imageUrl" class="upload-preview" fit="cover" />
                <div class="upload-overlay">
                  <span>点击替换</span>
                </div>
              </template>
              <template v-else>
                <div class="upload-placeholder">
                  <el-icon :size="32"><Plus /></el-icon>
                  <span>上传图片</span>
                </div>
              </template>
            </el-upload>
            <div class="upload-hint">
              <span>支持 JPG / PNG，建议尺寸 800x600</span>
              <el-button v-if="form.imageUrl" size="small" text type="danger" @click="form.imageUrl = ''">
                删除图片
              </el-button>
            </div>
          </div>
        </el-form-item>

        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :value="1">上架</el-radio>
            <el-radio :value="0">下架</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-divider content-position="left">材料清单</el-divider>
        <div class="dynamic-list">
          <div v-for="(item, idx) in form.ingredients" :key="idx" class="dynamic-row">
            <el-input v-model="item.name" placeholder="材料名称" class="row-name" />
            <el-input v-model="item.amount" placeholder="用量" class="row-amount" />
            <el-input v-model="item.unit" placeholder="单位" class="row-unit" />
            <el-button type="danger" size="small" :icon="Delete" circle @click="form.ingredients.splice(idx, 1)" />
          </div>
          <el-button size="small" :icon="Plus" @click="form.ingredients.push({ name: '', amount: '', unit: '' })">
            添加材料
          </el-button>
        </div>

        <el-divider content-position="left">烹饪步骤</el-divider>
        <div class="dynamic-list steps-list">
          <div v-for="(step, idx) in form.steps" :key="idx" class="step-card-item">
            <div class="step-header">
              <span class="step-badge">步骤 {{ idx + 1 }}</span>
              <el-button type="danger" size="small" :icon="Delete" circle @click="form.steps.splice(idx, 1)" />
            </div>
            <el-input
              v-model="step.description"
              placeholder="请输入步骤描述"
              type="textarea"
              :rows="2"
            />
            <div class="step-image-section">
              <el-upload
                :show-file-list="false"
                :before-upload="getStepUploadHandler(idx)"
                accept="image/*"
              >
                <template v-if="step.imageUrl">
                  <el-image :src="step.imageUrl" class="step-upload-preview" fit="cover" />
                </template>
                <template v-else>
                  <el-button size="small" :icon="Picture">添加步骤图</el-button>
                </template>
              </el-upload>
              <el-button v-if="step.imageUrl" size="small" text type="danger" @click="step.imageUrl = ''">
                删除
              </el-button>
            </div>
          </div>
          <el-button
            size="small"
            :icon="Plus"
            @click="form.steps.push({ stepNumber: form.steps.length + 1, description: '', imageUrl: '' })"
          >
            添加步骤
          </el-button>
        </div>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">保存菜品</el-button>
      </template>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog v-model="detailVisible" title="菜品详情" width="760px" destroy-on-close>
      <div v-if="currentDish" class="dish-detail">
        <div class="detail-hero">
          <div class="detail-image-wrap">
            <el-image
              v-if="currentDish.imageUrl"
              :src="currentDish.imageUrl"
              class="detail-image"
              fit="cover"
              :preview-src-list="[currentDish.imageUrl]"
            />
            <div v-else class="detail-image-placeholder">
              <span>{{ currentDish.name.charAt(0) }}</span>
            </div>
          </div>
          <div class="detail-meta">
            <h2 class="detail-name">{{ currentDish.name }}</h2>
            <el-tag class="detail-category" effect="plain" round>{{ currentDish.category }}</el-tag>
            <p class="detail-desc">{{ currentDish.description || '暂无描述' }}</p>
          </div>
        </div>

        <el-divider />
        <div class="detail-section">
          <h3 class="section-title"><el-icon><ShoppingCart /></el-icon> 所需材料</h3>
          <div v-if="currentDish.ingredients.length > 0" class="ingredient-grid">
            <div v-for="item in currentDish.ingredients" :key="item.id" class="ingredient-chip">
              <span class="chip-name">{{ item.name }}</span>
              <span class="chip-amount">{{ item.amount }} {{ item.unit }}</span>
            </div>
          </div>
          <el-empty v-else description="暂无材料信息" :image-size="60" />
        </div>

        <el-divider />
        <div class="detail-section">
          <h3 class="section-title"><el-icon><Document /></el-icon> 烹饪步骤</h3>
          <div v-if="currentDish.steps.length > 0" class="step-timeline">
            <div v-for="step in currentDish.steps" :key="step.id" class="timeline-item">
              <div class="timeline-number">{{ step.stepNumber }}</div>
              <div class="timeline-content">
                <p>{{ step.description }}</p>
                <el-image
                  v-if="step.imageUrl"
                  :src="step.imageUrl"
                  class="timeline-image"
                  fit="cover"
                  :preview-src-list="[step.imageUrl]"
                />
              </div>
            </div>
          </div>
          <el-empty v-else description="暂无步骤信息" :image-size="60" />
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Delete, Picture, ShoppingCart, Document } from '@element-plus/icons-vue'
import { getDishes, createDish, updateDish, deleteDish, deleteDishes } from '@/api/dish'
import type { Dish } from '@/api/types'

const dishes = ref<Dish[]>([])
const selectedIds = ref<number[]>([])
const dialogVisible = ref(false)
const detailVisible = ref(false)
const isEdit = ref(false)
const currentDish = ref<Dish | null>(null)
const submitting = ref(false)

// 搜索条件
const searchName = ref('')
const searchCategory = ref('')
const searchStatus = ref<number | undefined>(undefined)

const form = ref({
  id: 0,
  name: '',
  description: '',
  imageUrl: '',
  category: '家常菜',
  status: 1,
  ingredients: [] as { name: string; amount: string; unit: string }[],
  steps: [] as { stepNumber: number; description: string; imageUrl?: string }[]
})

onMounted(() => { loadDishes() })

const loadDishes = async () => {
  try {
    const params = {
      name: searchName.value || undefined,
      category: searchCategory.value || undefined,
      status: searchStatus.value
    }
    dishes.value = await getDishes(params)
  }
  catch (e) { console.error(e) }
}

const handleSearch = () => { loadDishes() }

const handleReset = () => {
  searchName.value = ''
  searchCategory.value = ''
  searchStatus.value = undefined
  loadDishes()
}

const getStepUploadHandler = (index: number) => (file: File) => handleStepImageUpload(file, index)

const compressImage = (file: File, maxWidth = 800, quality = 0.8): Promise<string> => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.onload = (e) => {
      const img = new Image()
      img.onload = () => {
        const canvas = document.createElement('canvas')
        let width = img.width
        let height = img.height
        if (width > maxWidth) {
          height = (height * maxWidth) / width
          width = maxWidth
        }
        canvas.width = width
        canvas.height = height
        const ctx = canvas.getContext('2d')
        if (!ctx) { reject(new Error('无法获取canvas context')); return }
        ctx.drawImage(img, 0, 0, width, height)
        const compressedBase64 = canvas.toDataURL('image/jpeg', quality)
        resolve(compressedBase64)
      }
      img.onerror = () => reject(new Error('图片加载失败'))
      img.src = e.target?.result as string
    }
    reader.onerror = () => reject(new Error('文件读取失败'))
    reader.readAsDataURL(file)
  })
}

const handleImageUpload = async (file: File): Promise<boolean> => {
  try {
    if (file.size > 10 * 1024 * 1024) { ElMessage.warning('图片大小不能超过10MB'); return false }
    ElMessage.info('正在压缩图片...')
    form.value.imageUrl = await compressImage(file, 800, 0.8)
    ElMessage.success('图片上传成功')
  } catch (error) {
    console.error(error); ElMessage.error('图片处理失败')
  }
  return false
}

const handleStepImageUpload = async (file: File, index: number): Promise<boolean> => {
  try {
    if (file.size > 10 * 1024 * 1024) { ElMessage.warning('图片大小不能超过10MB'); return false }
    ElMessage.info('正在压缩图片...')
    form.value.steps[index].imageUrl = await compressImage(file, 600, 0.75)
    ElMessage.success('步骤图片上传成功')
  } catch (error) {
    console.error(error); ElMessage.error('图片处理失败')
  }
  return false
}

const handleAdd = () => {
  isEdit.value = false
  form.value = { id: 0, name: '', description: '', imageUrl: '', category: '家常菜', status: 1, ingredients: [], steps: [] }
  dialogVisible.value = true
}

const handleEdit = (row: Dish) => {
  isEdit.value = true
  form.value = {
    id: row.id,
    name: row.name,
    description: row.description,
    imageUrl: row.imageUrl,
    category: row.category,
    status: row.status,
    ingredients: row.ingredients.map(i => ({ name: i.name, amount: i.amount, unit: i.unit })),
    steps: row.steps.map(s => ({ stepNumber: s.stepNumber, description: s.description, imageUrl: s.imageUrl }))
  }
  dialogVisible.value = true
}

const handleDetail = (row: Dish) => {
  currentDish.value = row
  detailVisible.value = true
}

const handleDelete = async (id: number) => {
  await ElMessageBox.confirm('确定删除该菜品吗？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
  await deleteDish(id)
  ElMessage.success('删除成功')
  loadDishes()
}

const handleSelectionChange = (selection: Dish[]) => { selectedIds.value = selection.map(s => s.id) }

const handleBatchDelete = async () => {
  if (selectedIds.value.length === 0) return
  await ElMessageBox.confirm(`确定批量删除选中的 ${selectedIds.value.length} 个菜品吗？`, '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
  await deleteDishes(selectedIds.value)
  ElMessage.success(`成功删除 ${selectedIds.value.length} 个菜品`)
  selectedIds.value = []
  loadDishes()
}

const handleSubmit = async () => {
  if (!form.value.name) { ElMessage.warning('请输入菜品名称'); return }
  try {
    submitting.value = true
    if (isEdit.value) { await updateDish(form.value.id, form.value); ElMessage.success('更新成功') }
    else { await createDish(form.value); ElMessage.success('添加成功') }
    dialogVisible.value = false; loadDishes()
  } catch (e) {
    console.error(e); ElMessage.error('操作失败')
  } finally { submitting.value = false }
}
</script>

<style scoped>
.dish-page { max-width: 1200px; }

.page-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
.toolbar-left { display: flex; gap: 12px; }
.dish-count { font-size: 13px; color: var(--color-muted); }

/* Search bar */
.search-bar {
  display: flex;
  gap: 12px;
  align-items: center;
  margin-bottom: 20px;
  flex-wrap: wrap;
}
.search-input { width: 200px; }
.search-select { width: 140px; }

/* Table */
.dish-table { border-radius: 12px; overflow: hidden; }

.dish-cell {
  display: flex;
  align-items: center;
  gap: 12px;
}
.dish-thumb {
  width: 52px; height: 52px;
  border-radius: 10px;
  flex-shrink: 0;
  object-fit: cover;
}
.dish-thumb.placeholder {
  background: linear-gradient(135deg, #F5D4B8, #E8A87C);
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: 'Noto Serif SC', serif;
  font-size: 20px;
  color: #fff;
  font-weight: 700;
}
.dish-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}
.dish-name { font-weight: 600; font-size: 14px; color: var(--color-brown); }
.dish-category { font-size: 12px; color: var(--color-muted); }

/* Upload */
.image-upload-section { display: flex; align-items: flex-start; gap: 16px; }
.custom-upload {
  border: 2px dashed #F0E8E0;
  border-radius: 12px;
  cursor: pointer;
  overflow: hidden;
  transition: all 0.3s;
  position: relative;
  width: 200px; height: 200px;
}
.custom-upload:hover { border-color: var(--color-clay-light); }
.upload-preview { width: 200px; height: 200px; display: block; }
.upload-overlay {
  position: absolute; inset: 0;
  background: rgba(61, 44, 46, 0.5);
  display: flex; align-items: center; justify-content: center;
  opacity: 0; transition: opacity 0.3s;
  color: #fff; font-size: 14px;
}
.custom-upload:hover .upload-overlay { opacity: 1; }
.upload-placeholder {
  width: 200px; height: 200px;
  display: flex; flex-direction: column;
  align-items: center; justify-content: center;
  gap: 8px; color: var(--color-muted);
}
.upload-hint { display: flex; flex-direction: column; gap: 8px; }
.upload-hint span { font-size: 12px; color: var(--color-muted); }

/* Dynamic lists */
.dynamic-list { padding: 8px 0; }
.dynamic-row {
  display: flex; gap: 8px;
  margin-bottom: 8px; align-items: center;
}
.row-name { flex: 2; }
.row-amount { flex: 1; }
.row-unit { width: 100px; }

/* Steps */
.steps-list { display: flex; flex-direction: column; gap: 12px; }
.step-card-item {
  padding: 16px;
  background: #FFFDF8;
  border: 1px solid #F0E8E0;
  border-radius: 12px;
}
.step-header {
  display: flex; justify-content: space-between; align-items: center;
  margin-bottom: 10px;
}
.step-badge {
  font-family: 'Noto Serif SC', serif;
  font-weight: 600; font-size: 13px;
  color: var(--color-clay);
  background: rgba(212, 118, 74, 0.1);
  padding: 2px 12px;
  border-radius: 20px;
}
.step-image-section{
  display: flex; align-items: center; gap: 8px;
  margin-top: 8px;
}
.step-upload-preview { width: 100px; height: 70px; border-radius: 8px; object-fit: cover; }

/* Detail dialog */
.detail-hero { display: flex; gap: 24px; }
.detail-image-wrap { flex-shrink: 0; }
.detail-image { width: 240px; height: 240px; border-radius: 16px; object-fit: cover; }
.detail-image-placeholder {
  width: 240px; height: 240px;
  background: linear-gradient(135deg, #F5D4B8, #E8A87C);
  border-radius: 16px;
  display: flex; align-items: center; justify-content: center;
  font-size: 64px; color: #fff;
  font-family: 'Noto Serif SC', serif; font-weight: 700;
}
.detail-meta { flex: 1; display: flex; flex-direction: column; gap: 12px; }
.detail-name {
  font-family: 'Noto Serif SC', serif;
  font-size: 26px; font-weight: 700;
  color: var(--color-brown); margin: 0;
}
.detail-category { align-self: flex-start; }
.detail-desc { color: var(--color-brown-light); line-height: 1.6; font-size: 14px; margin: 8px 0 0; }

.detail-section { margin: 16px 0; }
.section-title { display: flex; align-items: center; gap: 8px; font-size: 16px; color: var(--color-brown); margin-bottom: 16px; }

.ingredient-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
  gap: 8px;
}
.ingredient-chip {
  display: flex; justify-content: space-between; align-items: center;
  padding: 10px 14px;
  background: #FFFDF8;
  border: 1px solid #F0E8E0;
  border-radius: 10px;
}
.chip-name { font-weight: 500; color: var(--color-brown); font-size: 13px; }
.chip-amount { color: var(--color-muted); font-size: 12px; }

.step-timeline { display: flex; flex-direction: column; gap: 16px; }
.timeline-item { display: flex; gap: 16px; }
.timeline-number {
  width: 36px; height: 36px;
  background: linear-gradient(135deg, #D4764A, #E8A87C);
  color: #fff;
  border-radius: 50%;
  display: flex; align-items: center; justify-content: center;
  font-weight: 700; font-size: 14px;
  flex-shrink: 0;
}
.timeline-content {
  flex: 1; padding-top: 4px;
}
.timeline-content p { margin: 0 0 8px; color: var(--color-brown-light); line-height: 1.6; font-size: 14px; }
.timeline-image { width: 200px; height: 140px; border-radius: 10px; object-fit: cover; }
</style>
