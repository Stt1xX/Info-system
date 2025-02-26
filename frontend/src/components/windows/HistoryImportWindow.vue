<template>
  <div class="fixed inset-0 flex flex-col items-center justify-center bg-black bg-opacity-50 z-40 text-white">
    <h1 class="text-2xl font-bold mb-6 text-center">History</h1>
    <div class="bg-gray-800 p-4 rounded-lg shadow-lg w-[560px] flex flex-col justify-between min-h-[615.2px]">
      <ul v-if="imports.length > 0" class="list-none p-0 rounded-lg border border-gray-600">
        <li
            v-for="_import in imports"
            :key="_import.id"
            class="p-2 border-b hover:bg-gray-700 border-gray-600 first:rounded-t-lg last:rounded-b-lg last:border-b-0 transition-colors"
            :class="{ 'cursor-pointer': _import.importStatus === 'Success' }"
            @click="_import.importStatus === 'Success' && downloadFile(_import.id)"
        >
          <div class="grid grid-cols-[1fr_auto] gap-4 items-center">
            <!-- Левая часть -->
            <div class="flex flex-col">
              <div class="font-bold text-white">{{ _import.id }}</div>
              <div class="text-gray-400">{{ _import.authorName }}</div>
            </div>

            <!-- Правая часть -->
            <div class="flex flex-col items-end">
              <div v-if="_import.importStatus === 'Success'" class="text-gray-300 text-sm">
                cars: {{ _import.completedCars }} / coords: {{ _import.completedCoordinates }} / humans: {{ _import.completedHumans }}
              </div>
              <div
                  :class="{
                  'text-green-400 font-semibold': _import.importStatus === 'Success',
                  'text-red-400 font-semibold': _import.importStatus === 'Failed'
                }"
              >
                {{ _import.importStatus }}
              </div>
            </div>
          </div>
        </li>
      </ul>
      <div v-if="imports.length === 0" class="flex items-center justify-center h-full min-h-[200px] text-gray-400">
        It's quiet here so far...
      </div>
      <div class="flex justify-between items-center mt-6">
        <PaginationImportHistory />
        <button @click="closeWindow" type="button" class="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
          Close
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, onUnmounted } from "vue";
import PaginationImportHistory from "@/components/shared_comps/PaginationImportHistory.vue";
import { get, imports, pageNumber } from "@/js/import-history.js";

const emit = defineEmits(["close"]);

const closeWindow = () => {
  emit("close");
};

const downloadFile = (id) => {
  window.location.href = `/minio/download/${id}`;
};

onMounted(() => {
  get();
});

onUnmounted(() => {
  pageNumber.value = 0;
  imports.value = [];
});
</script>