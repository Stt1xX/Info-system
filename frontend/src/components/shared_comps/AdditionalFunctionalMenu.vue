<template>
  <div v-if="isAdditionalMenuVisible" class="fixed inset-0 flex flex-col items-center justify-center bg-black bg-opacity-50 z-50">
    <h1 class="text-2xl font-bold mb-6 text-center">Additional functionality</h1>
    <div class="bg-gray-800 p-4 rounded-lg shadow-lg w-[860px] flex flex-col justify-between">
      <ul class="list-none p-0 rounded-lg border border-gray-600">
        <li :class="{'bg-gray-600': selectedItem === AdditionalFunctionalType.SET_MOOD, 'hover:bg-gray-700': selectedItem !== AdditionalFunctionalType.SET_MOOD}"
            class="p-2 border-b border-gray-600 first:rounded-t-lg last:rounded-b-lg last:border-b-0 cursor-pointer"
            @click="toggleSelection(AdditionalFunctionalType.SET_MOOD)">
          <div class="flex justify-between">
            <div>
              <div class="font-bold text-white">Set the mood</div>
              <div class="text-gray-400">Change all the characters' mood to the saddest possible one.</div>
            </div>
          </div>
        </li>
        <li :class="{'bg-gray-600': selectedItem === AdditionalFunctionalType.TRANSFER_HEROES, 'hover:bg-gray-700': selectedItem !== AdditionalFunctionalType.TRANSFER_HEROES}"
            class="p-2 border-b border-gray-600 first:rounded-t-lg last:rounded-b-lg last:border-b-0 cursor-pointer"
            @click="toggleSelection(AdditionalFunctionalType.TRANSFER_HEROES)">
          <div class="flex justify-between">
            <div>
              <div class="font-bold text-white">Transfer the heroes</div>
              <div class="text-gray-400">Transfer all the heroes to the "Lada Kalina".</div>
            </div>
          </div>
        </li>
        <li :class="{'bg-gray-600': selectedItem === AdditionalFunctionalType.SOUNDTRACK_NUMBER, 'hover:bg-gray-700': selectedItem !== AdditionalFunctionalType.SOUNDTRACK_NUMBER}"
            class="p-2 border-b border-gray-600 first:rounded-t-lg last:rounded-b-lg last:border-b-0 cursor-pointer"
            @click="toggleSelection(AdditionalFunctionalType.SOUNDTRACK_NUMBER)">
          <div class="flex flex-col">
            <div class="flex justify-between items-center">
              <div>
                <div class="font-bold text-white">
                  Return the number of soundtracks
                  <span v-if="showSoundtrackNameWarning" class="text-red-500 text-sm">Soundtrack name must not be empty!</span>
                </div>
                <div class="text-gray-400">Return the number of objects whose Soundtrack name field value is less than the specified one.</div>
              </div>
              <input type="text" placeholder="Soundtrack name" v-model="soundtrackName" class="ml-4 p-1 rounded bg-gray-700 text-white border border-gray-500 focus:ring-2 focus:outline-none focus:ring-blue-500" />
            </div>
          </div>
        </li>
        <li :class="{'bg-gray-600': selectedItem === AdditionalFunctionalType.DELETE_BY_IMPACT_SPEED, 'hover:bg-gray-700': selectedItem !== AdditionalFunctionalType.DELETE_BY_IMPACT_SPEED}"
            class="p-2 border-b border-gray-600 first:rounded-t-lg last:rounded-b-lg last:border-b-0 cursor-pointer"
            @click="toggleSelection(AdditionalFunctionalType.DELETE_BY_IMPACT_SPEED)">
          <div class="flex flex-col">
            <div class="flex justify-between items-center">
              <div>
                <div class="font-bold text-white">
                  Delete person
                  <span v-if="showImpactSpeedWarning" class="text-red-500 text-sm">{{ impactSpeedWarningText }}</span>
                </div>
                <div class="text-gray-400">Delete one (any) person whose Impact speed field value is equivalent to the specified one.</div>
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
import {showAlert} from "@/js/custom-alert.js";
import {token} from "@/js/csrf-token.js";
import {AdditionalFunctionalType} from "@/js/utils.js";

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
    switch (selectedItem.value) {
      case AdditionalFunctionalType.SET_MOOD:
        setTheMood();
        break;
      case AdditionalFunctionalType.TRANSFER_HEROES:
        transferHeroes();
        break;
      case AdditionalFunctionalType.SOUNDTRACK_NUMBER:
        returnTheSoundtrackNumber();
        break;
      case AdditionalFunctionalType.DELETE_BY_IMPACT_SPEED:
        deletePersonByImpactSpeed();
        break;
    }
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

const setTheMood = () => {
  $.ajax({
    type: 'POST',
    url: '/business_logic/longing_mood',
    headers : {
      'X-CSRF-Token': token.value
    },
    success: (resp) => {
      console.log(resp)
      showAlert(resp)
    }
  });
}

const transferHeroes = () => {
  $.ajax({
    type: 'POST',
    url: '/business_logic/transfer_heroes',
    headers : {
      'X-CSRF-Token': token.value
    },
    success: (resp) => {
      showAlert(resp)
    }
  });
}

const returnTheSoundtrackNumber = () => {
  $.ajax({
    type: 'GET',
    url: `/business_logic/soundtrack_number_by_name/${soundtrackName.value}`,
    headers : {
      'X-CSRF-Token': token.value
    },
    success: (resp) => {
      showAlert(resp);
    }
  });
}

const deletePersonByImpactSpeed = () => {
  $.ajax({
    type: 'DELETE',
    url: `/business_logic/delete_person_by_impact_speed/${impactSpeed.value}`,
    headers : {
      'X-CSRF-Token': token.value
    },
    success: (resp) => {
      showAlert(resp)
    }
  });
}

</script>