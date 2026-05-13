import request from './request'
import type { DailyMenu, Order } from './types'

export function getTodayMenu() {
  return request.get<DailyMenu[]>('/menus/today')
}

export function getMenuByDate(date: string) {
  return request.get<DailyMenu[]>('/menus', { params: { date } })
}

export function publishMenu(date: string, dishIds: number[]) {
  return request.post('/menus', { date, dishIds })
}

export function getMyOrders() {
  return request.get<Order[]>('/orders/my')
}

export function getOrdersByDate(date: string) {
  return request.get<Order[]>('/orders', { params: { date } })
}

export function createOrder(date: string, menuIds: number[]) {
  return request.post<Order>('/orders', { date, menuIds })
}