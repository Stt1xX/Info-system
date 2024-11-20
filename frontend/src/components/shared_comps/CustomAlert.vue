<template>
  <div v-if="visible" class="bg-gray-800 text-white p-2 rounded-lg flex items-center justify-between fixed bottom-4 right-4 h-16 w-96 shadow-lg">
    <div class="flex items-center">
      <button @click="closeAlert" class="mr-2 p-1 rounded-full hover:bg-gray-600 transition-colors">
        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
        </svg>
      </button>
      <span class="text-sm">{{ message }}</span>
    </div>
    <div class="absolute bottom-0 left-0 h-1 bg-white animate-progress"></div>
  </div>
</template>

<script setup>
import { closeAlert, message, visible } from "@/js/custom-alert.js";

</script>

<style scoped>
@keyframes progress {
  from {
    width: 100%;
  }
  to {
    width: 0;
  }
}

.animate-progress {
  animation: progress 5s linear forwards;
}


</style>