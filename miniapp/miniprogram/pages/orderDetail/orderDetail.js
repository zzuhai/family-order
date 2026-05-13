const app = getApp()

Page({
  data: {
    orderId: null,
    order: null,
    statusText: '',
    loading: true
  },

  onLoad(options) {
    if (options.id) {
      this.setData({ orderId: options.id })
      this.loadOrderDetail()
    } else {
      wx.showToast({ title: '订单ID不存在', icon: 'none' })
      setTimeout(() => {
        wx.navigateBack()
      }, 1500)
    }
  },

  loadOrderDetail() {
    this.setData({ loading: true })

    app.request({ url: `/orders/${this.data.orderId}` })
      .then((order) => {
        const statusText = this.getStatusText(order.status)
        this.setData({
          order: order,
          statusText: statusText,
          loading: false
        })
      })
      .catch((err) => {
        console.error('加载订单详情失败', err)
        this.setData({ loading: false })
        wx.showToast({ title: '加载失败', icon: 'none' })
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
