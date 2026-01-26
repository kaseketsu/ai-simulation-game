<template>
  <teleport to="body">
    <Transition
      name="modal"
      enter-active-class="transition-all duration-400 ease-in-out"
      leave-active-class="transition-all duration-400 ease-in-out"
      enter-from-class="opacity-0 translate-y-4"
      leave-to-class="opacity-0 translate-y-4"
    >
      <!-- 弹窗 + 遮罩 -->
      <div v-if="visible" class="flex fixed inset-0 items-center justify-center">
        <!-- 遮罩 -->
        <div class="bg-black/65 absolute inset-0" @click="handleClose" />
        <!-- 弹窗 -->
        <div
          class="flex w-75 h-45 rounded-lg overflow-hidden z-10 transition-all duration-400 ease-in-out"
          :class="visible ? 'scale-100 opacity-100' : 'scale-90 opacity-0'"
        >
          <div class="w-30 bg-zinc-800 justify-center">
            <!-- header -->
            <div class="text-white mt-2 text-[0.6rem] items-center text-center">登录 / 注册</div>
            <!-- desc -->
            <div
              class="mt-1.5 bg-gradient-to-r from-blue-300 via-purple-300 to-pink-300 bg-clip-text text-transparent font-medium text-[0.45rem] text-center"
            >
              ✨尽情享受模拟经营的乐趣吧！
            </div>
            <!-- 登录相关 -->
            <div class="flex-1 flex flex-col items-center mt-2 gap-1.5">
              <input
                type="text"
                placeholder="账号 / 邮箱"
                v-model="formData.userAccount"
                class="w-25 py-1.5 px-2 bg-zinc-900 border border-zinc-700 rounded text-[0.45rem] text-white placeholder:text-zinc-500 focus:outline-none focus:border-blue-500 transition-colors duration-200 ease-in-out h-4.5"
              />
              <input
                type="password"
                placeholder="密码"
                v-model="formData.password"
                class="w-25 py-1.5 px-2 bg-zinc-900 text-white border border-zinc-700 rounded text-[0.45rem] placeholder:text-zinc-500 focus:outline-none focus:border-blue-500 transition-colors duration-200 ease-in-out h-4.5"
              />
              <input
                v-show="!isLoginModal"
                type="password"
                v-model="formData.passwordConfirm"
                placeholder="确认密码"
                class="w-25 py-1.5 px-2 bg-zinc-900 text-white border border-zinc-700 rounded text-[0.45rem] placeholder:text-zinc-500 focus:outline-none focus:border-blue-500 transition-colors duration-200 ease-in-out h-4.5"
              />
              <button
                class="w-15 rounded bg-gradient-to-r from-blue-300 via-purple-300 to-pink-300 px-2 h-4.5 text-white text-[0.45rem] mt-1.5 hover:opacity-90 transition-all duration-200 ease-in-out hover:cursor-pointer disabled:cursor-not-allowed"
                :disabled="isLoading"
                @click="handleRegiOrLogin"
              >
                {{ isLoginModal ? '登录' : '注册' }}
              </button>
              <div class="flex text-center gap-1 mt-2">
                <span class="text-[0.45rem] text-zinc-500">
                  {{ isLoginModal ? '尚无帐号?' : '已有账号? ' }}
                </span>
                <span
                  class="text-serif text-blue-400 hover:text-blue-300 hover:cursor-pointer disabled:cursor-not-allowed transition-colors text-[0.45rem]"
                  @click="toggleLoginModal"
                >
                  {{ isLoginModal ? '去注册' : '去登录' }}
                </span>
              </div>
            </div>
          </div>
          <img src="/hd-anime-girl.png" alt="beautiful-anime-girl" />
        </div>
      </div>
    </Transition>
  </teleport>
</template>

<script setup lang="ts">
// 接收父组件传递变量
import { reactive, ref } from 'vue'
import toast from '@/utils/toastUtil.ts'
import { userLogin } from '@/api/userController.ts'

const props = defineProps({
  visible: {
    type: Boolean,
    default: false,
  },
})
// 是否加载中
const isLoading = ref(false)
// 接收父组件传递事件
const emit = defineEmits(['close'])
const handleClose = () => {
  formData.userAccount = ''
  formData.password = ''
  formData.passwordConfirm = ''
  emit('close')
}
// 登录 modal 属性
const isLoginModal = ref(true)
// 定义表单值
const formData = reactive({
  userAccount: '',
  password: '',
  passwordConfirm: '',
})
// 切换登录 / 注册
const toggleLoginModal = () => {
  // 如果当前正在 loading，不允许用户切换
  if (isLoading.value) {
    return
  }
  isLoginModal.value = !isLoginModal.value
  formData.userAccount = ''
  formData.password = ''
  formData.passwordConfirm = ''
}
// 处理注册 / 登录
const handleRegiOrLogin = async () => {
  // 参数校验
  if (!formData.userAccount || !formData.password) {
    toast.error('账号或密码不能为空')
    return
  }
  isLoading.value = true
  try {
    console.log('请求参数：', {
      userAccount: formData.userAccount,
      userPassword: formData.password
    })
    // 调用后端接口
    if (isLoginModal.value) {
      // 请求后端登录接口
      const res = await userLogin({
        userAccount: formData.userAccount,
        userPassword: formData.password,
      })
      console.log(res.data)
      // 判断是否成功
      if (res.data.code === '990000') {
        toast.success('登录成功')
        handleClose()
      } else {
        toast.error('登录失败')
      }
    }
  } catch (e) {
    toast.error('登录失败, 原因是: ' + e.message)
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped></style>
