<template>
  <!-- 导航栏 -->
  <nav class="relative">
    <!-- 顶部区域 -->
    <div class="absolute right-2">
      <span
        v-if="!isLogin"
        class="text-white/80 font-medium text-[0.6rem] tracking-widest hover:text-white/100 cursor-pointer"
        @click="handleOpen"
      >
        登录
      </span>
      <div v-else>
        <img :src="userLoginStore.user.userAvatar || 'public/ai-anime-girl.png'" alt="登录头像" class="rounded-full border-zinc-500 w-4 h-4 mt-1.5" />
      </div>
    </div>
    <!-- 登录弹窗 -->
    <LoginModal :visible="visible" @close="close" />
  </nav>
</template>

<script setup lang="ts">
import LoginModal from '@/components/LoginModal.vue'
import { onMounted, ref } from 'vue'
import { useLoginUserStore } from '@/stores/loginUserStore.ts'
// 弹窗是否可见
const visible = ref(false)
// 获取登录信息
const userLoginStore = useLoginUserStore()
// 开启弹窗
const handleOpen = () => {
  visible.value = true
}
// 关闭弹窗
const close = () => {
  visible.value = false
  // 关闭弹窗后再获取一次用户信息
  userLoginStore.fetchUserDetail()
  changeLogStatus()
}
// 用户登录态
const isLogin = ref(false)
// 变更登录台
const changeLogStatus = () => {
  if (userLoginStore.user.userAccount) {
    isLogin.value = true
  }
}
// 挂载时获取用户
onMounted(async () => {
  await userLoginStore.fetchUserDetail()
  changeLogStatus()
})
</script>

<style scoped></style>
