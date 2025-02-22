<template>
  <div class="flex justify-center" ref="div">
    <input type="button" value="Previous" @click="prevPage" :disabled="pageNumber === 0" class="px-4 py-2 mx-1 bg-gray-700 text-white rounded disabled:opacity-50 cursor-pointer" />
    <span class="px-4 py-2 mx-1">{{ pageNumber + 1}}</span>
    <input type="button" value="Next" @click="nextPage" :disabled="totalPages - pageNumber <= 1" class="px-4 py-2 mx-1 bg-gray-700 text-white rounded disabled:opacity-50 cursor-pointer" />
  </div>
</template>

<script setup>
import {get, pageNumber, totalPages} from "@/js/import-history.js";
import {ref} from "vue";

const div = ref(null)

const prevPage = () => {
  if (pageNumber.value > 0) {
    pageNumber.value -= 1;
    get();
  }
};

const nextPage = () => {
  pageNumber.value += 1;
  get();
};


</script>

<style scoped>
</style>