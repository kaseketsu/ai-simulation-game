<template>
  <div class="flex min-h-screen">
    <GlobalAside />
    <main class="flex-1 bg-black transition-all duration-400 ease-in-out">
      <GlobalHeader />
    </main>
  </div>
</template>
<script setup lang="ts">
import GlobalAside from '@/components/layouts/GlobalAside.vue'
import GlobalHeader from '@/components/layouts/GlobalHeader.vue'
</script>

<style scoped></style>
