// app.js
App({
  globalData: {
    env: 'your-env-id',
    userInfo: null,
    baseUrl: 'https://www.zzuhai.xyz/api'
  },

  onLaunch() {
    // 初始化云开发
    if (!wx.cloud) {
      console.log('请使用 2.2.3 或以上的基础库以使用云能力')
    } else {
      wx.cloud.init({
        env: this.globalData.env,
        traceUser: true
      })
    }
  },

  // 封装请求方法
  request(options) {
    return new Promise((resolve, reject) => {
      wx.request({
        ...options,
        url: this.globalData.baseUrl + options.url,
        header: options.header || {},
        timeout: 10000,
        success: (res) => {
          if (res.statusCode === 200) {
            if (res.data.code === 200) {
              resolve(res.data.data)
            } else {
              reject(res.data.message || '请求失败')
            }
          } else {
            let errorMsg = '请求失败'
            if (res.statusCode >= 500) {
              errorMsg = '服务器内部错误'
            } else if (res.statusCode >= 400) {
              errorMsg = '请求参数错误'
            }
            reject(errorMsg)
          }
        },
        fail: (err) => {
          let errorMsg = '网络连接失败，请检查网络设置'
          if (err.errMsg && err.errMsg.includes('timeout')) {
            errorMsg = '请求超时，请稍后再试'
          }
          reject(errorMsg)
        }
      })
    })
  }
})
