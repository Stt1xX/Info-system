<template>
  <h1 class="text-2xl font-bold mb-6 text-center">Cars</h1>
  <div class="bg-gray-800 p-8 rounded-lg shadow-lg max-w-7xl mx-auto">
      <div class="grid grid-cols-3 gap-4">
          <CarItem v-for="car in cars" :key="car" :car="car"/>
      </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import CarItem from "@/components/items/CarItem.vue";
const cars = ref([
  {id : 1, name : "Car1", cool : true, author : "Bob"},
  {id : 2, name : "Car2", cool : true, author : "Bob" },
  {id : 3, name : "Car3", cool : true, author : "Bob" },
  {id : 4, name : "Car4", cool : true, author : "Bob" },
  {id : 5, name : "Car5", cool : true, author : "Bob" },
])
</script>

<style scoped>
</style>