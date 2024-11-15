<template>
  <h1>Please Register</h1>
  <form @submit.prevent="submit_form">
    <div>
      <input type="text" name="username" placeholder="Username" v-model="username"/>
    </div>
    <div>
      <input type="password" name="password" placeholder="Password" v-model="password"/>
    </div>
    <div>
      Select your role:
      <select v-model="role" name="role">
        <option value="USER">USER</option>
        <option value="ADMIN">ADMIN</option>
      </select>
    </div>
    <input type="submit" value="Registry"/>
    <input type="button" @click="router.push('/login')" value="Login">
  </form>
  <div v-if="message !== ''">
    {{message}}
  </div>

</template>


<script setup>

  import { ref } from 'vue'
  import router from "@/routes/routes.js";

  const username = ref()
  const password = ref()
  const role = ref()

  const message = ref('')

  const props = defineProps({
    token : String
  })

  const submit_form = () => {
      $.ajax({
        type: "POST",
        url: "/users/add_new_user",
        headers: {
          'X-CSRF-Token': props.token
        },
        contentType: "application/json",
        data: JSON.stringify({
          "username": username.value,
          "password": password.value,
          "role": role.value
        }),
        success: (resp) => {
          message.value = resp
        },
        error: (resp) => {
          message.value = resp.responseText
        }
      })
  }
</script>

<style scoped>

</style>