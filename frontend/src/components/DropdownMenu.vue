<template>
  <div v-if="visible" class="dropdown-menu absolute top-10 right-2 bg-gray-800 text-white rounded shadow-lg">
    <ul>
      <li @click="showDetails" class="px-4 py-2 hover:bg-gray-600 cursor-pointer flex items-center">
        <svg fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 mr-2">
          <path stroke-linecap="round" stroke-linejoin="round" d="M9.879 7.519c1.171-1.025 3.071-1.025 4.242 0 1.172 1.025 1.172 2.687 0 3.712-.203.179-.43.326-.67.442-.745.361-1.45.999-1.45 1.827v.75M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Zm-9 5.25h.008v.008H12v-.008Z" />
        </svg>
        Details
      </li>
      <li @click="editItem" class="px-4 py-2 hover:bg-gray-600 cursor-pointer flex items-center">
        <svg fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 mr-2">
          <path stroke-linecap="round" stroke-linejoin="round" d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10" />
        </svg>
        Edit
      </li>
      <li @click="deleteItem" class="px-4 py-2  hover:bg-red-500 hover:text-white text-red-500 cursor-pointer flex items-center">
        <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" class="w-4 h-4 mr-2" stroke-width="2">
          <path d="m14.74 9-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 0 1-2.244 2.077H8.084a2.25 2.25 0 0 1-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 0 0-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 0 1 3.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 0 0-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 0 0-7.5 0" />
        </svg>
        Delete
      </li>
    </ul>
    <component :is="formFor" :visible="isVisible" @close="closeAddEditWindow" :title="'Edit ' + name" :item="item" />
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';

const props = defineProps({
  visible: Boolean,
  onClose: Function,
  item: Object,
  formFor : Object,
  name : String
});

const isVisible = ref(false);


const showDetails = () => {
  // Implement show details logic
};

const deleteItem = () => {
  // Implement delete item logic
};

const editItem = () => {
  isVisible.value = true;
};

const closeAddEditWindow = () => {
  isVisible.value = false;
};

const handleClickOutside = (event) => {
  if (!event.target.closest('.dropdown-menu')) {
    props.onClose();
  }
};

onMounted(() => {
  document.addEventListener('click', handleClickOutside);
});

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside);
});
</script>

<style scoped>
.dropdown-menu {
  transition: background-color 0.3s;
  z-index: 50;
}

li {
  line-height: 1.25; /* Reduce the spacing between lines */
}
</style>