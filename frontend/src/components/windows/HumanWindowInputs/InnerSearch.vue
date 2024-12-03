<template>
  <div class="relative w-full">
    <div class="flex items-center justify-center">
      <SelectorFieldInnerSearch :item-type="itemType" ref="fieldSelector" />
      <input
          type="text"
          v-model="searchQuery"
          @focus="showDropdown = true"
          @input="search"
          @keydown.enter.prevent
          class="w-1/2 p-2 border border-gray-600 rounded-r bg-gray-700 text-white focus:outline-none focus:ring-0"
          placeholder="Search"
          ref="dropdown"
      />
      <svg @click="addNewItem" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.2" stroke="currentColor" class="size-11 text-gray-500 ml-2 hover:text-white hover:bg-gray-700 rounded-full transition-colors duration-300 cursor-pointer p-2">
        <path stroke-linecap="round" stroke-linejoin="round" d="M12 9v6m3-3H9m12 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z" />
      </svg>
      <component :is="addEditWindow" v-if="showAddEditWindow" @close="closeAddEditWindow"
                 :title="'Add new car'" :type="AddEditWindowType.ADDING" class="fixed inset-0 z-50" />
    </div>
    <div v-if="showDropdown && items.length > 0" class="absolute ml-[54px] left-1/2 transform -translate-x-1/2 w-1/2 bg-gray-700 border border-gray-600 rounded mt-1 z-10">
      <div v-for="item in items" :key="item.id" @click="selectItem(item)" class="p-2 hover:bg-gray-600 cursor-pointer flex justify-between">
        <p v-if="itemType === ItemType.CAR_HUMAN_SEARCH">{{ item.name }}</p>
        <p v-if="itemType === ItemType.COORDINATES_HUMAN_SEARCH">({{ item.x }}; {{item.y}})</p>
        <p>{{ item.author }}</p>
      </div>
      <PaginationItems :item-type="itemType" ref="paginationButtons" />
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, onUnmounted, ref } from 'vue';
import { AddEditWindowType, ItemType } from "@/js/utils.js";
import SelectorFieldInnerSearch from "@/components/windows/HumanWindowInputs/SelectorFieldInnerSearch.vue";
import { get, objects } from "@/js/items-ws.js";
import PaginationItems from "@/components/shared_comps/PaginationItems.vue";

const searchQuery = ref('');
const showDropdown = ref(false);
const showAddEditWindow = ref(false);
const fieldSelector = ref(null);
const dropdown = ref(null);
const paginationButtons = ref(null);

const items = computed(() => {
  return objects.value[props.itemType].data;
});

const props = defineProps({
  addEditWindow: Object,
  itemType: Number
});

const addNewItem = () => {
  showAddEditWindow.value = true;
};

const closeAddEditWindow = () => {
  showAddEditWindow.value = false;
};

const emit = defineEmits(['choose']);

const selectItem = (item) => {
  emit('choose', item);
};

const handleClickOutside = (event) => {
  if (dropdown.value && !dropdown.value.contains(event.target) && !paginationButtons.value?.div.contains(event.target)) {
    showDropdown.value = false;
  }
};

onMounted(() => {
  document.addEventListener('click', handleClickOutside);
});

const search = () => {
  objects.value[props.itemType].searchBy = fieldSelector.value.selectedOption.backName;
  objects.value[props.itemType].searchTemplate = searchQuery.value;
  get(props.itemType);
};

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside);
  objects.value[props.itemType].searchBy = 'id';
  objects.value[props.itemType].searchTemplate = '';
  get(props.itemType);
});
</script>

<style scoped>
</style>