<template>
  <div v-if="isSortMenuVisible" class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
    <div class="bg-white p-6 rounded-lg shadow-lg w-96">
      <h2 class="text-xl font-bold mb-4">Sort Options</h2>
      <div class="mb-4">
        <label for="sortOptions" class="block text-gray-700 mb-2">Select an option:</label>
        <select id="sortOptions" v-model="selectedOption" class="w-full p-2 border rounded">
          <option value="option1">Option 1</option>
          <option value="option2">Option 2</option>
          <option value="option3">Option 3</option>
        </select>
      </div>
      <div class="flex justify-end space-x-4">
        <button @click="applySort" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-700">Apply</button>
        <button @click="closeMenu" class="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-700">Close</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { isSortMenuVisible } from '@/components/buttons/SortButton.vue';

const selectedOption = ref('option1');

const applySort = () => {
  console.log(`Selected option: ${selectedOption.value}`);
  // Add your logic to apply the sort here
};

const closeMenu = () => {
  isSortMenuVisible.value = false;
};
</script>

<style scoped>
</style>