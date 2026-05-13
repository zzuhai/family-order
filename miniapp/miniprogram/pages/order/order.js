const app = getApp()

Page({
  data: {
    allDishes: [],           // 所有菜品
    categories: [],          // 分类列表
    selectedCategoryIndex: 0, // 当前选中的分类索引
    filteredDishes: [],      // 当前分类下的菜品
    selectedCount: 0,        // 已选菜品数量
    loading: true
  },

  onLoad() {
    this.loadDishes()
  },

  onShow() {
    this.loadDishes()
  },

  // 加载所有菜品
  loadDishes() {
    this.setData({ loading: true })

    // 加载完整菜品信息（包含图片）
    app.request({ url: '/dishes' })
      .then((dishes) => {
        // 为每个菜品添加selected属性
        const dishesWithSelect = dishes.map(dish => ({
          ...dish,
          selected: false
        }))

        // 提取所有不同的分类
        const categoriesSet = new Set()
        dishesWithSelect.forEach(dish => {
          if (dish.category) {
            categoriesSet.add(dish.category)
          }
        })

        const categories = ['全部', ...Array.from(categoriesSet)]

        this.setData({
          allDishes: dishesWithSelect,
          categories: categories,
          selectedCategoryIndex: 0,
          loading: false
        })

        // 初始显示全部菜品
        this.filterDishes()
      })
      .catch((err) => {
        console.error('加载菜品失败', err)
        this.setData({ loading: false })
        wx.showToast({ title: '加载菜品失败', icon: 'none' })
      })
  },

  // 选择分类
  selectCategory(e) {
    const index = e.currentTarget.dataset.index
    this.setData({
      selectedCategoryIndex: index
    })
    this.filterDishes()
  },

  // 根据分类过滤菜品
  filterDishes() {
    const { categories, selectedCategoryIndex, allDishes } = this.data
    const selectedCategory = categories[selectedCategoryIndex]

    let filtered = allDishes
    if (selectedCategory !== '全部') {
      filtered = allDishes.filter(dish => dish.category === selectedCategory)
    }

    this.setData({ filteredDishes: filtered })
  },

  // 切换菜品选中状态
  toggleSelect(e) {
    const dishId = e.currentTarget.dataset.dishId
    const { allDishes } = this.data

    // 更新allDishes中的选中状态
    const updatedDishes = allDishes.map(dish => {
      if (dish.id === dishId) {
        return { ...dish, selected: !dish.selected }
      }
      return dish
    })

    // 计算选中数量
    const selectedCount = updatedDishes.filter(dish => dish.selected).length

    this.setData({
      allDishes: updatedDishes,
      selectedCount: selectedCount
    })

    // 重新过滤显示
    this.filterDishes()
  },

  // 提交订单
  submitOrder() {
    const { allDishes } = this.data
    const selectedDishes = allDishes.filter(dish => dish.selected)

    if (selectedDishes.length === 0) {
      wx.showToast({ title: '请先选择菜品', icon: 'none' })
      return
    }

    const dishIds = selectedDishes.map(dish => dish.id)
    const today = new Date()
    const dateStr = `${today.getFullYear()}-${(today.getMonth() + 1).toString().padStart(2, '0')}-${today.getDate().toString().padStart(2, '0')}`

    wx.showLoading({ title: '提交中...' })

    app.request({
      url: '/orders',
      method: 'POST',
      data: {
        date: dateStr,
        dishIds: dishIds
      }
    })
      .then(() => {
        wx.hideLoading()
        wx.showToast({ title: '点餐成功', icon: 'success' })

        // 清空选择状态
        setTimeout(() => {
          const clearedDishes = allDishes.map(dish => ({ ...dish, selected: false }))
          this.setData({
            allDishes: clearedDishes,
            selectedCount: 0
          })
          this.filterDishes()
        }, 1500)
      })
      .catch((err) => {
        wx.hideLoading()
        wx.showToast({ title: err || '提交失败', icon: 'none' })
      })
  }
})