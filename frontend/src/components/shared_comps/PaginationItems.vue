<template>
  <div class="flex justify-center" ref="div">
    <input type="button" value="Previous" @click="prevPage" :disabled="objects[itemType].page_number === 0" class="px-4 py-2 mx-1 bg-gray-700 text-white rounded disabled:opacity-50 cursor-pointer" />
    <span class="px-4 py-2 mx-1">{{ objects[itemType].page_number + 1}}</span>
    <input type="button" value="Next" @click="nextPage" :disabled="objects[itemType].total_pages - objects[itemType].page_number <= 1" class="px-4 py-2 mx-1 bg-gray-700 text-white rounded disabled:opacity-50 cursor-pointer" />
  </div>
</template>

<script setup>
import {get, objects} from "@/js/items-ws.js";
import {ref} from "vue";

const div = ref(null)

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

defineExpose({
  div
})

</script>

<style scoped>
</style>