<template>
  <div class="min-h-screen bg-stone-900 text-stone-200 font-serif flex flex-col">
    <!-- Top Bar -->
    <header class="h-16 bg-stone-800 border-b border-stone-700 flex items-center justify-between px-6 shadow-md z-20">
      <div class="flex items-center gap-4">
        <div class="text-xl font-bold text-amber-500 tracking-wider">云隐食肆</div>
        <div class="px-2 py-0.5 bg-amber-900/50 text-amber-200 text-xs rounded border border-amber-700">{{ rankName }}</div>
      </div>
      
      <div class="flex items-center gap-6">
        <div class="flex items-center gap-2 bg-stone-900/50 px-3 py-1 rounded border border-stone-700">
           <span class="text-stone-400 text-sm">时辰:</span>
           <span class="text-amber-400 font-bold text-lg">{{ shichen }}</span>
        </div>
        <div class="flex items-center gap-2 bg-stone-900/50 px-3 py-1 rounded border border-stone-700">
           <span class="text-stone-400 text-sm">灵石:</span>
           <span class="text-amber-400 font-bold font-mono text-lg">{{ formattedMoney }}</span>
        </div>
        <button 
          @click="showSettings = !showSettings"
          class="text-stone-400 hover:text-stone-200 transition-colors cursor-pointer relative"
        >
           <span class="sr-only">设置</span>
           <span class="text-xl">⚙️</span>
        </button>
      </div>
    </header>

    <!-- Settings Modal -->
    <div v-if="showSettings" class="absolute top-16 right-6 z-50 w-48 bg-stone-800 border border-stone-600 rounded-lg shadow-xl animate-fade-in-down">
      <div class="p-2 space-y-1">
        <button class="w-full text-left px-4 py-2 text-sm text-stone-300 hover:bg-stone-700 hover:text-amber-400 rounded cursor-pointer transition-colors flex items-center gap-2">
          <span>🔊</span> 音律设置
        </button>
        <button class="w-full text-left px-4 py-2 text-sm text-stone-300 hover:bg-stone-700 hover:text-amber-400 rounded cursor-pointer transition-colors flex items-center gap-2">
          <span>🎨</span> 界面设置
        </button>
        <div class="h-px bg-stone-700 my-1"></div>
        <button 
          @click="handleLogout"
          class="w-full text-left px-4 py-2 text-sm text-red-400 hover:bg-stone-700 hover:text-red-300 rounded cursor-pointer transition-colors flex items-center gap-2"
        >
          <span>🚪</span> 退出登录
        </button>
      </div>
    </div>

    <!-- Main Content Area -->
    <main class="flex-1 flex overflow-hidden relative">
       <!-- Background Image -->
       <div class="absolute inset-0 bg-[radial-gradient(circle_at_center,_var(--tw-gradient-stops))] from-stone-800 via-stone-900 to-black opacity-80 pointer-events-none"></div>
       
       <!-- Left Panel: Logs & System Info -->
       <div class="w-1/3 max-w-sm bg-stone-900/80 backdrop-blur-sm border-r border-stone-700 flex flex-col p-4 z-10 shadow-lg">
          <h3 class="text-amber-500 font-bold mb-4 flex items-center gap-2 border-b border-stone-700 pb-2">
             <span>📜</span> 接膳日志
          </h3>
          <div class="flex-1 overflow-y-auto space-y-3 pr-2 text-sm text-stone-400 custom-scrollbar">
             <div v-for="(log, index) in logs" :key="index" class="p-2 bg-stone-800/50 rounded border-l-2 border-stone-600 hover:bg-stone-800 transition-colors">
                <div class="text-xs text-stone-500 mb-1">[{{ log.time }}]</div>
                <div>{{ log.message }}</div>
             </div>
             <div v-if="logs.length === 0" class="text-center text-stone-600 mt-4">暂无日志</div>
          </div>
       </div>

       <!-- Center Panel: Interaction / Visuals -->
       <div class="flex-1 flex flex-col p-6 z-10 relative">
          <!-- Central Display (e.g. Current Customer) -->
          <div class="flex-1 flex items-center justify-center">
             <div v-if="currentCustomer" class="text-center space-y-6 animate-fade-in-up">
                <div class="relative w-40 h-40 mx-auto">
                   <div class="absolute inset-0 bg-amber-500/20 rounded-full animate-pulse"></div>
                   <div class="relative w-full h-full bg-stone-800 rounded-full border-4 border-stone-700 flex items-center justify-center text-6xl shadow-2xl overflow-hidden">
                      👤
                   </div>
                </div>
                
                <div class="bg-stone-800/90 p-6 rounded-lg border border-stone-600 max-w-lg mx-auto shadow-xl backdrop-blur-md">
                   <div class="flex items-center justify-between mb-2">
                      <span class="text-amber-500 font-bold">{{ currentCustomer.name }}</span>
                      <span class="text-xs text-stone-500 bg-stone-900 px-2 py-0.5 rounded">{{ currentCustomer.title }}</span>
                   </div>
                   <p class="text-stone-300 italic mb-6 text-lg leading-relaxed">"{{ currentCustomer.dialogue }}"</p>
                   
                   <div class="flex gap-3 justify-center">
                      <button class="px-4 py-2 bg-amber-700/80 hover:bg-amber-600 text-amber-50 text-sm rounded border border-amber-500 transition-all hover:scale-105 shadow-lg cursor-pointer">
                         热情招呼
                      </button>
                      <button class="px-4 py-2 bg-stone-700 hover:bg-stone-600 text-stone-300 text-sm rounded border border-stone-500 transition-all hover:scale-105 cursor-pointer">
                         冷淡回应
                      </button>
                   </div>
                </div>
             </div>
             
             <div v-else class="text-center text-stone-500">
                <div class="text-6xl mb-4 opacity-30">🍵</div>
                <p class="text-xl">暂无修士光顾</p>
                <p class="text-sm mt-2 opacity-50">正在等待客源...</p>
             </div>
          </div>
       </div>
    </main>

    <!-- Bottom Bar: Actions -->
    <footer class="h-24 bg-stone-800 border-t border-stone-700 flex items-center justify-center gap-10 shadow-[0_-5px_15px_rgba(0,0,0,0.3)] z-20 pb-2">
       <button @click="$router.push('/game/market')" class="flex flex-col items-center gap-2 group cursor-pointer">
          <div class="w-12 h-12 rounded-full bg-stone-700 group-hover:bg-amber-700 flex items-center justify-center transition-all border border-stone-600 group-hover:border-amber-500 shadow-lg group-hover:shadow-amber-900/50">
             🛒
          </div>
          <span class="text-xs text-stone-400 group-hover:text-amber-200 font-medium">灵材采买</span>
       </button>
       
       <button @click="$router.push('/game/inventory')" class="flex flex-col items-center gap-2 group cursor-pointer">
          <div class="w-12 h-12 rounded-full bg-stone-700 group-hover:bg-amber-700 flex items-center justify-center transition-all border border-stone-600 group-hover:border-amber-500 shadow-lg group-hover:shadow-amber-900/50">
             📦
          </div>
          <span class="text-xs text-stone-400 group-hover:text-amber-200 font-medium">灵材库</span>
       </button>

       <button class="flex flex-col items-center gap-2 group -mt-10 cursor-pointer">
          <div class="w-20 h-20 rounded-full bg-gradient-to-br from-amber-600 to-amber-800 group-hover:from-amber-500 group-hover:to-amber-700 flex items-center justify-center transition-all border-4 border-stone-800 shadow-xl shadow-amber-900/50 transform group-hover:scale-110 group-active:scale-95">
             <span class="text-3xl filter drop-shadow-md">🈺</span>
          </div>
          <span class="text-sm font-bold text-amber-400 group-hover:text-amber-200">开始接膳</span>
       </button>

       <button @click="$router.push('/game/kitchen')" class="flex flex-col items-center gap-2 group cursor-pointer">
          <div class="w-12 h-12 rounded-full bg-stone-700 group-hover:bg-amber-700 flex items-center justify-center transition-all border border-stone-600 group-hover:border-amber-500 shadow-lg group-hover:shadow-amber-900/50">
             🔥
          </div>
          <span class="text-xs text-stone-400 group-hover:text-amber-200 font-medium">研发灵膳</span>
       </button>

       <button class="flex flex-col items-center gap-2 group cursor-pointer">
          <div class="w-12 h-12 rounded-full bg-stone-700 group-hover:bg-amber-700 flex items-center justify-center transition-all border border-stone-600 group-hover:border-amber-500 shadow-lg group-hover:shadow-amber-900/50">
             💰
          </div>
          <span class="text-xs text-stone-400 group-hover:text-amber-200 font-medium">灵石理财</span>
       </button>
    </footer>
  </div>
</template>

<script setup>
import { useGameStore } from '../../stores/game'
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useToastStore } from '@/stores/toast'

const router = useRouter()
const toast = useToastStore()
const gameStore = useGameStore()
const player = computed(() => gameStore.player)
const logs = computed(() => gameStore.logs)
const shichen = computed(() => gameStore.shichen)
const currentCustomer = computed(() => gameStore.currentCustomer)
const showSettings = ref(false)

// Helper to format money with commas
const formattedMoney = computed(() => {
  return player.value.spiritStones.toLocaleString()
})

const rankName = computed(() => {
  return `凡俗界食肆 · ${player.value.rank}阶`
})

const handleLogout = () => {
  toast.success('已退出登录')
  router.push('/auth/login')
}
</script>

<style scoped>
.custom-scrollbar::-webkit-scrollbar {
  width: 4px;
}
.custom-scrollbar::-webkit-scrollbar-track {
  background: rgba(0, 0, 0, 0.1);
}
.custom-scrollbar::-webkit-scrollbar-thumb {
  background: rgba(120, 113, 108, 0.5);
  border-radius: 2px;
}

@keyframes fade-in-up {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}
.animate-fade-in-up {
  animation: fade-in-up 0.5s ease-out forwards;
}

@keyframes fade-in-down {
  from { opacity: 0; transform: translateY(-10px); }
  to { opacity: 1; transform: translateY(0); }
}
.animate-fade-in-down {
  animation: fade-in-down 0.2s ease-out forwards;
}
</style>
