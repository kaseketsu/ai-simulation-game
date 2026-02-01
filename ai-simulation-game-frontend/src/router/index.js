import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/home/Landing.vue')
  },
  {
    path: '/auth/login',
    name: 'Login',
    component: () => import('../views/auth/Login.vue')
  },
  {
    path: '/auth/register',
    name: 'Register',
    component: () => import('../views/auth/Register.vue')
  },
  {
    path: '/game',
    children: [
      {
        path: 'start',
        name: 'GameStart',
        component: () => import('../views/game/Start.vue')
      },
      {
        path: 'main',
        name: 'GameMain',
        component: () => import('../views/game/Main.vue')
      },
      {
        path: 'market',
        name: 'GameMarket',
        component: () => import('../views/game/Market.vue')
      },
      {
        path: 'inventory',
        name: 'GameInventory',
        component: () => import('../views/game/Inventory.vue')
      },
      {
        path: 'kitchen',
        name: 'GameKitchen',
        component: () => import('../views/game/Kitchen.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
