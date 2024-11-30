<template>
  <component :is="addEditWindow" :visible="isModalVisible" @close="hideModal"
             :title="'Add new ' + getItemName(itemType)" :type="AddEditWindowType.ADDING" class="fixed inset-0 z-50"/>
  <h1 class="text-2xl font-bold mb-6 mt-6 text-center">{{ getHeader(props.itemType) }}</h1>
  <div class="mx-auto max-w-7xl mb-6">
    <div class="flex items-center space-x-4 justify-between">
      <SearchInput class="w-[90%]" />
      <SortButton :itemType="itemType"/>
      <AdditionalFuncButton />
    </div>
  </div>
  <div class="bg-gray-800 p-8 rounded-lg shadow-lg max-w-7xl mx-auto">
    <div v-if="objects[itemType].data.length === 0" class="flex justify-center items-center h-64">
      <svg @click="showModal" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="0.5" stroke="white" class="w-24 h-24 rounded-full flex items-center justify-center hover:scale-110 transition-transform duration-300 cursor-pointer hover:z-0">
        <path stroke-linecap="round" stroke-linejoin="round" d="M12 9v6m3-3H9m12 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z" />
      </svg>
    </div>
    <div v-else class="grid grid-cols-3 gap-6">
      <div class="flex justify-center items-center w-full">
        <svg @click="showModal" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="0.5" stroke="white" class="w-24 h-24 rounded-full flex items-center justify-center hover:scale-110 transition-transform duration-300 cursor-pointer">
          <path stroke-linecap="round" stroke-linejoin="round" d="M12 9v6m3-3H9m12 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z" />
        </svg>
      </div>
      <component :is="item" v-for="obj in objects[itemType].data" :key="obj.id" :obj="obj"/>
    </div>
  </div>
  <PaginationButtons :itemType="itemType"/>
</template>

<script setup>
import {ref} from 'vue';
import SearchInput from "@/components/shared_comps/SearchInput.vue";
import SortButton from "@/components/buttons/SortButton.vue";
import AdditionalFuncButton from "@/components/buttons/AdditionalFuncButton.vue";
import PaginationButtons from "@/components/shared_comps/PaginationButtons.vue";
import {AddEditWindowType, getHeader, getItemName} from "@/js/utils.js";
import {objects} from "@/js/items-ws.js";


const props = defineProps({
  item: Object,
  addEditWindow: Object,
  itemType: Number,
  header: String,
});

const isModalVisible = ref(false);

const showModal = () => {
  isModalVisible.value = true;
};

const hideModal = () => {
  isModalVisible.value = false;
};

</script>

<style scoped>
.grid > * {
  width: 100%;
}
</style>