<template>
  <button type="button" @click="logout" class="logout-btn">
    <svg class="fill-current w-4 h-4 mr-2" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
      <path d="M10 2v2H4v16h6v2H2V2h8zm12 10l-4-4v3H8v2h10v3l4-4z"/>
    </svg>
    <span>Log out</span>
  </button>
</template>

<script setup>
import {defineProps, inject} from 'vue'
import router from '@/routes/routes.js'
const logout_header = inject('logout')
const props = defineProps({
  token: String
})

const logout = () => {
  $.ajax({
    url: '/logout',
    type: 'POST',
    headers: {
      'X-CSRF-Token': props.token
    },
    success: () => {
      logout_header()
      router.push('/login')
    }
  })
}
</script>

<style scoped>
.logout-btn {
  @apply flex items-center bg-transparent hover:bg-blue-500 text-blue-500 hover:text-white font-bold py-2 px-4 rounded;
}
</style>