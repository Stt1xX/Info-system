<!-- frontend/src/components/DropdownMenu.vue -->
<template>
  <div v-if="visible" class="absolute top-10 right-2 bg-white text-black rounded shadow-lg">
    <ul>
      <li @click="showDetails" class="px-4 py-2 hover:bg-gray-200 cursor-pointer">Details</li>
      <li @click="deleteItem" class="px-4 py-2 hover:bg-gray-200 cursor-pointer">Delete</li>
      <li @click="editItem" class="px-4 py-2 hover:bg-gray-200 cursor-pointer">Edit</li>
    </ul>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';

const props = defineProps({
  visible: Boolean,
  onClose: Function
});

const showDetails = () => {
  // Implement show details logic
};

const deleteItem = () => {
  // Implement delete item logic
};

const editItem = () => {
  // Implement edit item logic
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
}
</style>