<template>
  <div class="fixed top-4 left-1/2 -translate-x-1/2 z-50 flex flex-col items-center gap-3 pointer-events-none w-full max-w-md px-4">
    <TransitionGroup name="toast">
      <div 
        v-for="toast in store.toasts" 
        :key="toast.id"
        class="pointer-events-auto flex items-start gap-3 px-6 py-4 rounded-lg shadow-2xl backdrop-blur-md border min-w-[300px] max-w-full transition-all duration-300 relative overflow-hidden group"
        :class="getTypeClasses(toast.type)"
      >
        <!-- Icon based on type -->
        <div class="flex-shrink-0 mt-0.5">
          <span v-if="toast.type === 'success'" class="text-lg">✨</span>
          <span v-else-if="toast.type === 'error'" class="text-lg">💥</span>
          <span v-else-if="toast.type === 'warning'" class="text-lg">⚡</span>
          <span v-else class="text-lg">📜</span>
        </div>

        <!-- Message -->
        <div class="flex-1 text-sm font-medium tracking-wide leading-relaxed font-serif pt-0.5">
          {{ toast.message }}
        </div>

        <!-- Copy Button (Only for Error) -->
        <button 
          v-if="toast.type === 'error'"
          @click="copyToClipboard(toast.message)"
          class="flex-shrink-0 text-stone-400 hover:text-stone-200 transition-colors p-1 rounded hover:bg-white/10"
          title="复制错误信息"
        >
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-copy"><rect width="14" height="14" x="8" y="8" rx="2" ry="2"/><path d="M4 16c-1.1 0-2-.9-2-2V4c0-1.1.9-2 2-2h10c1.1 0 2 .9 2 2"/></svg>
        </button>

        <!-- Close Button (Optional, maybe for long duration) -->
        <button 
          @click="store.remove(toast.id)"
          class="flex-shrink-0 text-stone-400 hover:text-stone-200 transition-colors p-1 rounded hover:bg-white/10 opacity-0 group-hover:opacity-100"
        >
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M18 6 6 18"/><path d="m6 6 12 12"/></svg>
        </button>

        <!-- Progress bar (optional visual flair) -->
        <div class="absolute bottom-0 left-0 h-0.5 bg-current opacity-20 w-full animate-shrink" :style="{ animationDuration: '2000ms' }"></div>
      </div>
    </TransitionGroup>
  </div>
</template>

<script setup>
import { useToastStore } from '@/stores/toast'

const store = useToastStore()

const getTypeClasses = (type) => {
  switch (type) {
    case 'success':
      return 'bg-stone-900/90 border-amber-600/50 text-amber-100 shadow-amber-900/20'
    case 'error':
      return 'bg-stone-900/90 border-red-800/50 text-red-100 shadow-red-900/20'
    case 'warning':
      return 'bg-stone-900/90 border-orange-700/50 text-orange-100 shadow-orange-900/20'
    default:
      return 'bg-stone-900/90 border-stone-600/50 text-stone-200 shadow-stone-900/20'
  }
}

const copyToClipboard = async (text) => {
  try {
    await navigator.clipboard.writeText(text)
    store.success('已复制错误信息')
  } catch (err) {
    console.error('Failed to copy', err)
  }
}
</script>

<style scoped>
.toast-enter-active,
.toast-leave-active {
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}

.toast-enter-from {
  opacity: 0;
  transform: translateY(-20px) scale(0.9);
}

.toast-leave-to {
  opacity: 0;
  transform: translateY(-20px) scale(0.9);
}

@keyframes shrink {
  from { width: 100%; }
  to { width: 0%; }
}

.animate-shrink {
  animation-name: shrink;
  animation-timing-function: linear;
  animation-fill-mode: forwards;
}
</style>
