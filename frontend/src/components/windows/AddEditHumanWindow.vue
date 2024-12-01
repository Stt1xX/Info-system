<template>
  <div v-if="visible" class="fixed inset-0 flex flex-col items-center justify-center bg-black bg-opacity-50">
    <h1 class="text-2xl font-bold mb-6 text-center">{{ title }}</h1>
    <div class="bg-gray-800 p-6 rounded-lg shadow-lg w-[900px] max-w-3xl">
      <div class="relative flex justify-between mb-10">
        <div v-for="(step, index) in steps" :key="index" class="relative z-10 flex flex-col items-center">
          <div class="mb-4 text-white text-lg">{{ step }}</div>
          <div @click="goToStep(index + 1)" :class="['w-10 h-10 rounded-full flex items-center justify-center text-white cursor-pointer', { 'bg-blue-500': currentStep >= index + 1, 'bg-gray-500': currentStep < index + 1 }]">{{ index + 1 }}</div>
        </div>
      </div>
      <div class="h-0.5 bg-gray-500 mb-10"></div>
      <form @submit.prevent="nextStep">
        <div class="h-[310px]">
          <SettingCar v-show="currentStep === AddHumanPageNumber.SET_CAR" :ref="el => pages[AddHumanPageNumber.SET_CAR] = el"/>
          <SettingCoordinates v-show="currentStep === AddHumanPageNumber.SET_COORDINATES" :ref="el => pages[AddHumanPageNumber.SET_COORDINATES] = el"/>
          <SettingHuman v-show="currentStep === AddHumanPageNumber.SET_HUMAN" :ref="el => pages[AddHumanPageNumber.SET_HUMAN] = el"/>
          <Preview v-if="currentStep === AddHumanPageNumber.PREVIEW" :ref="el => pages[AddHumanPageNumber.PREVIEW] = el" :data="formData"/>
        </div>
        <div class="flex justify-between items-center mt-10">
          <div>
            <button @click="prevStep" type="button" :disabled="currentStep === 1"
                    :class="['bg-gray-500 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline mr-2', { 'hover:bg-gray-700': currentStep !== 1, 'opacity-50': currentStep === 1 }]">
              Back
            </button>
            <button type="submit"
                    class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
              {{ currentStep === steps.length ? 'Create' : 'Next' }}
            </button>
          </div>
          <button @click="closeModal" type="button"
                  class="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
            Cancel
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import {ref, watch} from 'vue';
import SettingCar from "@/components/windows/HumanWindowInputs/SettingCar.vue";
import SettingCoordinates from "@/components/windows/HumanWindowInputs/SettingCoordinates.vue";
import Preview from "@/components/windows/HumanWindowInputs/Preview.vue";
import SettingHuman from "@/components/windows/HumanWindowInputs/SettingHuman.vue";
import {AddHumanPageNumber} from "@/js/utils.js";

const props = defineProps({
  visible: Boolean,
  title: String,
  type: Number,
});

const emit = defineEmits(['close']);
const pages = ref([]);
const steps = ['Setting Car', 'Setting Coordinates', 'Setting Human', 'Review'];
const currentStep = ref(1);

const formData = ref({
  car: {},
  coordinates: {},
  human: {},
});

watch(
    () => pages.value[AddHumanPageNumber.SET_HUMAN]?.formData,
    (newValue) => {
      formData.value.human = newValue;
    }
);

watch(
    () => pages.value[AddHumanPageNumber.SET_CAR]?.selectedCar,
    (newValue) => {
      formData.value.car = newValue;
    }
);

watch(
    () => pages.value[AddHumanPageNumber.SET_COORDINATES]?.selectedCoordinates,
    (newValue) => {
      formData.value.coordinates = newValue;
    }
);


const closeModal = () => {
  currentStep.value = 1;
  emit('close');
};

const nextStep = () => {
  if (currentStep.value !== AddHumanPageNumber.PREVIEW){
    if (!pages.value[currentStep.value].validate()) {
      return
    }
  }
  if (currentStep.value < steps.length) {
    currentStep.value++;
  } else {
    saveData();
  }
};

const prevStep = () => {
  if (currentStep.value > 1) {
    currentStep.value--;
  }
};

const goToStep = (step) => {
  if (step <= currentStep.value) {
    currentStep.value = step;
  }
};

const saveData = () => {
  // save data
  closeModal();
};
</script>