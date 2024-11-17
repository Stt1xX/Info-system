<template>
  <div id="app">
    <Header @logout="get_token" v-if="currentPath" :token="token"/>
    <router-view @logined="get_token" :token="token"/>
    <Footer/>
  </div>
</template>

<script setup>
  import Footer from "@/components/Footer.vue";
  import Header from "@/components/Header.vue";

  import {onMounted, ref, watch} from 'vue';
  import {useRoute} from "vue-router";


  const route = useRoute();
  const token = ref();
  const currentPath = ref(false);
  watch(
      () => route.path,
      (newPath) => {
        currentPath.value = !(newPath === '/login' ||
            newPath === '/registration' || newPath === '/error/ErrorPage');
      }
  );

  const get_token = () => {
      $.ajax({
        type: "GET",
        url: "/app/csrf-token",
        success: function (response) {
          token.value =response.csrfToken
        }
    });
  }

  onMounted( () => {
    get_token()
  })
</script>

<style scoped>
</style>