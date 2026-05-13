import axios from 'axios'
import type { AxiosInstance, AxiosResponse, RawAxiosRequestConfig } from 'axios'
import { ElMessage } from 'element-plus'

interface CustomAxiosInstance extends AxiosInstance {
  get<T>(url: string, config?: RawAxiosRequestConfig): Promise<T>
  post<T>(url: string, data?: any, config?: RawAxiosRequestConfig): Promise<T>
  put<T>(url: string, data?: any, config?: RawAxiosRequestConfig): Promise<T>
  delete<T>(url: string, config?: RawAxiosRequestConfig): Promise<T>
}

const service = axios.create({
  baseURL: '/api',
  timeout: 10000
}) as CustomAxiosInstance

service.interceptors.response.use(
  (response: AxiosResponse) => {
    const { code, message, data } = response.data
    if (code === 200) {
      return data
    } else {
      ElMessage.error(message || '请求失败')
      return Promise.reject(new Error(message || '请求失败'))
    }
  },
  (error) => {
    ElMessage.error(error.message || '网络错误')
    return Promise.reject(error)
  }
)

export interface ApiResponse<T> {
  code: number
  message: string
  data: T
}

export default service