<template>
  <div class="fixed inset-0 flex flex-col items-center justify-center bg-black bg-opacity-50 z-40 text-white">
    <h1 class="text-2xl font-bold mb-6 text-center">History</h1>
    <div class="bg-gray-800 p-4 rounded-lg shadow-lg w-[560px] flex flex-col justify-between min-h-[615.2px]">
      <ul class="list-none p-0 rounded-lg border border-gray-600">
        <li v-for="commit in commits" :key="commit.id" class="p-2 hover:bg-gray-700 border-b border-gray-600 first:rounded-t-lg last:rounded-b-lg last:border-b-0">
          <div class="flex justify-between">
            <div>
              <div class="font-bold text-white">{{ commit.id }}</div>
              <div class="text-gray-400">{{ commit.status }}</div>
            </div>
            <div class="text-gray-500 self-center">{{ commit.author }}</div>
            <div class="self-center">
              <div class="text-gray-400">{{ commit.accepted }} / {{ commit.all }}</div>
            </div>
          </div>
        </li>
      </ul>
      <div class="flex justify-between items-center mt-6">
        <PaginationImportHistory/>
        <button @click="closeWindow" type="button" class="bg-red-500 hover:bg-red-700text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">Close</button>
      </div>
    </div>
  </div>
</template>


<script setup>
import {onMounted, onUnmounted , ref } from "vue";
import PaginationImportHistory from "@/components/shared_comps/PaginationImportHistory.vue";
// import {connect, disconnect, commits, pageNumber} from "@/js/audit-ws.js";

let pageNumber = ref(0);
let commits = ref([{
  id : 1,
  status : "succeed",
  author : "Timur",
  accepted : "5",
  all : "7"
}]);

const emit = defineEmits(['close']);

const closeWindow = () => {
  emit('close');
};

onMounted(() => {
  // connect(props.id, props.itemCode)
})

onUnmounted(() => {
  // disconnect(props.id, props.itemCode)
  pageNumber.value = 0
  commits.value = []
})

</script>

<style scoped>
</style>