<template>
  <svg @click="toggleSortMenu" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-14 text-gray-500 hover:text-white hover:bg-gray-700 rounded-full transition-colors duration-300 cursor-pointer p-2">
    <path stroke-linecap="round" stroke-linejoin="round" d="M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5" />
  </svg>
  <AdditionalFunctionalMenu :isAdditionalMenuVisible="isAdditionalMenuVisible" @close="closeSortMenu" />
</template>

<script setup>
import {ref} from "vue";
import AdditionalFunctionalMenu from "@/components/shared_comps/AdditionalFunctionalMenu.vue";

const isAdditionalMenuVisible = ref(false);

const toggleSortMenu = () => {
  isAdditionalMenuVisible.value = !isAdditionalMenuVisible.value;
};

const closeSortMenu = () => {
  isAdditionalMenuVisible.value = false
}
</script>

<style scoped>
</style>