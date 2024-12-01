<template>
  <div class="form-group flex flex-col items-center">
    <label class="text-white mb-2 text-lg text-center block">Choose coordinates for your man:</label>
    <InnerSearch @choose="selectCoordinates" :add-edit-window="AddEditCoordsWindow" :item-type="ItemType.COORDINATES_HUMAN_SEARCH"/>
    <div v-if="Error" class="text-red-500 mt-2">{{ Error }}</div>
    <div class="grid grid-cols-2 mt-14 w-full gap-4" v-if="selectedCoordinates">
      <div class="flex flex-col space-y-2">
        <div class="flex justify-between items-center border-b border-gray-600 py-2">
          <h3 class="text-lg font-semibold text-white">Id</h3>
          <p class="text-white">{{ selectedCoordinates.id }}</p>
        </div>
        <div class="flex justify-between items-center border-b border-gray-600 py-2">
          <h3 class="text-lg font-semibold text-white">X coordinate</h3>
          <p class="text-white">{{ selectedCoordinates.x }}</p>
        </div>
      </div>
      <div class="flex flex-col space-y-2">
        <div class="flex justify-between items-center border-b border-gray-600 py-2">
          <h3 class="text-lg font-semibold text-white">Author</h3>
          <p class="text-white">{{ selectedCoordinates.author }}</p>
        </div>
        <div class="flex justify-between items-center border-b border-gray-600 py-2">
          <h3 class="text-lg font-semibold text-white">Y coordinate</h3>
          <p class="text-white">{{ selectedCoordinates.y }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref} from "vue";
import InnerSearch from "@/components/windows/HumanWindowInputs/InnerSearch.vue";
import {ItemType} from "@/js/utils.js";
import AddEditCoordsWindow from "@/components/windows/AddEditCoordsWindow.vue";

const selectedCoordinates = ref(null);
const Error = ref('');
const dropdownVisible = ref(false);

const selectCoordinates = (coordinates) => {
  selectedCoordinates.value = coordinates;
  dropdownVisible.value = false;
};

const validate = () => {
  if (!selectedCoordinates.value) {
    Error.value = "Coordinates can't be null";
    return false;
  }
  Error.value = '';
  return true;
};

defineExpose({
  selectedCoordinates,
  validate
});

</script>

<style scoped>

</style>