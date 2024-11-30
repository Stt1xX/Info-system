<template>
  <div class="flex justify-center mt-4">
    <button @click="prevPage" :disabled="objects[itemType].page_number === 0" class="px-4 py-2 mx-1 bg-gray-700 text-white rounded disabled:opacity-50">
      Previous
    </button>
    <span class="px-4 py-2 mx-1">{{ objects[itemType].page_number + 1}}</span>
    <button @click="nextPage" :disabled="objects[itemType].total_pages - objects[itemType].page_number <= 1" class="px-4 py-2 mx-1 bg-gray-700 text-white rounded disabled:opacity-50">
      Next
    </button>
  </div>
</template>

<script setup>
import {get, objects} from "@/js/items-ws.js";


const props = defineProps({
  itemType : Number
})

const prevPage = () => {
  if (objects.value[props.itemType].page_number > 0) {
    objects.value[props.itemType].page_number -= 1;
    get(props.itemType);
  }
};

const nextPage = () => {
  objects.value[props.itemType].page_number += 1;
    get(props.itemType);
};
</script>

<style scoped>
</style>