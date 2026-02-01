<template>
  <div class="flex flex-col items-center justify-center min-h-screen relative bg-stone-900">
    <!-- Background placeholder since we don't have images yet -->
    <div class="absolute inset-0 opacity-20 bg-[radial-gradient(ellipse_at_center,_var(--tw-gradient-stops))] from-amber-900 via-stone-900 to-black"></div>
    
    <div class="relative z-10 text-center space-y-8 p-12 border-y-2 border-amber-800/50 bg-black/40 backdrop-blur-sm max-w-3xl w-full mx-4 rounded-lg shadow-2xl">
      <h1 class="text-7xl font-bold text-transparent bg-clip-text bg-gradient-to-b from-amber-300 to-amber-700 tracking-[0.2em] drop-shadow-sm filter" style="font-family: 'STKaiti', 'KaiTi', serif;">
        修仙食肆
      </h1>
      
      <div class="space-y-4 py-6">
        <p class="text-2xl text-stone-300 font-light tracking-widest">
          天地为炉 · 造化为工
        </p>
        <p class="text-xl text-stone-400 font-light tracking-wider">
          以炼膳之道 · 求无上仙途
        </p>
      </div>

      <div class="pt-10 flex flex-col items-center gap-6">
        <router-link 
          to="/auth/register" 
          class="group relative px-16 py-4 bg-transparent overflow-hidden rounded border border-amber-600 text-amber-100 shadow-[0_0_20px_rgba(180,83,9,0.2)] transition-all duration-300 hover:shadow-[0_0_30px_rgba(180,83,9,0.4)]"
        >
          <span class="absolute inset-0 w-full h-full bg-gradient-to-r from-transparent via-amber-900/20 to-transparent -translate-x-full group-hover:translate-x-full transition-transform duration-700 ease-in-out"></span>
          <span class="relative text-2xl font-bold tracking-widest group-hover:text-white transition-colors">开宗立肆</span>
        </router-link>
        
        <router-link 
          to="/auth/login" 
          class="text-amber-500/60 hover:text-amber-400 text-base tracking-wider hover:underline underline-offset-4 transition-colors"
        >
          已有传承？点此回归
        </router-link>
      </div>
    </div>
    
    <div class="absolute bottom-8 text-stone-600 text-xs tracking-widest">
      修仙界食肆掌柜模拟器 v1.0
    </div>
  </div>
</template>

<style scoped>
/* Optional: Add some subtle animations */
</style>
