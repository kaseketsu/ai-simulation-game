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
    
    <div v-else-if="marketItems.length > 0" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
      <div 
        v-for="(item, index) in marketItems" 
        :key="index"
        class="bg-stone-800 border border-stone-700 rounded-lg p-4 hover:border-amber-500/50 transition-colors group relative flex flex-col gap-4"
      >
        <!-- Header -->
        <div class="flex justify-between items-start">
            <div>
                <h3 class="font-serif text-lg text-amber-100">{{ item.normalName || '未知灵材' }}</h3>
                <span class="text-xs text-stone-500 bg-stone-900 px-2 py-0.5 rounded border border-stone-800">
                    {{ getCategoryName(item.type) }}
                </span>
            </div>
        </div>

        <!-- Image Area -->
        <div class="aspect-video bg-stone-900 rounded overflow-hidden relative group-hover:shadow-inner transition-all">
             <img v-if="item.normalUrl" :src="item.normalUrl" :alt="item.normalName" class="w-full h-full object-cover opacity-80 group-hover:opacity-100 transition-opacity">
             <div v-else class="w-full h-full flex items-center justify-center text-4xl text-stone-700">
                🛍️
             </div>
        </div>

        <!-- Purchase Options (Simplified to Normal Quality for now) -->
        <div class="mt-auto pt-4 border-t border-stone-700/50 flex items-center justify-between">
            <div class="flex flex-col">
                <span class="text-xs text-stone-500">凡品价格</span>
                <span class="text-amber-400 font-mono font-bold text-lg">{{ item.normalPrice || '???' }} <span class="text-xs text-amber-600">灵石</span></span>
            </div>
            <button class="px-3 py-1.5 bg-stone-700 hover:bg-amber-700 text-stone-300 hover:text-white rounded border border-stone-600 hover:border-amber-500 transition-all text-sm">
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
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useGameStore } from '@/stores/game'
import { queryBaseSpiritualMaterials } from '@/service/api/spiritualMarketController'

const gameStore = useGameStore()
const loading = ref(false)
const marketItems = ref<any[]>([])

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
    await fetchMarketItems()
}

async function fetchMarketItems() {
    loading.value = true
    try {
        const req: any = {
            currentPage: 1,
            pageSize: 50 // Fetch enough items
        }
        
        // Only add type if it's not "All" (-1)
        if (currentCategory.value !== -1) {
            req.type = currentCategory.value
        }

        const res = await queryBaseSpiritualMaterials(req)
        
        if (res.code === '990000' && res.data && res.data.records) {
            marketItems.value = res.data.records
        } else {
            marketItems.value = []
        }
    } catch (error) {
        console.error('Failed to fetch market items:', error)
        marketItems.value = []
    } finally {
        loading.value = false
    }
}

onMounted(() => {
    fetchMarketItems()
})
</script>