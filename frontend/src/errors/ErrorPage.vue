<template>
  <div class="min-h-screen flex flex-col bg-gray-900 text-white">
    <div class="flex-grow flex flex-col items-center justify-center">
      <div class="w-1/3">
        <div class="flex justify-center mb-4">
          <GhostLogo />
        </div>
        <h1 class="text-2xl font-bold mb-6 text-center">Error</h1>
        <div class="error-container bg-gray-800 p-4 rounded-lg shadow-lg">
          <div class="error-message mt-4 p-3 bg-gray-700 border border-gray-600 rounded text-center">
            {{ errorMessage }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue'
import { useRoute } from 'vue-router'
import GhostLogo from "@/components/GhostLogo.vue";
const route = useRoute()
const error_code = decodeURIComponent(route.query.error_code)
const errorMessage = ref('An error occurred.')
onMounted(() => {
  switch (error_code){
    case '403':
      errorMessage.value = 'You do not have permission to view this page.'
      break
    case '404':
      errorMessage.value = 'Not found.'
      break
    default:
      errorMessage.value = 'An error occurred.'
  }
})
switch (error_code){
  case '403':
    errorMessage.value = 'You do not have permission to view this page.'
    break
}

</script>