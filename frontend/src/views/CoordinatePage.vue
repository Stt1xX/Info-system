<template>
  <div class="min-h-screen flex flex-col bg-gray-900 text-white">
    <main class="flex-grow container mx-auto p-4">
      <Table :item="CoordsItem" :add-edit-window="AddEditCoordsWindow" :objs="coordinates" :header="'Coordinates'" :name="'coordinates'"/>
    </main>
  </div>
</template>

<script setup>
import Table from '@/components/shared_comps/Table.vue';
import CoordsItem from "@/components/items/CoordsItem.vue";
import AddEditCoordsWindow from "@/components/windows/AddEditCoordsWindow.vue";
import {coordinates} from "@/js/coordinates-ws.js";

</script>