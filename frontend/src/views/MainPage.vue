<template>
  <p>Set your name!</p>
  <input placeholder="name" v-model="name" />
  <input placeholder="age" v-model="age" />
  <button type="button" @click ="add_data"> Add! </button>
  <button type="button" @click ="get_data"> GET DATA! </button>
  <p>{{ greeting }}</p>
  <ul>
    <li v-for="item in items" :key="item">{{ item }}</li>
  </ul>
  <div style="display : inline-block; background : black; width: 160px; height: 50px">

  </div>

  <button type="button" @click ="logout"> LOG OUT! </button>
  <div>
    <SocketsChat/>
  </div>


</template>

<script setup>

import { ref, onMounted } from 'vue'
import router from '@/routes/routes.js'
import SocketsChat from "@/components/SocketsChat.vue";
const greeting = ref('')
const name = ref('')
const age = ref('')
const items = ref(['Apple', 'Banana', 'Cherry', 'Date'])

onMounted(() => {
  $.get('users/get_username', (resp) => {
    greeting.value = "Hello " + resp
  })
})

const props = defineProps({
  token : String
})

const get_data = () => {
  $.get('/getusers'
  , function (resp){
      items.value = resp
  })
}


const add_data = () => {
  $.get('/add_new_user', {
        'name' : name.value,
        'age'  : age.value
      }
      , function (resp){
        items.value = resp
      })
}


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

</script>

<style scoped>

</style>