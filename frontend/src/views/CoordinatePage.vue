<template>
  <div class="min-h-screen flex flex-col bg-gray-900 text-white">
    <main class="flex-grow container mx-auto p-4">
      <Table :item="CoordsItem" :add-edit-window="AddEditCoordsWindow" :objs="coords" :header="'Coordinates'" :name="'coordinates'"/>
    </main>
  </div>
</template>

<script setup>
import Table from '@/components/Table.vue';
import {ref} from "vue";
import CoordsItem from "@/components/items/CoordsItem.vue";
import AddEditCoordsWindow from "@/components/windows/AddEditCoordsWindow.vue";

const coords = ref([
  {id: 1, x: 123, y:123.5, author: "Bob"},
  {id: 2, x: 123.123, y:123.5, author: "Bob"},
  {id: 3, x: 123, y:123.5, author: "Bob"},
  {id: 4, x: 123, y:123.5, author: "Bob"},
  {id: 5, x: 123, y:123.5, author: "Bob"},

]);

</script>