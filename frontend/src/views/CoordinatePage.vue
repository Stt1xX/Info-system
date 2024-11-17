<template>
  <div class="min-h-screen flex flex-col bg-gray-900 text-white">
    <main class="flex-grow container mx-auto p-4">
      COORDINATES
    </main>
  </div>
</template>

<script setup>
import Header from "@/components/Header.vue"

const props = defineProps({
  token: String
})
</script>