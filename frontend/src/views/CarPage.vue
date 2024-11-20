<template>
  <div class="min-h-screen flex flex-col bg-gray-900 text-white">
    <main class="flex-grow container mx-auto p-4">
      <Table :item="CarItem" :add-edit-window="AddEditCarWindow" :objs="cars" :header="'Cars'" :name="'car'"/>
    </main>
  </div>
</template>

<script setup>
import Table from '@/components/shared_comps/Table.vue';
import CarItem from "@/components/items/CarItem.vue";
import AddEditCarWindow from "@/components/windows/AddEditCarWindow.vue";

import {cars} from "@/js/cars-ws.js";

</script>