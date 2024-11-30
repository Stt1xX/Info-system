<template>
  <button type="button" @click="navigateToCarTable" class="table-btn">
    <svg class="fill-current w-4 h-4 mr-2" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
      <path d="M3 3h18v18H3V3zm2 2v2h2V5H5zm4 0v2h2V5H9zm4 0v2h2V5h-2zm4 0v2h2V5h-2zM5 9v2h2V9H5zm4 0v2h2V9H9zm4 0v2h2V9h-2zm4 0v2h2V9h-2zM5 13v2h2v-2H5zm4 0v2h2v-2H9zm4 0v2h2v-2h-2zm4 0v2h2v-2h-2zM5 17v2h2v-2H5zm4 0v2h2v-2H9zm4 0v2h2v-2h-2zm4 0v2h2v-2h-2z"/>
    </svg>
    <span>Cars</span>
  </button>
</template>

<script setup>

import router from "@/routes/routes.js";
import {onMounted} from "vue";
import {connect} from "@/js/items-ws.js";
import {ItemType} from "@/js/utils.js";

const navigateToCarTable = () => {
  router.push("/carTable")
}

onMounted(() => {
  connect(ItemType.CAR)
})

</script>

<style scoped>
.table-btn {
  @apply flex items-center bg-transparent hover:bg-blue-500 text-blue-500 hover:text-white font-bold py-2 px-4 rounded;
}
</style>