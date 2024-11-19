<template>
  <div v-if="visible" class="fixed inset-0 flex flex-col items-center justify-center bg-black bg-opacity-50">
    <h1 class="text-2xl font-bold mb-6 text-center">{{ title }}</h1>
    <div class="login-container bg-gray-800 p-4 rounded-lg shadow-lg">
      <form class="login-form space-y-4">
        <div class="form-group">
          <input v-model="xCoord" type="text" name="xCoord" placeholder="coordinate X" class="form-control w-full p-3 rounded bg-gray-700 border border-gray-600 focus:outline-none focus:ring-2 focus:ring-blue-500"/>
          <div v-if="xError" class="warning text-red-500 mt-2">{{ xError }}</div>
        </div>
        <div class="form-group">
          <input v-model="yCoord" type="text" name="yCoord" placeholder="coordinate Y" class="form-control w-full p-3 rounded bg-gray-700 border border-gray-600 focus:outline-none focus:ring-2 focus:ring-blue-500"/>
          <div v-if="yError" class="warning text-red-500 mt-2">{{ yError }}</div>
        </div>
        <div class="form-actions flex justify-between items-center mt-4">
          <button @click="addCoord" type="button" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">Save</button>
          <button @click="closeModal" type="button" class="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">Close</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const props = defineProps({
  visible: Boolean,
  title: String,
  item: {
    type : Object,
    default : {
      xCoord: '',
      yCoord: ''
    }
  }
});

const emit = defineEmits(['close', 'add']);

const xCoord = ref(props.item.xCoord);
const yCoord = ref(props.item.yCoord);
const xError = ref('');
const yError = ref('');

const closeModal = () => {
  xCoord.value = '';
  yCoord.value = '';
  emit('close');
};

const validateForm = () => {
  let isValid = true;
  xError.value = '';
  yError.value = '';
  if (isNaN(xCoord.value)) {
    xError.value = "X coordinate must be a number";
    isValid = false;
  }
  if (isNaN(yCoord.value)) {
    yError.value = "Y coordinate must be a number";
    isValid = false;
  } else if (Number(yCoord.value) <= -407) {
    yError.value = "Y coordinate must be greater than -407";
    isValid = false;
  }

  return isValid;
};

const addCoord = () => {
  if (!validateForm()) {
    return;
  }
  emit('add', {xCoord: xCoord.value, yCoord: yCoord.value});
  closeModal();
};
</script>

<style scoped>
</style>