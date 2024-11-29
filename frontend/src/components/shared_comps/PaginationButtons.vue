<template>
  <div class="flex justify-center mt-4">
    <button @click="prevPage" :disabled="page_number === 0" class="px-4 py-2 mx-1 bg-gray-700 text-white rounded disabled:opacity-50">
      Previous
    </button>
    <span class="px-4 py-2 mx-1">{{ page_number + 1}}</span>
    <button @click="nextPage" :disabled="total_pages - page_number <= 1" class="px-4 py-2 mx-1 bg-gray-700 text-white rounded disabled:opacity-50">
      Next
    </button>
  </div>
</template>

<script setup>
import {get, page_number, total_pages} from "@/js/cars-ws.js";


const prevPage = () => {
  if (page_number.value > 0) {
    page_number.value -= 1;
    get();
  }
};

const nextPage = () => {
    page_number.value += 1;
    get();
};
</script>

<style scoped>
</style>