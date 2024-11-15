<template>
  <p>{{ name }}</p>
  <button type="button" @click="logout"> LOG OUT! </button>
  <button type="button" @click="del_user"> Delete account </button>
  <div v-if="admin_role">
    <RegRequests/>
  </div>

</template>

<script setup>

import { ref, onMounted } from 'vue'
import router from '@/routes/routes.js'
import RegRequests from "@/components/RegRequests.vue";

const name = ref('')
const admin_role = ref()

onMounted(() => {
  $.get('users/get_user_info', (resp) => {
    name.value = resp.username
    admin_role.value = resp.admin_role
  })
})

const props = defineProps({
  token : String
})

const logout = () => {
  $.ajax({
    url : '/logout',
    type : 'POST',
    headers : {
      'X-CSRF-Token': props.token
    },
    success : () => {
        router.push('/login')
    }
  })
}

const del_user = () => {
  $.ajax({
    url : '/users/delete_user',
    type : 'POST',
    headers : {
      'X-CSRF-Token': props.token
    },
    contentType: "text/plain",
    data : name.value,
    success : () => {
      logout()
    }
  })
}


</script>

<style scoped>

</style>