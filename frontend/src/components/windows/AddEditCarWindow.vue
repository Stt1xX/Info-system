<template>
  <div v-if="visible" class="fixed inset-0 flex flex-col items-center justify-center bg-black bg-opacity-50">
    <h1 class="text-2xl font-bold mb-6 text-center">{{ title }}</h1>
    <div class="login-container bg-gray-800 p-4 rounded-lg shadow-lg w-[360px]">
      <form @submit.prevent="saveCar" class="space-y-4">
        <div class="form-group">
          <input v-model="name" type="text" name="name" placeholder="name" class="form-control w-full p-3 rounded bg-gray-700 border border-gray-600 focus:outline-none focus:ring-2 focus:ring-blue-500"/>
          <div v-if="carNameError" class="warning text-red-500 mt-2">{{ carNameError }}</div>
        </div>
        <div class="form-group flex items-center">
          <input v-model="cool" type="checkbox" id="cool" class="form-checkbox h-5 w-5 text-blue-600 bg-gray-700 border border-gray-600 rounded focus:outline-none focus:ring-2 focus:ring-blue-500">
          <label for="cool" class="ml-2 text-gray-300">Cool</label>
        </div>
        <div class="form-actions flex justify-between items-center mt-4">
          <input type="submit" value="Save" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline focus:cursor-pointer"/>
          <button @click="closeModal" type="button" class="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">Close</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import {token} from "@/js/csrf-token.js";
import {AddEditWindowType} from "@/js/utils.js";
import {showAlert} from "@/js/custom-alert.js";
const props = defineProps({
  visible: Boolean,
  title: String,
  type: Number,
  item: {
    type : Object,
    default : {
      name: '',
      cool: false
    }
  }
});

const emit = defineEmits(['close']);

const name = ref(props.item.name);
const cool = ref(props.item.cool);
const carNameError = ref('');

const closeModal = () => {
  name.value = '';
  cool.value = false;
  carNameError.value = '';
  emit('close');
};

const validateForm = () => {
  let isValid = true;
  if (!name.value) {
    carNameError.value = "Car's name can't be null";
    isValid = false;
  } else {
    carNameError.value = "";
  }
  return isValid;
};

const saveCar = () => {
  if (!validateForm()) {
    return;
  }
  if (props.type === AddEditWindowType.ADDING) {
    request('/cars/add', 'POST')
  } else {
    request('/cars/update/' + props.item.id, 'PATCH')
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
      "name": name.value,
      "cool": cool.value,
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