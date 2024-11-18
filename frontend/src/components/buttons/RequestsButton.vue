<template>
  <button type="button" @click="navigateToRequests" class="requests-btn">
    <svg class="fill-current w-4 h-4 mr-2" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
      <path d="M20 4H4c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 4l-8 5-8-5V6l8 5 8-5v2z"/>
    </svg>
    <span>Requests</span>
  </button>
</template>

<script setup>
import router from '@/routes/routes.js'
import {onMounted} from "vue";
import {connect} from "@/js/requests-ws.js";

const props = defineProps({
  is_admin: Boolean
})

onMounted(() => {
    connect()
})

const navigateToRequests = () => {
  if (props.is_admin)
    router.push("/admin/messages")
  else router.push("/error/ErrorPage?message=403")
}
</script>

<style scoped>
.requests-btn {
  @apply flex items-center bg-transparent hover:bg-green-500 text-green-500 hover:text-white font-bold py-2 px-4 rounded;
}
</style>