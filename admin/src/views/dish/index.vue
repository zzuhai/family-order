<template>
  <div class="dish-page">
    <div class="page-header">
      <el-button type="primary" @click="handleAdd">添加菜品</el-button>
      <el-button type="danger" :disabled="selectedIds.length === 0" @click="handleBatchDelete">
        批量删除{{ selectedIds.length ? ` (${selectedIds.length})` : '' }}
      </el-button>
    </div>

    <el-table :data="dishes" stripe @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="category" label="分类" width="100" />
      <el-table-column prop="description" label="描述" />
      <el-table-column label="图片" width="100">
        <template #default="{ row }">
          <el-image
            v-if="row.imageUrl"
            :src="row.imageUrl"
            style="width: 60px; height: 60px; border-radius: 4px"
            fit="cover"
            :preview-src-list="[row.imageUrl]"
          />
          <span v-else class="no-image">无图片</span>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? '上架' : '下架' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button size="small" type="primary" @click="handleDetail(row)">详情</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑菜品' : '添加菜品'"
      width="700px"
      :close-on-click-modal="false"
    >
      <el-form :model="form" label-width="100px">
        <el-form-item label="菜品名称" required>
          <el-input v-model="form.name" placeholder="请输入菜品名称" />
        </el-form-item>

        <el-form-item label="分类" required>
          <el-select v-model="form.category" placeholder="请选择分类">
            <el-option label="荤菜" value="荤菜" />
            <el-option label="素菜" value="素菜" />
            <el-option label="汤羹" value="汤羹" />
            <el-option label="家常菜" value="家常菜" />
            <el-option label="凉菜" value="凉菜" />
            <el-option label="主食" value="主食" />
          </el-select>
        </el-form-item>

        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入菜品描述" />
        </el-form-item>

        <el-form-item label="菜品图片">
          <div class="image-upload-section">
            <el-upload
              class="image-uploader"
              :show-file-list="false"
              :before-upload="handleImageUpload"
              accept="image/*"
            >
              <el-image
                v-if="form.imageUrl"
                :src="form.imageUrl"
                class="uploaded-image"
                fit="cover"
              />
              <el-icon v-else class="image-uploader-icon"><Plus /></el-icon>
            </el-upload>
            <div class="upload-tips">
              <span v-if="form.imageUrl" class="tip-success">已上传图片</span>
              <span v-else class="tip-info">点击上传图片（支持jpg、png格式）</span>
              <el-button v-if="form.imageUrl" size="small" type="danger" text @click="form.imageUrl = ''">
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

        <el-divider content-position="left">材料列表</el-divider>
        <div class="ingredients-section">
          <div v-for="(ingredient, index) in form.ingredients" :key="index" class="ingredient-item">
            <el-input v-model="ingredient.name" placeholder="材料名称" class="ingredient-name" />
            <el-input v-model="ingredient.amount" placeholder="用量" class="ingredient-amount" />
            <el-input v-model="ingredient.unit" placeholder="单位" class="ingredient-unit" />
            <el-button type="danger" size="small" :icon="Delete" circle @click="form.ingredients.splice(index, 1)" />
          </div>
          <el-button
            type="primary"
            size="small"
            :icon="Plus"
            @click="form.ingredients.push({ name: '', amount: '', unit: '' })"
          >
            添加材料
          </el-button>
        </div>

        <el-divider content-position="left">烹饪步骤</el-divider>
        <div class="steps-section">
          <div v-for="(step, index) in form.steps" :key="index" class="step-item">
            <div class="step-header">
              <span class="step-number">步骤 {{ index + 1 }}</span>
              <el-button type="danger" size="small" :icon="Delete" circle @click="form.steps.splice(index, 1)" />
            </div>
            <el-input
              v-model="step.description"
              placeholder="请输入步骤描述"
              type="textarea"
              :rows="2"
              class="step-description"
            />
            <div class="step-image-upload">
              <el-upload
                class="step-image-uploader"
                :show-file-list="false"
                :before-upload="(file) => handleStepImageUpload(file, index)"
                accept="image/*"
              >
                <el-image
                  v-if="step.imageUrl"
                  :src="step.imageUrl"
                  class="step-uploaded-image"
                  fit="cover"
                />
                <el-icon v-else class="step-image-uploader-icon"><Picture /></el-icon>
              </el-upload>
              <el-button
                v-if="step.imageUrl"
                size="small"
                type="danger"
                text
                @click="step.imageUrl = ''"
              >
                删除图片
              </el-button>
            </div>
          </div>
          <el-button
            type="primary"
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
        <el-button type="primary" @click="handleSubmit" :loading="submitting">确定</el-button>
      </template>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog v-model="detailVisible" title="菜品详情" width="800px">
      <div v-if="currentDish" class="dish-detail">
        <div class="detail-header">
          <el-image
            v-if="currentDish.imageUrl"
            :src="currentDish.imageUrl"
            class="detail-image"
            fit="cover"
            :preview-src-list="[currentDish.imageUrl]"
          />
          <div class="detail-info">
            <h2>{{ currentDish.name }}</h2>
            <el-tag class="detail-category">{{ currentDish.category }}</el-tag>
            <p class="detail-description">{{ currentDish.description }}</p>
          </div>
        </div>

        <el-divider />

        <div class="detail-section">
          <h3 class="section-title">
            <el-icon><ShoppingCart /></el-icon>
            所需材料
          </h3>
          <div v-if="currentDish.ingredients.length > 0" class="ingredients-list">
            <div v-for="item in currentDish.ingredients" :key="item.id" class="ingredient-card">
              <span class="ingredient-name">{{ item.name }}</span>
              <span class="ingredient-amount">{{ item.amount }} {{ item.unit }}</span>
            </div>
          </div>
          <el-empty v-else description="暂无材料信息" :image-size="80" />
        </div>

        <el-divider />

        <div class="detail-section">
          <h3 class="section-title">
            <el-icon><Document /></el-icon>
            烹饪步骤
          </h3>
          <div v-if="currentDish.steps.length > 0" class="steps-list">
            <div v-for="step in currentDish.steps" :key="step.id" class="step-card">
              <div class="step-number-badge">{{ step.stepNumber }}</div>
              <div class="step-content">
                <p class="step-desc">{{ step.description }}</p>
                <el-image
                  v-if="step.imageUrl"
                  :src="step.imageUrl"
                  class="step-image"
                  fit="cover"
                  :preview-src-list="[step.imageUrl]"
                />
              </div>
            </div>
          </div>
          <el-empty v-else description="暂无步骤信息" :image-size="80" />
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

onMounted(() => {
  loadDishes()
})

const loadDishes = async () => {
  try {
    dishes.value = await getDishes()
  } catch (e) {
    console.error(e)
  }
}

// 图片压缩函数
const compressImage = (file: File, maxWidth = 800, quality = 0.8): Promise<string> => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.onload = (e) => {
      const img = new Image()
      img.onload = () => {
        const canvas = document.createElement('canvas')
        let width = img.width
        let height = img.height

        // 按比例缩放
        if (width > maxWidth) {
          height = (height * maxWidth) / width
          width = maxWidth
        }

        canvas.width = width
        canvas.height = height

        const ctx = canvas.getContext('2d')
        if (!ctx) {
          reject(new Error('无法获取canvas context'))
          return
        }

        ctx.drawImage(img, 0, 0, width, height)

        // 转换为base64，quality参数控制压缩质量
        const compressedBase64 = canvas.toDataURL('image/jpeg', quality)

        // 计算压缩比例
        const originalSize = (e.target?.result as string).length
        const compressedSize = compressedBase64.length
        const ratio = ((1 - compressedSize / originalSize) * 100).toFixed(1)

        console.log(`图片压缩: ${(originalSize / 1024).toFixed(1)}KB -> ${(compressedSize / 1024).toFixed(1)}KB (压缩了${ratio}%)`)

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
    // 检查文件大小（限制10MB）
    if (file.size > 10 * 1024 * 1024) {
      ElMessage.warning('图片大小不能超过10MB')
      return false
    }

    ElMessage.info('正在压缩图片...')
    const compressedBase64 = await compressImage(file, 800, 0.8)
    form.value.imageUrl = compressedBase64
    ElMessage.success('图片上传成功')
  } catch (error) {
    console.error(error)
    ElMessage.error('图片处理失败')
  }
  return false // 返回false阻止自动上传
}

const handleStepImageUpload = async (file: File, index: number): Promise<boolean> => {
  try {
    if (file.size > 10 * 1024 * 1024) {
      ElMessage.warning('图片大小不能超过10MB')
      return false
    }

    ElMessage.info('正在压缩图片...')
    // 步骤图片使用更小的尺寸
    const compressedBase64 = await compressImage(file, 600, 0.75)
    form.value.steps[index].imageUrl = compressedBase64
    ElMessage.success('步骤图片上传成功')
  } catch (error) {
    console.error(error)
    ElMessage.error('图片处理失败')
  }
  return false
}

const handleAdd = () => {
  isEdit.value = false
  form.value = {
    id: 0,
    name: '',
    description: '',
    imageUrl: '',
    category: '家常菜',
    status: 1,
    ingredients: [],
    steps: []
  }
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
  await ElMessageBox.confirm('确定删除该菜品吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
  await deleteDish(id)
  ElMessage.success('删除成功')
  loadDishes()
}

const handleSelectionChange = (selection: Dish[]) => {
  selectedIds.value = selection.map(s => s.id)
}

const handleBatchDelete = async () => {
  if (selectedIds.value.length === 0) return

  await ElMessageBox.confirm(
    `确定批量删除选中的 ${selectedIds.value.length} 个菜品吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  )
  await deleteDishes(selectedIds.value)
  ElMessage.success(`成功删除 ${selectedIds.value.length} 个菜品`)
  selectedIds.value = []
  loadDishes()
}

const handleSubmit = async () => {
  if (!form.value.name) {
    ElMessage.warning('请输入菜品名称')
    return
  }

  try {
    submitting.value = true
    if (isEdit.value) {
      await updateDish(form.value.id, form.value)
      ElMessage.success('更新成功')
    } else {
      await createDish(form.value)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    loadDishes()
  } catch (e) {
    console.error(e)
    ElMessage.error('操作失败')
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.page-header {
  margin-bottom: 20px;
}

.no-image {
  color: #999;
  font-size: 12px;
}

/* 图片上传样式 */
.image-upload-section {
  display: flex;
  align-items: flex-start;
  gap: 20px;
}

.image-uploader {
  border: 2px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  overflow: hidden;
  transition: all 0.3s;
}

.image-uploader:hover {
  border-color: #409eff;
}

.uploaded-image {
  width: 200px;
  height: 200px;
  display: block;
}

.image-uploader-icon {
  font-size: 40px;
  color: #8c939d;
  width: 200px;
  height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.upload-tips {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.tip-success {
  color: #67c23a;
  font-size: 14px;
}

.tip-info {
  color: #909399;
  font-size: 13px;
}

/* 材料列表样式 */
.ingredients-section {
  padding: 10px 0;
}

.ingredient-item {
  display: flex;
  gap: 10px;
  margin-bottom: 10px;
  align-items: center;
}

.ingredient-name {
  flex: 2;
}

.ingredient-amount {
  flex: 1;
}

.ingredient-unit {
  width: 80px;
}

/* 步骤列表样式 */
.steps-section {
  padding: 10px 0;
}

.step-item {
  margin-bottom: 20px;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 8px;
}

.step-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.step-number {
  font-weight: bold;
  font-size: 14px;
  color: #409eff;
}

.step-description {
  margin-bottom: 10px;
}

.step-image-upload {
  display: flex;
  align-items: center;
  gap: 10px;
}

.step-image-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  overflow: hidden;
  transition: all 0.3s;
}

.step-image-uploader:hover {
  border-color: #409eff;
}

.step-uploaded-image {
  width: 120px;
  height: 90px;
  display: block;
}

.step-image-uploader-icon {
  font-size: 30px;
  color: #8c939d;
  width: 120px;
  height: 90px;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 详情页样式 */
.dish-detail {
  padding: 10px;
}

.detail-header {
  display: flex;
  gap: 30px;
}

.detail-image {
  width: 250px;
  height: 250px;
  border-radius: 12px;
  flex-shrink: 0;
}

.detail-info {
  flex: 1;
}

.detail-info h2 {
  margin: 0 0 15px 0;
  font-size: 28px;
  color: #303133;
}

.detail-category {
  font-size: 14px;
  margin-bottom: 15px;
}

.detail-description {
  color: #606266;
  line-height: 1.6;
  font-size: 15px;
}

.detail-section {
  margin: 20px 0;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 18px;
  color: #303133;
  margin-bottom: 15px;
}

.ingredients-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 12px;
}

.ingredient-card {
  padding: 12px 16px;
  background: #f4f4f5;
  border-radius: 6px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.ingredient-name {
  font-weight: 500;
  color: #303133;
}

.ingredient-amount {
  color: #909399;
  font-size: 14px;
}

.steps-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.step-card {
  display: flex;
  gap: 15px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 10px;
}

.step-number-badge {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 16px;
  flex-shrink: 0;
}

.step-content {
  flex: 1;
}

.step-desc {
  margin: 0 0 12px 0;
  color: #303133;
  line-height: 1.6;
  font-size: 15px;
}

.step-image {
  width: 300px;
  height: 200px;
  border-radius: 8px;
}
</style>
