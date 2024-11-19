<template>
  <div id="app">
    <Header @logout="logout" v-if="currentAuth" :token="token"/>
    <router-view @logged="logged" :token="token"/>
    <Footer/>
  </div>
</template>

<script setup>
  import Footer from "@/components/shared_comps/Footer.vue";
  import Header from "@/components/shared_comps/Header.vue";
  import { onMounted, ref, watch} from 'vue';
  import {useRoute} from "vue-router";

  const route = useRoute();
  const token = ref();
  const currentAuth = ref(false);

  watch(
      () => route.path,
      (newPath) => {
        currentAuth.value = !(newPath === '/login' ||
            newPath === '/registration' || newPath === '/error/ErrorPage');
      }
  );

  const logged = () => {
    get_token()
  }

  const logout = () => {
    get_token()
  }

  const get_token = () => {
      $.ajax({
        type: "GET",
        url: "/app/csrf-token",
        success: function (response) {
          token.value =response.csrfToken
        }
    });
  }


  onMounted( async () => {
    get_token()
  })

</script>

<style scoped>
</style>