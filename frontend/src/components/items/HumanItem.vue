<template>
  <div class="w-full rounded shadow-lg bg-gray-700 relative">
    <button @click="toggleMenu" @click.stop class="absolute top-2 right-2 p-2 rounded-full hover:bg-gray-600">
      <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v.01M12 12v.01M12 18v.01"></path>
      </svg>
    </button>
    <DropdownMenu :visible="menuVisible" :onClose="closeMenu" :item="obj" :formFor="AddEditHumanWindow" :itemCode="ItemType.HUMAN"/>
    <div class="px-6 py-4 flex items-center">
      <div class="flex-grow w-52">
        <div class="font-bold text-xl mb-2">{{obj.name}}</div>
        <p class="text-base">
          id: {{ obj.id }}<br>
          author: {{obj.author}}<br>
        </p>
      </div>
      <HumanLogo class="w-[108px] h-[108px] mr-14" />
    </div>
  </div>
</template>

<script setup>
import {ref} from 'vue';
import HumanLogo from "@/components/logos/HumanLogo.vue";
import DropdownMenu from "@/components/shared_comps/DropdownMenu.vue";
import AddEditHumanWindow from '@/components/windows/AddEditHumanWindow.vue';
import {ItemType} from "@/js/utils.js";

const props = defineProps({
  obj: Object
});

const menuVisible = ref(false);

const toggleMenu = (event) => {
  event.stopPropagation();
  if (!menuVisible.value) {
    closeAllMenus();
  }
  menuVisible.value = !menuVisible.value;
};


const closeMenu = () => {
  menuVisible.value = false
};

const closeAllMenus = () => {
  const event = new CustomEvent('close-all-menus');
  window.dispatchEvent(event);
};

window.addEventListener('close-all-menus', closeMenu);

</script>

<style scoped>
button {
  transition: background-color 0.3s;
}
</style>