import { defineStore } from 'pinia'
import { ref, reactive, computed } from 'vue'
import { queryPlayProgress } from '@/service/api/playProgressController'
import { gameStart } from '@/service/api/gameEntranceController'
import { useUserStore } from './user'

// Define interfaces for state
interface PlayerStats {
  jianling: number
  biandao: number
  lianshan: number
}

interface Player {
  userId: number | null
  name: string
  rank: number
  spiritStones: number
  category: string
  region: string
  stats: PlayerStats
}

interface Log {
  time: string
  message: string
}

interface Customer {
  name: string
  title: string
  dialogue: string
}

export const useGameStore = defineStore('game', () => {
  const userStore = useUserStore()

  // Player Stats
  const player = reactive<Player>({
    userId: null,
    name: '散修',
    rank: 1, // 1-20
    spiritStones: 0,
    category: '', // solid | liquid
    region: '', // xuanzhou | canglan
    stats: {
      jianling: 0,
      biandao: 0,
      lianshan: 0
    }
  })

  // Time System
  const time = ref(8) // 0-23, default 8 (Chen Shi)

  const shichen = computed(() => {
    const t = time.value
    if (t >= 23 || t < 1) return '子时'
    if (t >= 1 && t < 3) return '丑时'
    if (t >= 3 && t < 5) return '寅时'
    if (t >= 5 && t < 7) return '卯时'
    if (t >= 7 && t < 9) return '辰时'
    if (t >= 9 && t < 11) return '巳时'
    if (t >= 11 && t < 13) return '午时'
    if (t >= 13 && t < 15) return '未时'
    if (t >= 15 && t < 17) return '申时'
    if (t >= 17 && t < 19) return '酉时'
    if (t >= 19 && t < 21) return '戌时'
    if (t >= 21 && t < 23) return '亥时'
    return '未知'
  })

  // Inventory
  const inventory = reactive<any[]>([])

  // Dishes (Menu)
  const menu = reactive<any[]>([])

  // Logs
  const logs = ref<Log[]>([])

  // Current Customer (AI Generated)
  const currentCustomer = ref<Customer | null>(null)

  function addLog(message: string) {
    logs.value.unshift({ time: shichen.value, message })
    if (logs.value.length > 50) logs.value.pop()
  }

  // Actions
  async function checkProgress() {
    try {
      // 1. Get User Info via UserStore
      // If user is not loaded, try to load it
      let userInfo = userStore.loginUser
      if (!userInfo.userId) {
         userInfo = await userStore.getLoginUser()
      }

      // Check again
      // Note: API definition has userId in LoginUserVO?
      // typings.d.ts says: LoginUserVO = { userName, userAccount, userAvatar, userState }
      // But user says: { userId: 7, ... }
      // We should use 'any' or extend the type if TS complains, but for now let's assume API definition is partial or we cast it.
      const uid = (userInfo as any)?.userId || (userInfo as any)?.id

      if (!uid) {
         throw new Error('无法获取用户信息')
      }
      player.userId = uid

      // 2. Query Progress
      const res = await queryPlayProgress({ userId: player.userId as number })

      // Check code 990000 or 0 (user said 990000 is success, but typings might vary. Let's assume 990000 based on user input)
      // Actually user said fetchLoginUser code is 990000. Let's assume all APIs use 990000.
      const code = res.code || (res.data && (res.data as any).code)
      const data = res.data && (res.data as any).data ? (res.data as any).data : res.data

      if (code === '990000' && data) {
        // Restore state
        player.name = data.userName || '散修'
        player.rank = data.storeLevel || 1
        player.spiritStones = data.earnedMoney || 0
        player.category = data.storeType || ''
        player.stats.jianling = data.sense || 0
        player.stats.biandao = data.speakingSkill || 0
        player.stats.lianshan = data.cookingSkill || 0

        return true // Has progress
      }

      return false // No progress
    } catch (error) {
      console.error('Check progress failed:', error)
      return false
    }
  }

  async function startGame(initData: { category: string, stats: PlayerStats }) {
    try {
      if (!player.userId) {
        const userInfo = await userStore.getLoginUser()
        const uid = (userInfo as any)?.userId || (userInfo as any)?.id
        if (uid) player.userId = uid
      }

      const res = await gameStart({
        userId: player.userId as number,
        userName: userStore.loginUser.userName || '散修',
        userRole: '0', // 0 - Normal User
        storeName: '云隐食肆',
        storeType: initData.category, // '1' (Solid) or '0' (Liquid)
        sense: initData.stats.jianling,
        speakingSkill: initData.stats.biandao,
        cookingSkill: initData.stats.lianshan
      })

      const code = res.code || (res.data && (res.data as any).code)
      const data = res.data && (res.data as any).data ? (res.data as any).data : res.data

      if (code === '990000' && data) {
        player.category = data.storeType
        player.spiritStones = data.earnedMoney || 0
        player.rank = data.storeLevel || 1
        player.stats.jianling = data.sense
        player.stats.biandao = data.speakingSkill
        player.stats.lianshan = data.cookingSkill

        addLog('食肆正式开张！')

        // Start simulation
        setTimeout(() => {
           generateCustomer()
        }, 1500)

        return true
      }
      return false
    } catch (error) {
      console.error('Start game failed:', error)
      throw error
    }
  }

  function generateCustomer() {
     currentCustomer.value = {
        name: '韩立',
        title: '散修 · 炼气期',
        dialogue: '掌柜的，今日可有上好的灵米粥？近来修行遇瓶颈，需固本培元。'
     }
     addLog(`散修 韩立 光顾了食肆。`)
  }

  return {
    player,
    inventory,
    menu,
    logs,
    currentCustomer,
    time,
    shichen,
    addLog,
    checkProgress,
    startGame,
    generateCustomer
  }
})
