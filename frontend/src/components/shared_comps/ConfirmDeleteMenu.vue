<template>
  <div v-if="isConfirmDeleteMenuVisible" class="fixed inset-0 flex flex-col items-center justify-center bg-black bg-opacity-50 z-50">
    <h1 class="text-2xl font-bold mb-6 text-center text-white">Confirmation</h1>
    <div class="bg-gray-800 p-4 rounded-lg shadow-lg w-96">
      <form class="space-y-4">
        <div class="form-group">
          Are you sure you want to delete the <span class="font-bold">{{author}}'s {{getItemName()}}</span> with id: <span class="font-bold">{{id}}</span>?
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
import {ItemType} from "@/js/utils.js";

const props = defineProps({
  isConfirmDeleteMenuVisible: Boolean,
  itemCode: Number,
  id: String,
  author : String
});

const getItemName = () => {
  switch (props.itemCode){
    case 1:
      return 'car';
    case 2:
      return 'coordinates';
    case 3:
      return 'human';
    default:
      return 'UNDEFINED';
  }
}

const emit = defineEmits(['close']);

const confirmFunc = () => {
  delete_obj()
  closeMenu();
};

const closeMenu = () => {
  emit('close');
};

const delete_obj = () => {
  switch(props.itemCode){
    case(ItemType.CAR) : {
      $.ajax({
        type: 'DELETE',
        url: 'cars/delete/' + props.id,
      }) /// NOT DONE
    }
  }
}
</script>

<style scoped>
</style>