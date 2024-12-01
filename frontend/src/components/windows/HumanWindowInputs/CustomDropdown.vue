<template>
  <div class="relative w-full" ref="dropdown">
    <div @click="toggleDropdown" class="bg-gray-700 border border-gray-600 rounded p-2 flex justify-between items-center cursor-pointer">
      <span>{{ selectedOption || placeholder }}</span>
      <svg class="w-4 h-4 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
      </svg>
    </div>
    <div v-if="dropdownVisible" class="absolute mt-1 w-full bg-gray-700 border border-gray-600 rounded shadow-lg z-10">
      <div v-for="option in options" :key="option" @click="selectOption(option)" class="p-2 hover:bg-gray-600 cursor-pointer">
        {{ option }}
      </div>
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref, watch} from 'vue';

const props = defineProps({
  modelValue: String,
  options: Array,
  placeholder: String
});

const emit = defineEmits(['update:modelValue']);
const dropdown = ref()
const dropdownVisible = ref(false);
const selectedOption = ref(props.modelValue);

const toggleDropdown = () => {
  dropdownVisible.value = !dropdownVisible.value;
};

const selectOption = (option) => {
  selectedOption.value = option;
  emit('update:modelValue', option);
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

watch(() => props.modelValue, (newValue) => {
  selectedOption.value = newValue;
});
</script>

<style scoped>
</style>