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
    <div v-if="loading" class="flex justify-center items-center h-64">
        <div class="animate-spin text-4xl">☯️</div>
    </div>

    <div v-else-if="filteredInventory.length > 0" class="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-4">
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
         <p class="text-xs text-stone-500 mt-1">
             <span v-if="item.rarity === 1">普通</span>
             <span v-else-if="item.rarity === 2" class="text-blue-400">稀有</span>
             <span v-else-if="item.rarity === 3" class="text-purple-400">传世</span>
             <span v-else-if="item.rarity === 4" class="text-orange-400">神话</span>
             <span v-else>未知</span>
         </p>
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

    <!-- Pagination -->
    <div v-if="total > 0" class="flex justify-center items-center gap-4 mt-8 pb-8">
        <button 
            @click="handlePageChange(currentPage - 1)"
            :disabled="currentPage === 1"
            class="px-3 py-1 bg-stone-800 border border-stone-700 rounded text-stone-400 hover:text-amber-400 hover:border-amber-500 disabled:opacity-50 disabled:cursor-not-allowed transition-all"
        >
            上一页
        </button>
        <span class="text-stone-500 text-sm">
            第 <span class="text-amber-500 font-bold">{{ currentPage }}</span> / {{ Math.ceil(total / pageSize) || 1 }} 页
        </span>
        <button 
            @click="handlePageChange(currentPage + 1)"
            :disabled="currentPage * pageSize >= total"
            class="px-3 py-1 bg-stone-800 border border-stone-700 rounded text-stone-400 hover:text-amber-400 hover:border-amber-500 disabled:opacity-50 disabled:cursor-not-allowed transition-all"
        >
            下一页
        </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useGameStore } from '@/stores/game'
import { listSpiritualRepoByPage } from '@/service/api/playProgressController'

const gameStore = useGameStore()
const loading = ref(false)
const inventoryItems = ref<any[]>([])
const currentPage = ref(1)
const pageSize = ref(50)
const total = ref(0)

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

// Note: API does not support filtering by type yet, so we only filter locally on the current page
// or we just show all items if category filtering is not possible.
// For now, let's just return all items if type information is missing in response.
const filteredInventory = computed(() => {
  const items = inventoryItems.value || []
  if (currentCategory.value === -1) {
    return items
  }
  // Try to filter if item has type property (it might not)
  return items.filter((item: any) => {
      if (item.type !== undefined) {
          return item.type === currentCategory.value
      }
      return true // If no type info, show it anyway to avoid empty list
  })
})

async function handlePageChange(page: number) {
    if (page < 1 || (page > 1 && (page - 1) * pageSize.value >= total.value)) return
    currentPage.value = page
    await fetchInventory()
}

async function fetchInventory() {
    if (!gameStore.player.userId) return
    
    loading.value = true
    try {
        const res = await listSpiritualRepoByPage({
            userId: gameStore.player.userId,
            currentPage: currentPage.value,
            pageSize: pageSize.value
        })
        
        if (res.code === '990000' && res.data) {
            inventoryItems.value = res.data.records || []
            total.value = res.data.total || 0
        } else {
            inventoryItems.value = []
            total.value = 0
        }
    } catch (error) {
        console.error('Failed to fetch inventory:', error)
        inventoryItems.value = []
        total.value = 0
    } finally {
        loading.value = false
    }
}

onMounted(() => {
    fetchInventory()
})
</script>
