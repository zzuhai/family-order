const app = getApp()

Page({
  data: {
    menus: [],
    loading: true,
    date: '',
    hasOrder: false
  },

  onLoad() {
    this.loadData()
  },

  onShow() {
    this.loadData()
  },

  loadData() {
    const today = new Date()
    const dateStr = `${today.getFullYear()}-${(today.getMonth() + 1).toString().padStart(2, '0')}-${today.getDate().toString().padStart(2, '0')}`
    this.setData({ date: dateStr, loading: true })

    app.request({ url: '/menus/today' })
      .then((data) => {
        const menus = data.map((item) => ({
          ...item,
          selected: false
        }))
        this.setData({ menus, loading: false })
      })
      .catch(() => {
        this.setData({ loading: false })
        wx.showToast({ title: '加载失败', icon: 'none' })
      })
  },

  toggleSelect(e) {
    const index = e.currentTarget.dataset.index
    const menus = this.data.menus
    menus[index].selected = !menus[index].selected
    this.setData({ menus })
  },

  submitOrder() {
    const selectedMenus = this.data.menus.filter(m => m.selected)
    if (selectedMenus.length === 0) {
      wx.showToast({ title: '请选择菜品', icon: 'none' })
      return
    }

    const menuIds = selectedMenus.map(m => m.id)

    wx.showLoading({ title: '提交中...' })

    app.request({
      url: '/orders',
      method: 'POST',
      data: {
        date: this.data.date,
        menuIds
      }
    })
      .then(() => {
        wx.hideLoading()
        wx.showToast({ title: '点餐成功', icon: 'success' })
        this.setData({ hasOrder: true })
      })
      .catch((err) => {
        wx.hideLoading()
        wx.showToast({ title: err || '提交失败', icon: 'none' })
      })
  },

  goToDetail(e) {
    const dishId = e.currentTarget.dataset.dishId
    wx.navigateTo({
      url: `/pages/order/order?id=${dishId}`
    })
  }
})