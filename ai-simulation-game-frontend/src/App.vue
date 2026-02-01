<template>
  <div class="min-h-screen bg-stone-900 text-stone-200 font-serif">
    <ToastContainer />
    <router-view />
  </div>
</template>

<script setup>
import ToastContainer from '@/components/ui/ToastContainer.vue'
</script>

<style>
/* Global styles if needed */
body {
  background-color: #1c1917; /* stone-900 */
}
</style>
