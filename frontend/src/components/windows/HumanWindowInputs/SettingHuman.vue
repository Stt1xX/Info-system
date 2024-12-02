<template>
  <div>
    <label class="text-white mb-2 text-lg text-center block">Choose other parameters for your man:</label>
    <div class="form-container flex justify-center items-center h-full">
      <div class="form-group grid grid-cols-2 gap-4 w-full max-w-2xl">
        <div class="flex flex-col">
          <input v-model="selectedHuman.name" type="text" placeholder="Name" class="mb-1 p-2 rounded bg-gray-700 text-white w-full focus:outline-none focus:ring-2 focus:ring-blue-500"/>
          <div class="h-4 text-red-500">{{ errors.name }}</div>
        </div>
        <div class="flex flex-col">
          <input v-model="selectedHuman.soundtrackName" type="text" placeholder="Soundtrack Name" class="mb-1 p-2 rounded bg-gray-700 text-white w-full focus:outline-none focus:ring-2 focus:ring-blue-500"/>
          <div class="h-4 text-red-500">{{ errors.soundtrackName }}</div>
        </div>
        <div class="flex flex-col">
          <input v-model="selectedHuman.impactSpeed" type="text" placeholder="Impact Speed" class="mb-1 p-2 rounded bg-gray-700 text-white w-full focus:outline-none focus:ring-2 focus:ring-blue-500"/>
          <div class="h-4 text-red-500">{{ errors.impactSpeed }}</div>
        </div>
        <div class="flex flex-col">
          <input v-model="selectedHuman.minutesOfWaiting" type="text" placeholder="Minutes of Waiting" class="mb-1 p-2 rounded bg-gray-700 text-white w-full focus:outline-none focus:ring-2 focus:ring-blue-500"/>
          <div class="h-4 text-red-500">{{ errors.minutesOfWaiting }}</div>
        </div>
        <div class="flex flex-col">
          <CustomDropdown v-model="selectedHuman.weaponType" :options="weaponOptions" placeholder="Select Weapon Type" class="mb-1 w-full"/>
          <div class="h-4 text-red-500">{{ errors.weaponType }}</div>
        </div>
        <div class="flex flex-col">
          <CustomDropdown v-model="selectedHuman.mood" :options="moodOptions" placeholder="Select Mood" class="mb-1 w-full"/>
          <div class="h-4 text-red-500">{{ errors.mood }}</div>
        </div>
        <div class="flex flex-col items-center">
          <div class="flex items-center">
            <input v-model="selectedHuman.realHero" type="checkbox" class="mr-2 custom-checkbox"/>
            <label class="text-white">Real Hero</label>
          </div>
          <div class="h-4"></div>
        </div>
        <div class="flex flex-col items-center">
          <div class="flex items-center">
            <input v-model="selectedHuman.hasToothpick" type="checkbox" class="mr-2 custom-checkbox"/>
            <label class="text-white">Has Toothpick</label>
          </div>
          <div class="h-4"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import CustomDropdown from '@/components/windows/HumanWindowInputs/CustomDropdown.vue';

const selectedHuman = ref({hasToothpick : false, realHero : false});
const errors = ref({
  name: '',
  mood: '',
  impactSpeed: '',
  soundtrackName: '',
  minutesOfWaiting: '',
  weaponType: ''
});

const moodOptions = ref([]);
const weaponOptions = ref([]);

const validate = () => {
  let isValid = true;
  if (!selectedHuman.value.name) {
    errors.value.name = "Name can't be empty";
    isValid = false;
  } else {
    errors.value.name = '';
  }
  if (!selectedHuman.value.mood) {
    errors.value.mood = "Mood can't be empty";
    isValid = false;
  } else {
    errors.value.mood = '';
  }
  if (!selectedHuman.value.impactSpeed) {
    errors.value.impactSpeed = "Impact Speed can't be empty";
    isValid = false;
  } else if (isNaN(selectedHuman.value.impactSpeed)) {
    errors.value.impactSpeed = "Impact Speed must be a number";
    isValid = false;
  } else if (selectedHuman.value.impactSpeed > 768) {
    errors.value.impactSpeed = "Impact Speed must be less than 769";
    isValid = false;
  } else {
    errors.value.impactSpeed = '';
  }
  if (!selectedHuman.value.soundtrackName) {
    errors.value.soundtrackName = "Soundtrack Name can't be empty";
    isValid = false;
  } else {
    errors.value.soundtrackName = '';
  }
  if (!selectedHuman.value.minutesOfWaiting) {
    errors.value.minutesOfWaiting = "Minutes of Waiting can't be empty";
    isValid = false;
  } else if (isNaN(selectedHuman.value.minutesOfWaiting)) {
    errors.value.minutesOfWaiting = "Minutes of Waiting must be a number";
    isValid = false;
  } else {
    errors.value.minutesOfWaiting = '';
  }
  if (!selectedHuman.value.weaponType) {
    errors.value.weaponType = "Weapon Type can't be empty";
    isValid = false;
  } else {
    errors.value.weaponType = '';
  }
  return isValid;
};

onMounted(() => {
  $.ajax({
    url: '/humans/get_weapon_types',
    type: 'GET',
    success: function (data) {
      weaponOptions.value = data;
    }
  });
  $.ajax({
    url: '/humans/get_moods',
    type: 'GET',
    success: function (data) {
      moodOptions.value = data;
    }
  });
});

defineExpose({
  validate,
  selectedHuman
});
</script>

<style scoped>
.custom-checkbox {
  width: 20px;
  height: 20px;
}
</style>