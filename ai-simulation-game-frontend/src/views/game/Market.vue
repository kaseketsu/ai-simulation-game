<template>
  <div class="min-h-screen bg-stone-900 text-stone-200 p-8">
    <div class="flex items-center justify-between mb-8 border-b border-stone-700 pb-4">
       <div class="flex items-center gap-4">
         <h1 class="text-3xl font-bold text-amber-500 font-serif">灵材采买</h1>
         <div class="bg-stone-800 px-3 py-1 rounded border border-stone-700 flex items-center gap-2">
            <span class="text-stone-400 text-sm">灵石:</span>
            <span class="text-amber-400 font-mono font-bold">{{ gameStore.player.spiritStones.toLocaleString() }}</span>
         </div>
       </div>
       <button @click="$router.back()" class="px-4 py-2 bg-stone-700 rounded hover:bg-stone-600 border border-stone-600 text-stone-300">返回</button>
    </div>

    <!-- Category Tags -->
    <div class="flex flex-wrap gap-2 mb-6">
      <button 
        v-for="cat in categories" 
        :key="cat.id"
        @click="handleCategoryChange(cat.id)"
        class="px-4 py-1.5 rounded-full text-sm transition-all duration-300 border"
        :class="currentCategory === cat.id 
          ? 'bg-amber-600 border-amber-500 text-white shadow-lg shadow-amber-900/50' 
          : 'bg-stone-800 border-stone-700 text-stone-400 hover:bg-stone-700 hover:text-stone-200'"
      >
        {{ cat.name }}
      </button>
    </div>

    <!-- Market Grid -->
    <div v-if="loading" class="flex justify-center items-center h-64">
        <div class="animate-spin text-4xl">☯️</div>
    </div>
    
    <div v-else-if="marketItems.length > 0" class="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-5 xl:grid-cols-6 gap-3">
      <div 
        v-for="(item, index) in marketItems" 
        :key="index"
        class="bg-stone-800 border border-stone-700 rounded-lg p-2 hover:border-amber-500/50 transition-colors group relative flex flex-col gap-2"
      >
        <!-- Header -->
        <div class="flex justify-between items-start">
            <div class="w-full">
                <h3 class="font-serif text-sm text-amber-100 truncate" :title="item.normalName">{{ item.normalName || '未知灵材' }}</h3>
                <span class="text-[10px] text-stone-500 bg-stone-900 px-1.5 py-0.5 rounded border border-stone-800 mt-1 inline-block">
                    {{ getCategoryName(item.type) }}
                </span>
            </div>
        </div>

        <!-- Image Area -->
        <div class="aspect-square bg-stone-900 rounded overflow-hidden relative group-hover:shadow-inner transition-all">
             <img v-if="item.normalUrl" :src="item.normalUrl" :alt="item.normalName" class="w-full h-full object-cover opacity-80 group-hover:opacity-100 transition-opacity">
             <div v-else class="w-full h-full flex items-center justify-center text-2xl text-stone-700">
                🛍️
             </div>
        </div>

        <!-- Purchase Options (Simplified to Normal Quality for now) -->
        <div class="mt-auto pt-2 border-t border-stone-700/50 flex flex-col gap-1">
            <div class="flex items-baseline justify-between">
                <span class="text-[10px] text-stone-500">凡品</span>
                <span class="text-amber-400 font-mono font-bold text-sm">{{ item.normalPrice || '???' }} <span class="text-[10px] text-amber-600">灵石</span></span>
            </div>
            <button class="w-full py-1 bg-stone-700 hover:bg-amber-700 text-stone-300 hover:text-white rounded border border-stone-600 hover:border-amber-500 transition-all text-xs">
                购买
            </button>
        </div>
      </div>
    </div>

    <!-- Empty State -->
    <div v-else class="text-center text-stone-500 mt-20">
       <div class="text-6xl mb-4 opacity-50">🛒</div>
       <p class="text-xl">
         {{ currentCategory === -1 ? '灵市暂无货物' : '该分类下暂无货物' }}
       </p>
       <p class="text-sm mt-2 opacity-60">请稍后再来...</p>
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
import { ref, onMounted } from 'vue'
import { useGameStore } from '@/stores/game'
import { queryBaseSpiritualMaterials } from '@/service/api/spiritualMarketController'

const gameStore = useGameStore()
const loading = ref(false)
const marketItems = ref<any[]>([])
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

function getCategoryName(type: number) {
  const cat = categories.find(c => c.id === type)
  return cat ? cat.name : '未知'
}

async function handleCategoryChange(id: number) {
    currentCategory.value = id
    currentPage.value = 1 // Reset to first page on category change
    await fetchMarketItems()
}

async function handlePageChange(page: number) {
    if (page < 1 || (page > 1 && (page - 1) * pageSize.value >= total.value)) return
    currentPage.value = page
    await fetchMarketItems()
}

async function fetchMarketItems() {
    loading.value = true
    try {
        const req: any = {
            currentPage: currentPage.value,
            pageSize: pageSize.value
        }
        
        // Map frontend "All" (-1) to backend "All" (6)
        req.type = currentCategory.value === -1 ? 6 : currentCategory.value

        const res = await queryBaseSpiritualMaterials(req)
        
        if (res.code === '990000' && res.data) {
            marketItems.value = res.data.records || []
            total.value = res.data.total || 0
        } else {
            marketItems.value = []
            total.value = 0
        }
    } catch (error) {
        console.error('Failed to fetch market items:', error)
        marketItems.value = []
        total.value = 0
    } finally {
        loading.value = false
    }
}

onMounted(() => {
    fetchMarketItems()
})
</script>