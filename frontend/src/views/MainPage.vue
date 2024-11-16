<template>
  <div class="min-h-screen flex flex-col bg-gray-900 text-white">
    <Header :token="token" :username="name" />
    <main class="flex-grow container mx-auto p-4">
      <div class="flex justify-between items-center mb-4">
        <p class="text-xl font-semibold">{{ name }}</p>
      </div>
      <div v-if="admin_role" class="mt-4">
        <RegRequests/>
      </div>
    </main>
    <Footer/>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue'
import RegRequests from "@/components/RegRequests.vue"
import Header from "@/components/Header.vue"
import Footer from "@/components/Footer.vue"

const name = ref('')
const admin_role = ref()

onMounted(() => {
  $.get('users/get_user_info', (resp) => {
    name.value = resp.username
    admin_role.value = resp.admin_role
  })
})

const props = defineProps({
  token: String
})
</script>