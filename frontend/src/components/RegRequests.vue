<template>
  <div>
    <button type="button" @click="get"> Сообщения </button>
    <p v-for="message in messages" :key="message.id">
      {{ message.id }} : {{ message.username }} : {{ message.date }}
      <button type="button" @click="accept(message.id)">Принять</button>
      <button type="button" @click="reject(message.id)">Отклонить</button>
    </p>
    <button type="button" @click="messages.value = null">Скрыть</button>
  </div>
</template>

<script setup>

import {onMounted, ref} from "vue";
import {Stomp} from "@stomp/stompjs";
import SockJS from "sockjs-client";

const messages = ref()

let stompClient = null;

const get_reg_requests = (data) => {
  messages.value = data
  console.log(data)
}

const connect = (onMessageReceived) => {
  stompClient = Stomp.over(function(){
    return new SockJS('/ws')
  });
  // stompClient.debug = function() {} on release uncomment
  stompClient.connect({}, () => {
    stompClient.subscribe('/topic/reg_requests', (message) => {
      onMessageReceived(JSON.parse(message.body));
    });
  });
}

onMounted(() => {connect(get_reg_requests)})

function get() {
  if (stompClient && stompClient.connected) {
    stompClient.send('/app/get_reg_requests', {}, {});
  }
}

function accept(id) {
  if (stompClient && stompClient.connected) {
    stompClient.send('/app/accept', {}, id);
  }
}

function reject(id) {
  if (stompClient && stompClient.connected) {
    stompClient.send('/app/reject', {}, id);
  }
}


</script>

<style scoped>

</style>