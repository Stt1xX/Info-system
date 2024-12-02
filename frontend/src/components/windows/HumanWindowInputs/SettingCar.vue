<template>
  <div class="form-group flex flex-col items-center">
    <label class="text-white mb-2 text-lg text-center block">Choose a car for your man:</label>
    <InnerSearch @choose="selectCar" :add-edit-window="AddEditCarWindow" :item-type="ItemType.CAR_HUMAN_SEARCH"/>
    <div v-if="Error" class="text-red-500 mt-2">{{ Error }}</div>
    <div class="grid grid-cols-2 mt-14 w-full gap-4" v-if="selectedCar">
      <div class="flex flex-col space-y-2">
        <div class="flex justify-between items-center border-b border-gray-600 py-2">
          <h3 class="text-lg font-semibold text-white">Id</h3>
          <p class="text-white">{{ selectedCar.id }}</p>
        </div>
        <div class="flex justify-between items-center border-b border-gray-600 py-2">
          <h3 class="text-lg font-semibold text-white">Name</h3>
          <p class="text-white">{{ selectedCar.name }}</p>
        </div>
      </div>
      <div class="flex flex-col space-y-2">
        <div class="flex justify-between items-center border-b border-gray-600 py-2">
          <h3 class="text-lg font-semibold text-white">Is cool</h3>
          <p class="text-white">{{ selectedCar.cool }}</p>
        </div>
        <div class="flex justify-between items-center border-b border-gray-600 py-2">
          <h3 class="text-lg font-semibold text-white">Author</h3>
          <p class="text-white">{{ selectedCar.author }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref} from "vue";
import InnerSearch from "@/components/windows/HumanWindowInputs/InnerSearch.vue";
import AddEditCarWindow from "@/components/windows/AddEditCarWindow.vue";
import {ItemType} from "@/js/utils.js";

const selectedCar = ref(null);
const Error = ref('');

const selectCar = (car) => {
  selectedCar.value = car;
};

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