<template>
  <div id="app">
    <Header v-if="flag_header" :username="username" :admin_role="admin_role"/>
    <RouterView/>
    <Footer/>
  </div>
</template>

<script setup>
  import Footer from "@/components/Footer.vue";
  import Header from "@/components/Header.vue";

  import {onMounted, provide, ref} from 'vue';

  const username = ref()
  const flag_header = ref(false)
  const admin_role = ref()

  onMounted(() => {
    get_user_info()
  })

  function get_user_info() {
    $.get('/users/get_user_info', (resp) => {
      username.value = resp.username
      admin_role.value = resp.admin_role
    })
    flag_header.value = true;
  }

  function logout() {
    flag_header.value = false
    username.value = null
    admin_role.value = null
  }

  provide('get_user_info', get_user_info)
  provide('logout', logout)
</script>

<style scoped>
</style>