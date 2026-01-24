<template>
  <aside
    class="h-screen bg-black/95 flex-shrink-0 overflow-hidden"
    :class="[isCollapsed ? 'w-10' : 'w-30', 'transition-[width] duration-400 ease-in-out']"
  >
    <!-- logo 区 -->
    <div class="flex items-center gap-1 ml-2 pt-2 h-6" id="logoArea">
      <img
        src="/favicon.png"
        alt="logo"
        class="rounded-md transition-all duration-300 ease-in-out"
        :class="isCollapsed ? 'opacity-0 w-0' : 'opacity-100 w-3'"
      />
      <div
        class="text-white font-serif text-xs transition-all duration-300 ease-in-out"
        :class="isCollapsed ? 'opacity-0 w-0' : 'opacity-100 w-auto'"
      >
        Flower
      </div>
      <img
        :src="isCollapsed ? '/opAs-w).svg' : '/clAs-w.svg'"
        alt="closeAside"
        class="h-2.5 color-white cursor-pointer opacity-80 hover:opacity-100 transition-all duration-300"
        :class="isCollapsed ? 'ml-auto mr-4' : 'ml-8'"
        @click="isCollapsed = !isCollapsed"
      />
    </div>
  </aside>
</template>

<script setup lang="ts">
import { type Ref, ref } from 'vue'
// 是否折叠
const isCollapsed: Ref<boolean> = ref(false)
/**
 * 修改 collapse 状态
 */
const changeCollapsed = () => {
  isCollapsed.value = !isCollapsed.value
}
</script>

<style scoped></style>
