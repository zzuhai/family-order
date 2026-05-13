const app = getApp()

Page({
  data: {
    loading: false
  },

  onLogin() {
    if (this.data.loading) return

    this.setData({
      loading: true
    })

    // 调用微信登录接口获取code
    wx.login({
      success: (res) => {
        if (res.code) {
          // 发送code到后端服务器换取token
          app.login(res.code)
            .then((data) => {
              this.setData({
                loading: false
              })

              // 显示登录成功提示
              wx.showToast({
                title: '登录成功',
                icon: 'success'
              })

              // 延迟跳转以让用户看到成功提示
              setTimeout(() => {
                // 登录成功后跳转到首页
                wx.switchTab({
                  url: '/pages/index/index'
                })
              }, 1500)
            })
            .catch((err) => {
              this.setData({
                loading: false
              })

              // 根据错误类型显示不同的提示信息
              let errorMsg = '登录失败，请重试'
              if (err.includes('网络')) {
                errorMsg = '网络连接失败，请检查网络设置'
              } else if (err.includes('服务器')) {
                errorMsg = '服务器异常，请稍后再试'
              }

              wx.showToast({
                title: errorMsg,
                icon: 'none',
                duration: 3000
              })
            })
        } else {
          this.setData({
            loading: false
          })
          wx.showToast({
            title: '登录失败，请重试',
            icon: 'none'
          })
        }
      },
      fail: () => {
        this.setData({
          loading: false
        })
        wx.showToast({
          title: '微信登录失败',
          icon: 'none'
        })
      }
    })
  },

  // 用户协议点击事件
  onAgreementTap() {
    wx.showToast({
      title: '用户协议功能待完善',
      icon: 'none'
    })
  },

  // 隐私政策点击事件
  onPrivacyTap() {
    wx.showToast({
      title: '隐私政策功能待完善',
      icon: 'none'
    })
  },

  // 页面加载时检查是否已登录
  onLoad() {
    const token = wx.getStorageSync('token')
    if (token) {
      // 如果已经登录，直接跳转到首页
      wx.switchTab({
        url: '/pages/index/index'
      })
    }
  }
})