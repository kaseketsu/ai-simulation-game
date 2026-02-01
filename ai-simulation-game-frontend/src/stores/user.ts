import { defineStore } from 'pinia'
import { ref } from 'vue'
import { fetchLoginUser } from '@/service/api/userController'

export const useUserStore = defineStore('user', () => {
  const loginUser = ref<API.LoginUserVO>({
    userName: '未登录',
    userAvatar: '',
    userState: 0
  })

  async function getLoginUser() {
    try {
      const res = await fetchLoginUser()
      // 兼容处理：检查 res.code 或 res.data.code
      const code = res.code || (res.data && res.data.code)
      const data = res.data && res.data.data ? res.data.data : res.data
      
      if (code === '990000' && data) {
        loginUser.value = data
        return data
      }
      return null
    } catch (error) {
      console.error('获取用户信息失败', error)
      return null
    }
  }

  return {
    loginUser,
    getLoginUser
  }
})
