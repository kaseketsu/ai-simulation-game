<template>
  <div class="min-h-screen bg-stone-900 text-stone-200 p-8">
    <div class="flex items-center justify-between mb-8 border-b border-stone-700 pb-4">
       <h1 class="text-3xl font-bold text-amber-500 font-serif">灵材库</h1>
       <button @click="$router.back()" class="px-4 py-2 bg-stone-700 rounded hover:bg-stone-600 border border-stone-600 text-stone-300">返回</button>
    </div>

    <!-- Category Tags -->
    <div class="flex flex-wrap gap-2 mb-6">
      <button 
        v-for="cat in categories" 
        :key="cat.id"
        @click="currentCategory = cat.id"
        class="px-4 py-1.5 rounded-full text-sm transition-all duration-300 border"
        :class="currentCategory === cat.id 
          ? 'bg-amber-600 border-amber-500 text-white shadow-lg shadow-amber-900/50' 
          : 'bg-stone-800 border-stone-700 text-stone-400 hover:bg-stone-700 hover:text-stone-200'"
      >
        {{ cat.name }}
      </button>
    </div>

    <!-- Inventory Grid -->
    <div v-if="filteredInventory.length > 0" class="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-4">
      <div 
        v-for="item in filteredInventory" 
        :key="item.id"
        class="bg-stone-800 border border-stone-700 rounded-lg p-3 hover:border-amber-500/50 transition-colors group relative"
      >
        <!-- Rarity Indicator (Border/Glow) -->
        <div class="aspect-square bg-stone-900 rounded mb-2 overflow-hidden relative">
             <img v-if="item.url" :src="item.url" :alt="item.name" class="w-full h-full object-cover">
             <div v-else class="w-full h-full flex items-center justify-center text-4xl bg-stone-800">
                📦
             </div>
             <div class="absolute top-1 right-1 bg-black/60 text-xs px-1.5 rounded text-white">
                x{{ item.count }}
             </div>
        </div>
        <h3 class="font-serif text-amber-100 truncate">{{ item.name }}</h3>
        <p class="text-xs text-stone-500 mt-1">{{ getCategoryName(item.type) }}</p>
      </div>
    </div>

    <!-- Empty State -->
    <div v-else class="text-center text-stone-500 mt-20">
       <div class="text-6xl mb-4 opacity-50">📦</div>
       <p class="text-xl">
         {{ currentCategory === -1 ? '库房空空如也' : '该分类下暂无灵材' }}
       </p>
       <p v-if="currentCategory === -1" class="text-sm mt-2 opacity-60">快去灵市采买些灵材吧...</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useGameStore } from '@/stores/game'

const gameStore = useGameStore()

const categories = [
  { id: -1, name: '全部' },
  { id: 0, name: '灵谷' },
  { id: 1, name: '灵蔬' },
  { id: 2, name: '灵肉' },
  { id: 3, name: '灵茶' },
  { id: 4, name: '灵果' },
  { id: 5, name: '灵酿' }
]

const currentCategory = ref(-1)

const filteredInventory = computed(() => {
  const items = gameStore.inventory || []
  if (currentCategory.value === -1) {
    return items
  }
  return items.filter((item: any) => item.type === currentCategory.value)
})

function getCategoryName(type: number) {
  const cat = categories.find(c => c.id === type)
  return cat ? cat.name : '未知'
}
</script>
