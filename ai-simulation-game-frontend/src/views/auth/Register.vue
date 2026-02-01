<template>
  <div class="flex items-center justify-center min-h-screen bg-stone-900 relative">
    <div class="absolute inset-0 opacity-10 bg-[radial-gradient(circle_at_top,_var(--tw-gradient-stops))] from-amber-800 to-transparent"></div>
    
    <div class="relative w-full max-w-md p-8 bg-stone-800/50 border border-stone-700 rounded-lg shadow-xl backdrop-blur-md">
      <h2 class="text-3xl font-bold text-center text-amber-500 mb-8 font-serif tracking-wide">开宗立肆</h2>
      
      <form @submit.prevent="handleRegister" class="space-y-6">
        <div>
          <label class="block text-sm font-medium text-stone-400 mb-1">仙籍传讯 (手机/邮箱)</label>
          <input type="text" v-model="form.account" class="w-full bg-stone-900/50 border border-stone-600 rounded px-4 py-2 text-amber-100 focus:outline-none focus:border-amber-600 transition-colors" placeholder="请输入手机号或邮箱" />
          <p v-if="errors.account" class="text-red-500 text-xs mt-1">{{ errors.account }}</p>
        </div>
        
        <div>
          <label class="block text-sm font-medium text-stone-400 mb-1">秘钥 (密码)</label>
          <div class="relative">
            <input 
              :type="showPassword ? 'text' : 'password'" 
              v-model="form.password" 
              class="w-full bg-stone-900/50 border border-stone-600 rounded px-4 py-2 text-amber-100 focus:outline-none focus:border-amber-600 transition-colors pr-10" 
              placeholder="至少8位，含字母数字" 
            />
            <button 
              type="button" 
              @click="showPassword = !showPassword"
              class="absolute right-3 top-1/2 -translate-y-1/2 text-stone-400 hover:text-amber-500 transition-colors"
            >
              <img :src="showPassword ? '/src/assets/viewPassword.svg' : '/src/assets/hidePassword.svg'" class="w-5 h-5" alt="toggle password" />
            </button>
          </div>
          <p v-if="errors.password" class="text-red-500 text-xs mt-1">{{ errors.password }}</p>
        </div>
        
        <div class="flex gap-4">
          <div class="flex-1">
            <label class="block text-sm font-medium text-stone-400 mb-1">灵识印记 (验证码)</label>
            <input type="text" v-model="form.code" class="w-full bg-stone-900/50 border border-stone-600 rounded px-4 py-2 text-amber-100 focus:outline-none focus:border-amber-600 transition-colors" placeholder="验证码" />
          </div>
          <div class="flex items-end">
            <button type="button" class="px-4 py-2 bg-stone-700 text-stone-300 rounded border border-stone-600 hover:bg-stone-600 text-sm h-[42px] whitespace-nowrap">获取印记</button>
          </div>
        </div>
        
        <div class="flex items-center gap-2">
          <input type="checkbox" id="agree" v-model="form.agree" class="rounded border-stone-600 bg-stone-900 text-amber-600 focus:ring-amber-500/50" />
          <label for="agree" class="text-xs text-stone-500 cursor-pointer">我已阅读并同意《修仙界食肆律法》及《散修隐私密卷》</label>
        </div>
        
        <button type="submit" class="w-full py-3 bg-amber-700 hover:bg-amber-600 text-amber-50 font-bold rounded shadow-lg shadow-amber-900/20 transition-all transform hover:translate-y-[-1px]">
          立肆 (注册)
        </button>
      </form>
      
      <div class="mt-6 text-center">
        <router-link to="/auth/login" class="text-sm text-amber-600 hover:text-amber-500 transition-colors">已有传承？返回登录</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { userRegister } from '@/service/api/userController'
import { useToastStore } from '@/stores/toast'

const router = useRouter()
const toast = useToastStore()
const showPassword = ref(false)

const form = reactive({
  account: '',
  password: '',
  code: '',
  agree: false
})

const errors = reactive({
  account: '',
  password: ''
})

const validate = () => {
  let isValid = true
  errors.account = ''
  errors.password = ''

  if (!form.account) {
    errors.account = '请输入手机号或邮箱'
    isValid = false
  } else if (form.account.length < 4) {
     errors.account = '账号长度过短'
     isValid = false
  }

  if (!form.password) {
    errors.password = '请输入密码'
    isValid = false
  } else {
    if (form.password.length < 8) {
      errors.password = '密码长度至少8位'
      isValid = false
    }
    if (!/[A-Za-z]/.test(form.password) || !/[0-9]/.test(form.password)) {
      errors.password = '密码需包含字母和数字'
      isValid = false
    }
  }

  return isValid
}

const handleRegister = async () => {
  if (!form.agree) {
    toast.warning('请先同意相关律法')
    return
  }
  
  if (!validate()) return

  try {
    const res = await userRegister({
      userAccount: form.account,
      userPassword: form.password,
      checkPassword: form.password, // Confirm password same as password for now
      userName: form.account // Default username to account
    })
    
    if (res.code === '0' || res.data) {
       // Registration success
       toast.success('开宗立肆成功，请前往登录')
       router.push('/auth/login')
    } else {
       toast.error(res.message || '注册失败')
    }
  } catch (error) {
    console.error(error)
    toast.error('注册发生异常')
  }
}
</script>
