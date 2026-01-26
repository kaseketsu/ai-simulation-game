import { defineStore } from 'pinia'
import { ref } from 'vue'
import { fetchLoginUser } from '@/api/userController.ts'

/**
 * 全局用户状态管理钩子函数
 */
export const useLoginUserStore = defineStore('loginUserStore', () => {
  // 默认值
  const user = ref<any>({
    userName: '千早爱音',
    userAvatar: 'public/ai-anime-girl.png'
  })

  // 获取用户信息
  async function fetchUserDetail() {
    const res = await fetchLoginUser()
    if (res.data.code === '990000' && res.data.data) {
      user.value = res.data.data
    }
  }

  // 更新用户信息
  function setLoginUser(newLoginUser: any) {
    user.value = newLoginUser
  }

  return { user, setLoginUser, fetchUserDetail }
})
