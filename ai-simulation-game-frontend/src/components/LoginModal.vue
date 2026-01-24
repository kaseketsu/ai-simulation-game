<template>
  <teleport to="body">
    <Transition
      name="modal"
      enter-active-class="transition-all duration-400 ease-in-out"
      leave-active-class="transition-all duration-400 ease-in-out"
      enter-from-class="opacity-0"
      leave-to-class="opacity-0"
    >
      <!-- 弹窗 + 遮罩 -->
      <div v-if="visible" class="flex fixed inset-0 items-center justify-center">
        <!-- 遮罩 -->
        <div class="bg-black/65 absolute inset-0" @click="handleClose" />
        <!-- 弹窗 -->
        <div
          class="flex w-70 h-40 rounded-lg overflow-hidden z-10 transition-all duration-400 ease-in-out"
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
          </div>
          <img src="/hd-anime-girl.png" alt="beautiful-anime-girl" />
        </div>
      </div>
    </Transition>
  </teleport>
</template>

<script setup lang="ts">
// 接收父组件传递变量
const props = defineProps({
  visible: {
    type: Boolean,
    default: false,
  },
})
// 接收父组件传递事件
const emit = defineEmits(['close'])
const handleClose = () => {
  emit('close')
}
</script>

<style scoped></style>
