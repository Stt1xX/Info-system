<template>
  <div class="flex items-center justify-center p-4 rounded-lg pl-0">
    <div class="relative" ref="dropdown">
      <div @click="toggleDropdown" class="h-14 text-center w-40 rounded-l-full bg-gray-700 focus:outline-none hover:cursor-pointer flex items-center justify-between px-4">
        <span>{{ selectedOption }}</span>
        <svg class="fill-current h-4 w-4" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"><path d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"/></svg>
      </div>
      <ul v-if="isDropdownOpen" class="absolute z-10 w-full bg-gray-700 rounded-lg mt-1">
        <li v-for="option in options" :key="option" @click="selectOption(option)" class="px-4 py-2 hover:bg-gray-600 cursor-pointer">{{ option }}</li>
      </ul>
    </div>
    <input v-model="inputValue" type="text" placeholder="Enter value" class="h-14 w-full p-4 bg-gray-700 focus:outline-none border-none rounded-none"/>
    <button class="h-14 w-20 rounded-r-full bg-blue-500 hover:bg-blue-700 text-white focus:outline-none focus:shadow-outline">
      <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" class="w-6 h-6 mx-auto mr-6">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-4.35-4.35m1.4-5.65a7 7 0 1 1-14 0 7 7 0 0 1 14 0z" />
      </svg>
    </button>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';

const inputValue = ref('');
const selectedOption = ref('Option 1');
const options = ref(['Option 1', 'Option 2', 'Option 3']);
const isDropdownOpen = ref(false);
const dropdown = ref(null);

const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value;
};

const selectOption = (option) => {
  selectedOption.value = option;
  isDropdownOpen.value = false;
};

const handleClickOutside = (event) => {
  if (dropdown.value && !dropdown.value.contains(event.target)) {
    isDropdownOpen.value = false;
  }
};

onMounted(() => {
  document.addEventListener('click', handleClickOutside);
});

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside);
});
</script>

<style scoped>
</style>