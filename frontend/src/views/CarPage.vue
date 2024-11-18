<template>
  <div class="min-h-screen flex flex-col bg-gray-900 text-white">
    <main class="flex-grow container mx-auto p-4">
      <CarTable />
    </main>
  </div>
</template>

<script setup>
import CarTable from '@/components/tables/CarTable.vue';
</script>