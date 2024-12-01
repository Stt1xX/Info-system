<template>
  <div class="relative" ref="dropdown">
    <div @click="toggleDropdown" class="h-[41.6px] rounded-l w-40 bg-gray-700 focus:outline-none hover:cursor-pointer flex items-center justify-between px-2">
      <span>{{selectedOption.frontName}}</span>
      <svg class="fill-current h-4 w-4" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"><path d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"/></svg>
    </div>
    <ul v-if="isDropdownOpen" class="absolute z-10 bg-gray-700 rounded-lg mt-1">
      <li v-for="option in options" :key="option" @click="selectOption(option)" class="px-4 py-2 hover:bg-gray-600 cursor-pointer">{{ option.frontName }}</li>
    </ul>
  </div>
</template>

<script setup>
import { onMounted, onUnmounted, ref } from 'vue';
import { getUrlPrefix } from '@/js/utils.js';

const selectedOption = ref({ frontName: 'id', backName: 'id' });
const options = ref([]);
const isDropdownOpen = ref(false);
const dropdown = ref(null);

const props = defineProps({
  itemType: Number
});

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
  $.ajax({
    type: 'GET',
    url: getUrlPrefix(props.itemType) + '/get_searchable_fields',
    success: (res) => {
      options.value = res;
    }
  });
});

defineExpose({
  selectedOption,
  options
});

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside);
});
</script>