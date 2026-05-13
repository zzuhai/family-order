const app = getApp()

Page({
  data: {
    orders: [],
    loading: true
  },

  onLoad() {
    this.loadOrders()
  },

  onShow() {
    this.loadOrders()
  },

  loadOrders() {
    this.setData({ loading: true })

    app.request({ url: '/orders/my' })
      .then((data) => {
        this.setData({ orders: data, loading: false })
      })
      .catch(() => {
        this.setData({ loading: false })
        wx.showToast({ title: '加载失败', icon: 'none' })
      })
  },

  goToDetail(e) {
    const orderId = e.currentTarget.dataset.orderId
    wx.navigateTo({
      url: `/pages/orderDetail/orderDetail?id=${orderId}`
    })
  },

  getStatusText(status) {
    const statusMap = {
      'submitted': '已提交',
      'preparing': '准备中',
      'completed': '已完成'
    }
    return statusMap[status] || '未知'
  }
})