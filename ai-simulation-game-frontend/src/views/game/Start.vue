<template>
  <div class="min-h-screen bg-stone-900 flex flex-col items-center justify-center p-8 relative overflow-hidden">
     <!-- Background Effects -->
     <div class="absolute top-0 left-0 w-full h-full pointer-events-none">
        <div class="absolute top-1/4 left-1/4 w-96 h-96 bg-amber-900/20 rounded-full blur-3xl animate-pulse"></div>
        <div class="absolute bottom-1/4 right-1/4 w-96 h-96 bg-stone-800/40 rounded-full blur-3xl animate-pulse delay-1000"></div>
     </div>

     <div class="relative z-10 max-w-4xl w-full bg-stone-800/80 border border-amber-800/30 rounded-xl shadow-2xl overflow-hidden backdrop-blur-sm min-h-[600px] flex flex-col">
        
        <!-- Progress Bar -->
        <div class="h-1 bg-stone-700 w-full">
           <div class="h-full bg-amber-600 transition-all duration-500 ease-out" :style="{ width: `${(step / 3) * 100}%` }"></div>
        </div>

        <div class="flex-1 flex flex-col p-12">
           
           <!-- Step 1: Intro -->
           <div v-if="step === 1" class="flex-1 flex flex-col items-center justify-center text-center space-y-8 animate-fade-in">
              <h2 class="text-4xl font-bold text-amber-500 font-serif mb-8">楔子</h2>
              <p class="text-2xl text-stone-300 leading-relaxed font-light tracking-widest max-w-2xl">
                 “汝为一介散修，于玄洲 / 沧澜界开一食肆，<br>
                 以炼膳之道求仙途...”
              </p>
              <div class="pt-12">
                 <button @click="nextStep" class="px-8 py-3 bg-amber-700 hover:bg-amber-600 text-amber-50 rounded border border-amber-500 shadow-lg transition-all">
                    踏入仙途
                 </button>
              </div>
           </div>

           <!-- Step 2: Category -->
           <div v-if="step === 2" class="flex-1 flex flex-col items-center justify-center animate-fade-in">
              <h2 class="text-3xl font-bold text-amber-500 mb-12 font-serif">择道 (经营品类)</h2>
              <div class="grid grid-cols-1 md:grid-cols-2 gap-8 w-full max-w-3xl">
                 <div 
                    @click="selectCategory('1')"
                    :class="['p-8 rounded-lg border-2 cursor-pointer transition-all hover:scale-105', form.category === '1' ? 'border-amber-500 bg-amber-900/30' : 'border-stone-600 bg-stone-800/50 hover:border-stone-500']"
                 >
                    <h3 class="text-2xl font-bold text-amber-400 mb-4">固本类</h3>
                    <p class="text-stone-400 mb-4">以灵谷、灵米为主，炼造固本培元的灵膳。</p>
                    <p class="text-sm text-stone-500">特点：初始灵石成本低</p>
                 </div>
                 <div 
                    @click="selectCategory('0')"
                    :class="['p-8 rounded-lg border-2 cursor-pointer transition-all hover:scale-105', form.category === '0' ? 'border-amber-500 bg-amber-900/30' : 'border-stone-600 bg-stone-800/50 hover:border-stone-500']"
                 >
                    <h3 class="text-2xl font-bold text-amber-400 mb-4">淬灵类</h3>
                    <p class="text-stone-400 mb-4">以灵泉、灵果为主，炼造快速淬灵的灵饮。</p>
                    <p class="text-sm text-stone-500">特点：修士群体广泛</p>
                 </div>
              </div>
              <div class="mt-12">
                 <button @click="nextStep" :disabled="!form.category" class="px-8 py-3 bg-amber-700 hover:bg-amber-600 disabled:opacity-50 disabled:cursor-not-allowed text-amber-50 rounded border border-amber-500 shadow-lg transition-all">
                    确认道统
                 </button>
              </div>
           </div>

           <!-- Step 3: Stats (Moved up) -->
           <div v-if="step === 3" class="flex-1 flex flex-col items-center justify-center animate-fade-in">
              <h2 class="text-3xl font-bold text-amber-500 mb-8 font-serif">天赋 (初始点数)</h2>
              
              <div class="bg-stone-900/50 p-6 rounded-lg mb-8 border border-stone-700">
                 <div class="text-center text-xl text-stone-300 mb-2">剩余点数: <span class="text-amber-400 font-bold text-2xl">{{ remainingPoints }}</span></div>
                 <div class="text-sm text-stone-500 text-center">初始共 3 点</div>
              </div>

              <div class="space-y-6 w-full max-w-xl">
                 <!-- Stat 1 -->
                 <div class="flex items-center justify-between bg-stone-800 p-4 rounded border border-stone-700">
                    <div class="flex-1">
                       <h4 class="text-lg font-bold text-amber-100">鉴灵术</h4>
                       <p class="text-xs text-stone-500">影响灵材采集品质和兑换价格 (每点+2%)</p>
                    </div>
                    <div class="flex items-center gap-4">
                       <button @click="decrement('jianling')" class="w-8 h-8 rounded bg-stone-700 hover:bg-stone-600 text-stone-300 flex items-center justify-center font-bold">-</button>
                       <span class="w-8 text-center text-xl font-bold text-amber-400">{{ form.stats.jianling }}</span>
                       <button @click="increment('jianling')" class="w-8 h-8 rounded bg-stone-700 hover:bg-stone-600 text-stone-300 flex items-center justify-center font-bold" :disabled="remainingPoints <= 0">+</button>
                    </div>
                 </div>

                 <!-- Stat 2 -->
                 <div class="flex items-center justify-between bg-stone-800 p-4 rounded border border-stone-700">
                    <div class="flex-1">
                       <h4 class="text-lg font-bold text-amber-100">辩道术</h4>
                       <p class="text-xs text-stone-500">影响修士满意度和灵膳售价 (每点+2%)</p>
                    </div>
                    <div class="flex items-center gap-4">
                       <button @click="decrement('biandao')" class="w-8 h-8 rounded bg-stone-700 hover:bg-stone-600 text-stone-300 flex items-center justify-center font-bold">-</button>
                       <span class="w-8 text-center text-xl font-bold text-amber-400">{{ form.stats.biandao }}</span>
                       <button @click="increment('biandao')" class="w-8 h-8 rounded bg-stone-700 hover:bg-stone-600 text-stone-300 flex items-center justify-center font-bold" :disabled="remainingPoints <= 0">+</button>
                    </div>
                 </div>

                 <!-- Stat 3 -->
                 <div class="flex items-center justify-between bg-stone-800 p-4 rounded border border-stone-700">
                    <div class="flex-1">
                       <h4 class="text-lg font-bold text-amber-100">炼膳术</h4>
                       <p class="text-xs text-stone-500">影响炼膳品质和成功率 (每点+2%)</p>
                    </div>
                    <div class="flex items-center gap-4">
                       <button @click="decrement('lianshan')" class="w-8 h-8 rounded bg-stone-700 hover:bg-stone-600 text-stone-300 flex items-center justify-center font-bold">-</button>
                       <span class="w-8 text-center text-xl font-bold text-amber-400">{{ form.stats.lianshan }}</span>
                       <button @click="increment('lianshan')" class="w-8 h-8 rounded bg-stone-700 hover:bg-stone-600 text-stone-300 flex items-center justify-center font-bold" :disabled="remainingPoints <= 0">+</button>
                    </div>
                 </div>
              </div>

              <div class="mt-12 flex gap-4">
                 <button @click="prevStep" class="px-6 py-3 bg-stone-700 hover:bg-stone-600 text-stone-300 rounded border border-stone-500 transition-all">
                    返回
                 </button>
                 <button @click="finish" :disabled="remainingPoints > 0" class="px-8 py-3 bg-amber-700 hover:bg-amber-600 disabled:opacity-50 disabled:cursor-not-allowed text-amber-50 rounded border border-amber-500 shadow-lg transition-all">
                    开宗立肆
                 </button>
              </div>
           </div>

        </div>
     </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useGameStore } from '../../stores/game'
import { useToastStore } from '@/stores/toast'

const router = useRouter()
const toast = useToastStore()
const gameStore = useGameStore()
const step = ref(1)

const form = reactive({
  category: '', // 1: solid, 0: liquid
  stats: {
    jianling: 0,
    biandao: 0,
    lianshan: 0
  }
})

const maxPoints = 3

const remainingPoints = computed(() => {
  return maxPoints - (form.stats.jianling + form.stats.biandao + form.stats.lianshan)
})

const nextStep = () => {
  step.value++
}

const prevStep = () => {
  step.value--
}

const selectCategory = (val) => {
  form.category = val
}

const increment = (stat) => {
  if (remainingPoints.value > 0) {
    form.stats[stat]++
  }
}

const decrement = (stat) => {
  if (form.stats[stat] > 0) {
    form.stats[stat]--
  }
}

const finish = async () => {
  try {
    const success = await gameStore.startGame(form)
    if (success) {
      toast.success('开宗立肆成功！')
      router.push('/game/main')
    } else {
      toast.error('开宗立肆失败')
    }
  } catch (error) {
    toast.error('初始化异常')
  }
}
</script>

<style scoped>
@keyframes fade-in {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}
.animate-fade-in {
  animation: fade-in 0.5s ease-out forwards;
}
</style>
