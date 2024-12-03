<template>
  <div class="fixed inset-0 flex flex-col items-center justify-center bg-black bg-opacity-50 z-40">
    <h1 class="text-2xl font-bold mb-6 text-center">{{ title }}</h1>
    <div class="login-container bg-gray-800 p-4 rounded-lg shadow-lg w-[360px]">
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
          <button @click="saveCoordinates" type="button" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">Save</button>
          <button @click="closeModal" type="button" class="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">Close</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import {AddEditWindowType} from "@/js/utils.js";
import {token} from "@/js/csrf-token.js";
import {showAlert} from "@/js/custom-alert.js";

const props = defineProps({
  title: String,
  type: Number,
  item: {
    type : Object,
    default : {
      x: '',
      y: ''
    }
  }
});

const emit = defineEmits(['close', 'add']);

const xCoord = ref(props.item.x);
const yCoord = ref(props.item.y);
const xError = ref('');
const yError = ref('');

const closeModal = () => {
  xCoord.value = '';
  yCoord.value = '';
  xError.value = '';
  yError.value = '';
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

const saveCoordinates = () => {
  if (!validateForm()) {
    return;
  }
  if (props.type === AddEditWindowType.ADDING) {
    request('/coordinates/add', 'POST')
  } else {
    request('/coordinates/update/' + props.item.id, 'PATCH')
  }
  closeModal();
};

const request = (url, type) => {
  $.ajax({
    url: url,
    type: type,
    contentType: 'application/json',
    headers: {
      'X-CSRF-Token': token.value
    },
    data: JSON.stringify({
      "x": xCoord.value,
      "y": yCoord.value,
    }),
    success: function (data) {
      showAlert(data)
    },
    error: function (error) {
      showAlert(error.responseText)
    }
  })
}
</script>

<style scoped>
</style>