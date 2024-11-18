<template>
  <div class="max-w-sm rounded overflow-hidden shadow-lg bg-gray-700 relative">
    <button @click="toggleMenu" class="absolute top-2 right-2 p-2 rounded-full hover:bg-gray-600">
      <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v.01M12 12v.01M12 18v.01"></path>
      </svg>
    </button>
    <DropdownMenu :visible="menuVisible" @onClose="closeMenu" />
    <div class="px-6 py-4 flex items-center">
      <div class="flex-grow w-52">
        <div class="font-bold text-xl mb-2">{{car.name}}</div>
        <p class="text-base">
          id: {{ car.id }}<br>
          isCool: {{ car.cool }}<br>
          author: {{car.author}}<br>
        </p>
      </div>
      <CarLogo class="w-[108px] h-[108px] mr-14" />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import CarLogo from "@/components/logos/CarLogo.vue";
import DropdownMenu from "@/components/DropdownMenu.vue";

const props = defineProps({
  car: Object
});

const menuVisible = ref(false);

const toggleMenu = () => {
  menuVisible.value = !menuVisible.value;
};

const closeMenu = () => {
  menuVisible.value = false;
};
</script>

<style scoped>
button {
  transition: background-color 0.3s;
}
</style>