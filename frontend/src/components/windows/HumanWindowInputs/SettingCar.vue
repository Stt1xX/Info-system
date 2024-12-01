<template>

  <div class="form-group flex flex-col items-center">
    <label class="text-white mb-2 text-lg text-center block">Choose a car for your man:</label>
    <div class="relative w-1/2" ref="dropdown">
      <div @click="toggleDropdown" class="bg-gray-700 border border-gray-600 rounded p-2 flex justify-between items-center cursor-pointer">
        <span>{{ selectedCarName }}</span>
        <svg class="w-4 h-4 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
        </svg>
      </div>
      <div v-if="dropdownVisible" class="absolute mt-1 w-full bg-gray-700 border border-gray-600 rounded shadow-lg z-10">
        <div v-for="car in cars" :key="car.id" @click="selectCar(car)" class="p-2 hover:bg-gray-600 cursor-pointer">
          {{ car.name }}
        </div>
        <div @click="addNewCar" class="p-2 hover:bg-gray-600 cursor-pointer">Add new car</div>
      </div>
    </div>
    <div v-if="Error" class="text-red-500 mt-2">{{ Error }}</div>
    <AddEditCarWindow  :visible="showAddEditCarWindow" @close="closeAddEditCarWindow"
                       :title="'Add new car'" :type="AddEditWindowType.ADDING" class="fixed inset-0 z-50"/>
  </div>
</template>

<script setup>
import {computed, onMounted, ref} from "vue";
import AddEditCarWindow from "@/components/windows/AddEditCarWindow.vue";
import {AddEditWindowType} from "@/js/utils.js";

const cars = ref([{id: 1, name: 'Car 1'}, {id: 2, name: 'Car 2'}]); // Example car data
const selectedCar = ref(null);
const Error = ref('');
const showAddEditCarWindow = ref(false);
const dropdownVisible = ref(false);
const dropdown = ref(null);

const toggleDropdown = () => {
  dropdownVisible.value = !dropdownVisible.value;
};

const selectedCarName = computed(() => {
  if (selectedCar.value) {
    return cars.value.find(car => car.id === selectedCar.value.id).name;
  }
  return 'Select a car';
});

const addNewCar = () => {
  showAddEditCarWindow.value = true;
};

const closeAddEditCarWindow = () => {
  showAddEditCarWindow.value = false;
};

const selectCar = (car) => {
  selectedCar.value = car;
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
  if (!selectedCar.value) {
    Error.value = "Car can't be null";
    return false;
  }
  Error.value = '';
  return true;
};

defineExpose({
  selectedCar,
  validate
});

</script>

<style scoped>

</style>