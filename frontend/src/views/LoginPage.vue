<template>
  <h1>Please Log In</h1>
  <meta name="csrf-token" content="your-csrf-token-value">
  <form @submit.prevent="submitForm">
    <div>
      <input type="text" name="username" placeholder="Username" v-model="username"/>
    </div>
    <div>
      <input type="password" name="password" placeholder="Password" v-model="password"/>
    </div>
    <input type="submit" value="Log in" />
  </form>
  <div v-if="flag_reg === false">
    Попробуйте еще раз, неверный логин или пароль
  </div>

  <p>
    {{ username }}
    {{ password }}
  </p>
</template>

<script setup>

  import {ref} from "vue";
  import router from "@/routes/routes.js"
  const username = ref()
  const password = ref()
  const flag_reg = ref(true)

  const props = defineProps({
    token : String
  })

    const submitForm = async () => {
      $.ajax({
        type : "POST",
        url : "/login",
        headers: {
          'X-CSRF-Token': props.token
        },
        data : {
          "username" : username.value,
          "password" : password.value
        },
        success: function(response) {
          switch (response.status){
            case  "success":
              router.push('/greeting')
              flag_reg.vale = true
              break
            case "unauthorized":
              flag_reg.value = false;
              break
          }
        }
      })
    }
</script>

<style scoped>

</style>