<template>
  <div class="min-h-screen flex flex-col bg-gray-900 text-white">
    <Header :token="token" />
    <main class="flex-grow container mx-auto p-4">
    </main>
    <Footer/>
  </div>
</template>

<script setup>
import Header from "@/components/Header.vue"
import Footer from "@/components/Footer.vue"

const props = defineProps({
  token: String
})
</script>