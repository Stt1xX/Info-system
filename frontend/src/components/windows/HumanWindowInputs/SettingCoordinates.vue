<template>
  <div class="form-group flex flex-col items-center">
    <label class="text-white mb-2 text-lg text-center block">Choose coordinates for your man:</label>
    <div class="relative w-1/2" ref="dropdown">
      <div @click="toggleDropdown" class="bg-gray-700 border border-gray-600 rounded p-2 flex justify-between items-center cursor-pointer">
        <span>{{ selectedCoordinatesName }}</span>
        <svg class="w-4 h-4 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
        </svg>
      </div>
      <div v-if="dropdownVisible" class="absolute mt-1 w-full bg-gray-700 border border-gray-600 rounded shadow-lg z-10">
        <div v-for="coord in coordinates" :key="coord.id" @click="selectCoordinates(coord)" class="p-2 hover:bg-gray-600 cursor-pointer">
          {{ 'x: ' + coord.x + ' y: ' + coord.y }}
        </div>
        <div @click="addNewCoordinates" class="p-2 hover:bg-gray-600 cursor-pointer">Add new coordinates</div>
      </div>
    </div>
    <div v-if="Error" class="text-red-500 mt-2">{{ Error }}</div>
    <AddEditCoordsWindow  :visible="showAddEditCoordinatesWindow" @close="closeAddEditCoordinatesWindow"
                          :title="'Add new car'" :type="AddEditWindowType.ADDING" class="fixed inset-0 z-50"/>
  </div>
</template>

<script setup>

import {computed, onMounted, ref} from "vue";
import AddEditCoordsWindow from "@/components/windows/AddEditCoordsWindow.vue";
import {AddEditWindowType} from "@/js/utils.js";

const coordinates = ref([{id: 1, x: 123, y: 154}, {id: 2, x: 13121223, y: 151234}]); // Example coordinates data
const selectedCoordinates = ref(null);
const Error = ref('');
const showAddEditCoordinatesWindow = ref(false);
const dropdownVisible = ref(false);
const dropdown = ref(null);

const toggleDropdown = () => {
  dropdownVisible.value = !dropdownVisible.value;
};

const selectedCoordinatesName = computed(() => {
  if (selectedCoordinates.value) {
    let coord = coordinates.value.find(coordinate => coordinate.id === selectedCoordinates.value.id)
    return 'x: ' + coord.x + ' y: ' + coord.y;
  }
  return 'Select coordinates';
});

const addNewCoordinates = () => {
  showAddEditCoordinatesWindow.value = true;
  dropdownVisible.value = false;
};

const closeAddEditCoordinatesWindow = () => {
  showAddEditCoordinatesWindow.value = false;
};

const selectCoordinates = (coordinates) => {
  selectedCoordinates.value = coordinates;
  dropdownVisible.value = false;
};

const handleClickOutside = (event) => {
  if (dropdown.value && !dropdown.value.contains(event.target)) {
    dropdownVisible.value = false;
  }
};

onMounted(() => {
  document.addEventListener('click', handleClickOutside);
});

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