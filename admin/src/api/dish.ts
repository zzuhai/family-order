import request from './request'
import type { Dish } from './types'

export interface DishQuery {
  name?: string
  category?: string
  status?: number
}

export function getDishes(params?: DishQuery) {
  return request.get<Dish[]>('/dishes', { params })
}

export function getDish(id: number) {
  return request.get<Dish>(`/dishes/${id}`)
}

export function createDish(data: Partial<Dish>) {
  return request.post<Dish>('/dishes', data)
}

export function updateDish(id: number, data: Partial<Dish>) {
  return request.put<Dish>(`/dishes/${id}`, data)
}

export function deleteDish(id: number) {
  return request.delete(`/dishes/${id}`)
}

export function deleteDishes(ids: number[]) {
  return request.delete('/dishes/batch', { data: ids })
}