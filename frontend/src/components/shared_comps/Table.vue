<template>
  <h1 class="text-2xl font-bold mb-6 mt-6 text-center">{{ header }}</h1>
  <div class="mx-auto max-w-7xl mb-6">
    <div class="flex items-center space-x-4 justify-between">
      <SearchInput class="w-[90%]"/>
      <SortButton />
      <AdditionalFuncButton />
    </div>
  </div>
  <div class="bg-gray-800 p-8 rounded-lg shadow-lg max-w-7xl mx-auto">
    <div class="grid grid-cols-3 gap-4">
      <div class="flex justify-center items-center">
        <svg @click="showModal" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="0.5" stroke="white" class="w-24 h-24 rounded-full flex items-center justify-center hover:scale-110 transition-transform duration-300 cursor-pointer">
          <path stroke-linecap="round" stroke-linejoin="round" d="M12 9v6m3-3H9m12 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z" />
        </svg>
      </div>
      <component :is="item" v-for="obj in objs" :key="obj.id" :obj="obj"/>
    </div>
  </div>
  <component :is="addEditWindow" :visible="isModalVisible" @close="hideModal"
             :title="'Add new ' + name" :type="AddEditWindowType.ADDING"/>
</template>

<script setup>
import {ref} from 'vue'
import SearchInput from "@/components/shared_comps/SearchInput.vue";
import SortButton from "@/components/buttons/SortButton.vue";
import AdditionalFuncButton from "@/components/buttons/AdditionalFuncButton.vue";
import {AddEditWindowType} from "@/js/utils.js";

const props = defineProps({
  objs: Array,
  item: Object,
  addEditWindow: Object,
  header : String,
  name : String
})

const isModalVisible = ref(false);

const showModal = () => {
  isModalVisible.value = true;
};

const hideModal = () => {
  isModalVisible.value = false;
};

</script>

<style scoped>
</style>