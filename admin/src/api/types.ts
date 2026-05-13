export interface Dish {
  id: number
  name: string
  description: string
  imageUrl: string
  category: string
  status: number
  ingredients: DishIngredient[]
  steps: DishStep[]
}

export interface DishIngredient {
  id?: number
  dishId?: number
  name: string
  amount: string
  unit: string
}

export interface DishStep {
  id?: number
  dishId?: number
  stepNumber: number
  description: string
  imageUrl?: string
}

export interface DailyMenu {
  id: number
  menuDate: string
  dishId: number
  dishName: string
  dishDescription: string
  dishImageUrl: string
}

export interface Order {
  id: number
  memberId: number
  memberName: string
  memberAvatar: string
  menuDate: string
  status: string
  createdAt: string
  items: OrderItem[]
}

export interface OrderItem {
  id: number
  dishId: number
  dishName: string
  dishImageUrl: string
}

export interface Member {
  id: number
  openid: string
  nickname: string
  avatar: string
  role: string
  createdAt: string
}