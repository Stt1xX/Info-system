<template>
  <div v-if="isAdditionalMenuVisible" class="fixed inset-0 flex flex-col items-center justify-center bg-black bg-opacity-50 z-50">
    <h1 class="text-2xl font-bold mb-6 text-center">Additional functionality</h1>
    <div class="bg-gray-800 p-4 rounded-lg shadow-lg w-[860px] flex flex-col justify-between">
      <ul class="list-none p-0 rounded-lg border border-gray-600">
        <li :class="{'bg-gray-600': selectedItem === 1, 'hover:bg-gray-700': selectedItem !== 1}"
            class="p-2 border-b border-gray-600 first:rounded-t-lg last:rounded-b-lg last:border-b-0 cursor-pointer"
            @click="toggleSelection(1)">
          <div class="flex justify-between">
            <div>
              <div class="font-bold text-white">Set the mood</div>
              <div class="text-gray-400">Change all the characters' mood to the saddest possible one.</div>
            </div>
          </div>
        </li>
        <li :class="{'bg-gray-600': selectedItem === 2, 'hover:bg-gray-700': selectedItem !== 2}"
            class="p-2 border-b border-gray-600 first:rounded-t-lg last:rounded-b-lg last:border-b-0 cursor-pointer"
            @click="toggleSelection(2)">
          <div class="flex justify-between">
            <div>
              <div class="font-bold text-white">Transfer the heroes</div>
              <div class="text-gray-400">Transfer all the heroes who do not have a car to the red Lada Kalina.</div>
            </div>
          </div>
        </li>
        <li :class="{'bg-gray-600': selectedItem === 3, 'hover:bg-gray-700': selectedItem !== 3}"
            class="p-2 border-b border-gray-600 first:rounded-t-lg last:rounded-b-lg last:border-b-0 cursor-pointer"
            @click="toggleSelection(3)">
          <div class="flex flex-col">
            <div class="flex justify-between items-center">
              <div>
                <div class="font-bold text-white">
                  Return the number
                  <span v-if="showSoundtrackNameWarning" class="text-red-500 text-sm">Soundtrack name must not be empty!</span>
                </div>
                <div class="text-gray-400">Return the number of objects whose soundtrack Name field value is less than the specified one.</div>
              </div>
              <input type="text" placeholder="Soundtrack name" v-model="soundtrackName" class="ml-4 p-1 rounded bg-gray-700 text-white border border-gray-500 focus:ring-2 focus:outline-none focus:ring-blue-500" />
            </div>
          </div>
        </li>
        <li :class="{'bg-gray-600': selectedItem === 4, 'hover:bg-gray-700': selectedItem !== 4}"
            class="p-2 border-b border-gray-600 first:rounded-t-lg last:rounded-b-lg last:border-b-0 cursor-pointer"
            @click="toggleSelection(4)">
          <div class="flex flex-col">
            <div class="flex justify-between items-center">
              <div>
                <div class="font-bold text-white">
                  Delete person
                  <span v-if="showImpactSpeedWarning" class="text-red-500 text-sm">{{ impactSpeedWarningText }}</span>
                </div>
                <div class="text-gray-400">Delete one (any) person whose impact Speed field value is equivalent to the specified one.</div>
              </div>
              <input type="text" v-model="impactSpeed" placeholder="Impact speed" class="ml-4 p-1 rounded bg-gray-700 text-white border border-gray-500 focus:ring-2 focus:outline-none focus:ring-blue-500" />
            </div>
          </div>
        </li>
      </ul>
      <div class="flex justify-between items-center mt-6">
        <button @click="applyFunc" type="button"
                class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
          Apply
        </button>
        <div v-if="showNoSelectionWarning" class="text-red-500">No option selected!</div>
        <button @click="closeMenu" type="button"
                class="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
          Close
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const selectedItem = ref(null);
const soundtrackName = ref('');
const impactSpeed = ref('');
const showSoundtrackNameWarning = ref(false);
const showImpactSpeedWarning = ref(false);
const impactSpeedWarningText = ref('');
const showNoSelectionWarning = ref(false);

const props = defineProps({
  isAdditionalMenuVisible: Boolean
});

const emit = defineEmits(['close', 'apply']);

const toggleSelection = (index) => {
  selectedItem.value = selectedItem.value === index ? null : index;
};

const validate = () => {
  showNoSelectionWarning.value = false;
  if (selectedItem.value === null) {
    showNoSelectionWarning.value = true;
    return false;
  }
  if (selectedItem.value === 3) {
    showSoundtrackNameWarning.value = !soundtrackName.value;
    return !showSoundtrackNameWarning.value;
  } else if (selectedItem.value === 4) {
    if (!impactSpeed.value) {
      impactSpeedWarningText.value = 'Impact speed must be not empty!';
      showImpactSpeedWarning.value = true;
    } else if (isNaN(impactSpeed.value)) {
      impactSpeedWarningText.value = 'Impact speed must be a number!';
      showImpactSpeedWarning.value = true;
    } else {
      showImpactSpeedWarning.value = false;
    }
    return !showImpactSpeedWarning.value;
  }
  return true;
};

const applyFunc = () => {
  if (validate()) {
    emit('apply');
    closeMenu();
  }
};

const closeMenu = () => {
  emit('close');
  showImpactSpeedWarning.value = false;
  showSoundtrackNameWarning.value = false;
  showNoSelectionWarning.value = false;
  soundtrackName.value = '';
  selectedItem.value = null;
  impactSpeed.value = '';
  impactSpeedWarningText.value = '';
};
</script>