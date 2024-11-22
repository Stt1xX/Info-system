<template>
  <svg @click="toggleSortMenu" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-14 text-gray-500 hover:text-white hover:bg-gray-700 rounded-full transition-colors duration-300 cursor-pointer p-2">
    <path stroke-linecap="round" stroke-linejoin="round" d="M10.5 6h9.75M10.5 6a1.5 1.5 0 1 1-3 0m3 0a1.5 1.5 0 1 0-3 0M3.75 6H7.5m3 12h9.75m-9.75 0a1.5 1.5 0 0 1-3 0m3 0a1.5 1.5 0 0 0-3 0m-3.75 0H7.5m9-6h3.75m-3.75 0a1.5 1.5 0 0 1-3 0m3 0a1.5 1.5 0 0 0-3 0m-9.75 0h9.75" />
  </svg>
  <SortMenu :isSortMenuVisible="isSortMenuVisible" @close="closeSortMenu" :item-type="itemType" />
</template>

<script setup>
import { ref } from 'vue';
import SortMenu from "@/components/shared_comps/SortMenu.vue";

const props = defineProps({
  itemType: Number
})

const isSortMenuVisible = ref(false);

const toggleSortMenu = () => {
  isSortMenuVisible.value = !isSortMenuVisible.value;
};

const closeSortMenu = () => {
  isSortMenuVisible.value = false
}

</script>

<style scoped>
</style>