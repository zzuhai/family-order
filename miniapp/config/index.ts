// 开发环境配置
const config = {
  // 开发环境
  dev: {
    baseUrl: 'http://localhost:8080/api'
  },
  // 生产环境
  prod: {
    baseUrl: 'https://your-domain.com/api' // 请替换为实际的生产环境地址
  }
}

// 获取当前环境配置
const env = process.env.NODE_ENV === 'production' ? 'prod' : 'dev'
module.exports = config[env]