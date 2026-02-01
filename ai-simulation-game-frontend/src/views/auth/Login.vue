<template>
  <div class="flex items-center justify-center min-h-screen bg-stone-900 relative">
    <div class="absolute inset-0 opacity-10 bg-[radial-gradient(circle_at_top,_var(--tw-gradient-stops))] from-amber-800 to-transparent"></div>
    
    <div class="relative w-full max-w-md p-8 bg-stone-800/50 border border-stone-700 rounded-lg shadow-xl backdrop-blur-md">
      <h2 class="text-3xl font-bold text-center text-amber-500 mb-8 font-serif tracking-wide">食肆回归</h2>
      
      <form @submit.prevent="handleLogin" class="space-y-6">
        <div>
          <label class="block text-sm font-medium text-stone-400 mb-1">仙籍 (账号)</label>
          <input type="text" v-model="form.account" class="w-full bg-stone-900/50 border border-stone-600 rounded px-4 py-2 text-amber-100 focus:outline-none focus:border-amber-600 transition-colors" placeholder="手机号/邮箱" />
        </div>
        
        <div>
          <label class="block text-sm font-medium text-stone-400 mb-1">秘钥 (密码)</label>
          <div class="relative">
             <input 
               :type="showPassword ? 'text' : 'password'" 
               v-model="form.password" 
               class="w-full bg-stone-900/50 border border-stone-600 rounded px-4 py-2 text-amber-100 focus:outline-none focus:border-amber-600 transition-colors pr-10" 
               placeholder="请输入密码" 
             />
             <button 
               type="button" 
               @click="showPassword = !showPassword"
               class="absolute right-3 top-1/2 -translate-y-1/2 text-stone-400 hover:text-amber-500 transition-colors"
             >
               <img :src="showPassword ? '/src/assets/viewPassword.svg' : '/src/assets/hidePassword.svg'" class="w-5 h-5" alt="toggle password" />
             </button>
          </div>
        </div>
        
        <div class="flex items-center justify-between text-sm">
          <div class="flex items-center gap-2">
            <input type="checkbox" id="remember" v-model="form.remember" class="rounded border-stone-600 bg-stone-900 text-amber-600 focus:ring-amber-500/50" />
            <label for="remember" class="text-stone-500 cursor-pointer">铭刻神识 (记住我)</label>
          </div>
          <a href="#" class="text-amber-600 hover:text-amber-500">遗忘秘钥?</a>
        </div>
        
        <button type="submit" class="w-full py-3 bg-amber-700 hover:bg-amber-600 text-amber-50 font-bold rounded shadow-lg shadow-amber-900/20 transition-all transform hover:translate-y-[-1px]">
          归位 (登录)
        </button>
      </form>
      
      <div class="mt-6 text-center">
        <router-link to="/auth/register" class="text-sm text-amber-600 hover:text-amber-500 transition-colors">暂无传承？开宗立肆</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { userLogin } from '@/service/api/userController'
import { useToastStore } from '@/stores/toast'
import { useGameStore } from '@/stores/game'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const toast = useToastStore()
const gameStore = useGameStore()
const userStore = useUserStore()
const showPassword = ref(false)
const form = reactive({
  account: '',
  password: '',
  remember: false
})

const handleLogin = async () => {
  if (!form.account || !form.password) {
    toast.warning('请输入账号和密码')
    return
  }

  try {
    const res = await userLogin({
      userAccount: form.account,
      userPassword: form.password
    })

    if (res.code === '0' || res.data) {
       // Login success
       toast.success('归位成功，欢迎回来')
       
       // Update User Info in Store immediately
       await userStore.getLoginUser()
       
       // Check game progress
       const hasProgress = await gameStore.checkProgress()
       if (hasProgress) {
          router.push('/game/main')
       } else {
          router.push('/game/start')
       }
    } else {
       toast.error(res.message || '登录失败')
    }
  } catch (error) {
    console.error(error)
    toast.error('登录发生异常')
  }
}
</script>
