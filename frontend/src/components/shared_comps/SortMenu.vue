<template>
  <div v-if="isSortMenuVisible" class="fixed inset-0 flex flex-col items-center justify-center bg-black bg-opacity-50 z-50">
    <h1 class="text-2xl font-bold mb-6 text-center">Sorting</h1>
    <div class="bg-gray-800 p-4 rounded-lg shadow-lg w-[360px]">
      <form class="space-y-4">
        <div class="form-group">
          <div class="flex flex-col space-y-2">
            <div v-for="option in options" :key="option" class="p-2 rounded flex items-center justify-between bg-gray-700">
              <span>{{ option }}</span>
              <div class="flex">
                <span @click="selectDirection(true, option)" :class="{'bg-gray-600': selectedOption === option && selectedDirection === true}" class="ml-2 hover:bg-gray-600 w-8 h-8 flex items-center justify-center rounded-full cursor-pointer">
                  <svg fill="none" viewBox="0 0 24 24" stroke-width="1" stroke="currentColor" class="size-6">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M8.25 6.75 12 3m0 0 3.75 3.75M12 3v18" />
                  </svg>
                </span>
                <span @click="selectDirection(false, option)" :class="{'bg-gray-600': selectedOption === option && selectedDirection === false}" class="ml-2 hover:bg-gray-600 w-8 h-8 flex items-center justify-center rounded-full cursor-pointer">
                  <svg fill="none" viewBox="0 0 24 24" stroke-width="1" stroke="currentColor" class="size-6">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 17.25 12 21m0 0-3.75-3.75M12 21V3" />
                  </svg>
                </span>
              </div>
            </div>
          </div>
        </div>
        <div class="flex justify-between items-center mt-4">
          <button @click="applySort" type="button" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">Apply</button>
          <button @click="closeMenu" type="button" class="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">Close</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import {getUrlPrefix} from "@/js/utils.js";
import {get, order, sortBy} from "@/js/cars-ws.js";

const selectedOption = ref('id');
const selectedDirection = ref(true);

const props = defineProps({
  isSortMenuVisible: Boolean,
  itemType: Number
});

const emit = defineEmits(['close']);

const applySort = () => {
  sortBy.value = selectedOption.value;
  order.value = selectedDirection.value;
  get();
  closeMenu();
};

const closeMenu = () => {
  selectedOption.value = sortBy.value
  selectedDirection.value = order.value
  emit('close');
};

const selectDirection = (direction, option) => {
  selectedOption.value = option;
  selectedDirection.value = direction;
};

const options = ref([]);

onMounted(() => {
  $.ajax({
    type: 'GET',
    url: getUrlPrefix(props.itemType) + '/get_sortable_fields',
    success: (res) => {
      options.value = res;
    }
  });
});
</script>