<template>
  <div class="w-full rounded shadow-lg bg-gray-700 relative">
    <button @click="toggleMenu" @click.stop class="absolute top-2 right-2 p-2 rounded-full hover:bg-gray-600">
      <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v.01M12 12v.01M12 18v.01"></path>
      </svg>
    </button>
    <DropdownMenu v-if="isMenuVisible" @edit="openEditWindow" @delete="openDeleteConfirm" @close="closeMenu" @history="openHistoryWindow"/>
    <div class="px-6 py-4 flex items-center">
      <div class="flex-grow w-52">
        <div class="font-bold text-xl mb-2">{{obj.name}}</div>
        <p class="text-base">
          id: {{ obj.id }}<br>
          isCool: {{ obj.cool }}<br>
          author: {{obj.author}}<br>
        </p>
      </div>
      <CarLogo class="w-[108px] h-[108px] mr-14" />
    </div>
  </div>
  <AddEditCarWindow v-if="isEditVisible" @close="closeEditWindow" :title="'Edit ' + getItemName(ItemType.CAR)"
                    :item="obj" :type="AddEditWindowType.EDITING"/>
  <ConfirmDeleteWindow v-if="isDeleteConfirmVisible"
                     :id="obj.id" :item-code="ItemType.CAR" :author="obj.author"
                     @close="closeDeleteConfirmWindow" />
  <HistoryWindow v-if="isHistoryVisible" @close="closeHistoryWindow"
                    :id="obj.id" :itemCode="ItemType.CAR"/>
</template>

<script setup>
import {ref} from 'vue';
import CarLogo from "@/components/logos/CarLogo.vue";
import DropdownMenu from "@/components/shared_comps/DropdownMenu.vue";
import AddEditCarWindow from '@/components/windows/AddEditCarWindow.vue';
import HistoryWindow from "@/components/windows/HistoryWindow.vue";
import {AddEditWindowType, getItemName, ItemType} from "@/js/utils.js";
import ConfirmDeleteWindow from "@/components/windows/ConfirmDeleteWindow.vue";

const props = defineProps({
  obj: Object
});

const isMenuVisible = ref(false);
const isEditVisible = ref(false);
const isDeleteConfirmVisible = ref(false);
const isHistoryVisible = ref(false);

const toggleMenu = (event) => {
  event.stopPropagation();
  if (!isMenuVisible.value) {
    closeAllMenus();
  }
  isMenuVisible.value = !isMenuVisible.value;
};
const closeMenu = () => {
  isMenuVisible.value = false
};
const closeAllMenus = () => {
  const event = new CustomEvent('close-all-menus');
    window.dispatchEvent(event);
};

window.addEventListener('close-all-menus', closeMenu);

const closeEditWindow = () => {
  isEditVisible.value = false;
};
const openEditWindow = () => {
  isEditVisible.value = true;
};

const closeDeleteConfirmWindow = () => {
  isDeleteConfirmVisible.value = false;
};
const openDeleteConfirm = () => {
  isDeleteConfirmVisible.value = true;
};

const closeHistoryWindow = () => {
  isHistoryVisible.value = false;
};
const openHistoryWindow = () => {
  isHistoryVisible.value = true;
};

</script>

<style scoped>
button {
  transition: background-color 0.3s;
}
</style>