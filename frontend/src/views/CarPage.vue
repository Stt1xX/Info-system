<template>
  <div class="min-h-screen flex flex-col bg-gray-900 text-white">
    <main class="flex-grow container mx-auto p-4">
      <Table :item="CarItem" :add-edit-window="AddEditCarWindow" :objs="cars" :header="'Cars'" :name="'car'"/>
    </main>
  </div>
</template>

<script setup>
import Table from '@/components/Table.vue';
import {ref} from "vue";
import CarItem from "@/components/items/CarItem.vue";
import AddEditCarWindow from "@/components/windows/AddEditCarWindow.vue";

const cars = ref([
  {id: 1, name: "Car1", cool: true, author: "Bob"},
  {id: 2, name: "Car2", cool: true, author: "Bob"},
  {id: 3, name: "Car3", cool: true, author: "Bob"},
  {id: 4, name: "Car4", cool: true, author: "Bob"},
  {id: 5, name: "Car5", cool: true, author: "Bob"},
]);

</script>