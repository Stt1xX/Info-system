<template>
  <div class="min-h-screen flex flex-col bg-gray-900 text-white">
    <main class="flex-grow container mx-auto p-4">
      <Table :item="HumanItem" :add-edit-window="AddEditHumanWindow" :itemType="ItemType.HUMAN"/>
    </main>
  </div>
</template>

<script setup>
import Table from '@/components/shared_comps/Table.vue';
import HumanItem from "@/components/items/HumanItem.vue";
import AddEditHumanWindow from "@/components/windows/AddEditHumanWindow.vue";
import {ItemType} from "@/js/utils.js";

</script>