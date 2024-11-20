<template>
  <div v-if="isConfirmDeleteMenuVisible" class="fixed inset-0 flex flex-col items-center justify-center bg-black bg-opacity-50 z-50">
    <h1 class="text-2xl font-bold mb-6 text-center text-white">Confirmation</h1>
    <div class="bg-gray-800 p-4 rounded-lg shadow-lg w-96">
      <form class="space-y-4">
        <div class="form-group">
          Are you sure you want to delete the <span class="font-bold">{{author}}'s {{itemName}}</span> with id: <span class="font-bold">{{id}}</span>?
        </div>
        <div class="flex justify-between items-center mt-4">
          <button @click="confirmFunc" type="button" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">Yes</button>
          <button @click="closeMenu" type="button" class="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">No</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  isConfirmDeleteMenuVisible: Boolean,
  itemName: String,
  id: String,
  author : String
});

const emit = defineEmits(['close', 'apply']);

const confirmFunc = () => {
  emit('apply');
  closeMenu();
};

const closeMenu = () => {
  emit('close');
};
</script>

<style scoped>
</style>