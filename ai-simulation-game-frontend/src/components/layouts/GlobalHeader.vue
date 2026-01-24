<template>
  <!-- 导航栏 -->
  <nav class="relative mt-1.5">
    <!-- 顶部区域 -->
    <span
      class="text-white/80 absolute right-2 font-medium text-[0.6rem] tracking-widest hover:text-white/100 cursor-pointer"
      @click="handleOpen"
    >
      登录
    </span>
    <!-- 登录弹窗 -->
    <LoginModal :visible="visible" @close="close"/>
  </nav>
</template>

<script setup lang="ts">
import LoginModal from '@/components/LoginModal.vue'
import { ref } from 'vue'
// 弹窗是否可见
const visible = ref(false)
// 开启弹窗
const handleOpen = () => {
  visible.value = true
}
// 关闭弹窗
const close = () => {
  visible.value = false
}
</script>

<style scoped></style>
