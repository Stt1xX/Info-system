<template>
  <button type="button" @click="del_user" class="delete-btn">
    <svg class="fill-current w-4 h-4 mr-2" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
      <path d="M3 6h18v2H3V6zm2 3h14v13H5V9zm3 2v9h2v-9H8zm4 0v9h2v-9h-2zm4 0v9h2v-9h-2z"/>
    </svg>
    <span>Delete account</span>
  </button>
</template>

<script setup>
import {defineProps} from 'vue'
import router from '@/routes/routes.js'

const props = defineProps({
  token: String,
})

const emit = defineEmits()

const del_user = () => {
  $.ajax({
    url: '/users/delete_user',
    type: 'POST',
    headers: {
      'X-CSRF-Token': props.token
    },
    contentType: "text/plain",
    success: () => {
      $.ajax({
        url: '/logout',
        type: 'POST',
        headers: {
          'X-CSRF-Token': props.token
        },
        success: () => {
          router.push('/login')
          emit('logout')
        }
      })
    }
  })
}
</script>

<style scoped>
.delete-btn {
  @apply flex items-center bg-transparent hover:bg-red-500 text-red-500 hover:text-white font-bold py-2 px-4 rounded;
}
</style>