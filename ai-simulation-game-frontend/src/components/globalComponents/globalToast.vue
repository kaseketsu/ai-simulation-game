<template>
  <teleport to="body">
    <Transition
      name="toast"
      enter-active-class="transition-all duration-300 ease-in-out"
      leave-active-class="transition-all duration-300 ease-in-out"
      enter-from-class="opacity-0 translate-y-4"
      leave-to-class="opacity-0 translate-y-4"
    >
      <div
        v-if="visible"
        class="fixed top-4 z-50 flex items-center gap-2 px-3 py-2 rounded-lg bg-zinc-900/95 backdrop-blur-sm border border-zinc-500 shadow-lg"
        :class="typeClass"
      >
        <!-- 图标 -->
        <span class="text-[0.55rem]">
          {{ type === 'error' ? '❌' : type === 'success' ? '✅' : '⚠️' }}
        </span>
        <!-- 提示文字 -->
        <span class="text-[0.45rem] text-white">{{ message }}</span>
      </div>
    </Transition>
  </teleport>
</template>

<script setup lang="ts">
import { ref, watch, onMounted, onUnmounted } from 'vue'

// 定义props
const props = defineProps({
  visible: {
    type: Boolean,
    default: false,
  },
  message: {
    type: String,
    required: true,
  },
  type: {
    type: String,
    default: 'error', // error/success/warning
    validator: (v: string) => ['error', 'success', 'warning'].includes(v),
  },
  duration: {
    type: Number,
    default: 3000, // 自动关闭时间（毫秒）
  },
})

// 定义emit
const emit = defineEmits(['close'])

// 类型对应的样式类
const typeClass = ref('')
watch(
  () => props.type,
  (val) => {
    switch (val) {
      case 'error':
        typeClass.value = 'border-l-2 border-pink-500'; // 粉色边框（呼应渐变的pink）
        break;
      case 'success':
        typeClass.value = 'border-l-2 border-green-500';
        break;
      case 'warning':
        typeClass.value = 'border-l-2 border-yellow-500';
        break;
    }
  },
  { immediate: true }
)

// 自动关闭定时器
let timer = null

// 关闭提示
const close = () => {
  if (timer) clearTimeout(timer)
  emit('close')
}

// 监听visible，自动关闭
watch(
  () => props.visible,
  (val) => {
    if (val && props.duration > 0) {
      timer = setTimeout(close, props.duration)
    } else if (!val && timer) {
      clearTimeout(timer)
    }
  },
  { immediate: true }
)

// 组件卸载时清除定时器
onUnmounted(() => {
  if (timer) clearTimeout(timer)
})
</script>
