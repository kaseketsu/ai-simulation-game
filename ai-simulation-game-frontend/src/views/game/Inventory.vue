<template>
  <div class="min-h-screen bg-stone-900 text-stone-200 p-8">
    <div class="flex items-center justify-between mb-8 border-b border-stone-700 pb-4">
       <h1 class="text-3xl font-bold text-amber-500 font-serif">灵材库</h1>
       <button @click="$router.back()" class="px-4 py-2 bg-stone-700 rounded hover:bg-stone-600 border border-stone-600 text-stone-300">返回</button>
    </div>
    <div class="text-center text-stone-500 mt-20">
       <div class="text-6xl mb-4 opacity-50">📦</div>
       <p class="text-xl">库房空空如也</p>
       <p class="text-sm mt-2 opacity-60">快去灵市采买些灵材吧...</p>
    </div>
  </div>
</template>
