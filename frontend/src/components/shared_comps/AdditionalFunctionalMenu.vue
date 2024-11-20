<template>
  <div v-if="isAdditionalMenuVisible" class="fixed inset-0 flex flex-col items-center justify-center bg-black bg-opacity-50 z-50">
    <h1 class="text-2xl font-bold mb-6 text-center">Additional functionality</h1>
    <div class="bg-gray-800 p-4 rounded-lg shadow-lg w-64">
      <form class="space-y-4">
        <div class="form-group">
          <div class="flex flex-col space-y-2">
            <label :class="{'bg-gray-600': selectedOption === 'option1', 'hover:bg-gray-700': selectedOption !== 'option1'}" class="p-2 rounded cursor-pointer">
              <input type="radio" v-model="selectedOption" value="option1" class="hidden"> Option 1
            </label>
            <label :class="{'bg-gray-600': selectedOption === 'option2', 'hover:bg-gray-700': selectedOption !== 'option2'}" class="p-2 rounded cursor-pointer">
              <input type="radio" v-model="selectedOption" value="option2" class="hidden"> Option 2
            </label>
            <label :class="{'bg-gray-600': selectedOption === 'option3', 'hover:bg-gray-700': selectedOption !== 'option3'}" class="p-2 rounded cursor-pointer">
              <input type="radio" v-model="selectedOption" value="option3" class="hidden"> Option 3
            </label>
          </div>
        </div>
        <div class="flex justify-between items-center mt-4">
          <button @click="applyFunc" type="button" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">Apply</button>
          <button @click="closeMenu" type="button" class="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">Close</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const selectedOption = ref('option1');

const props = defineProps({
  isAdditionalMenuVisible: Boolean
});

const emit = defineEmits(['close', 'apply']);

const applyFunc = () => {
  emit('apply');
  closeMenu();
};

const closeMenu = () => {
  emit('close');
};
</script>

<style scoped>
.hidden {
  display: none;
}
</style>