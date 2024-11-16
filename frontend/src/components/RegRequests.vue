<template>
  <div class="container">
    <button class="btn" type="button" @click="get">Сообщения</button>
    <div v-for="message in messages" :key="message.id" class="message">
      <p>{{ message.id }} : {{ message.username }} : {{ message.date }}</p>
      <div class="actions">
        <button class="btn accept" type="button" @click="accept(message.id)">Принять</button>
        <button class="btn reject" type="button" @click="reject(message.id)">Отклонить</button>
      </div>
    </div>
    <button class="btn" type="button" @click="messages.value = null">Скрыть</button>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";

const messages = ref();

let stompClient = null;

const get_reg_requests = (data) => {
  messages.value = data;
  console.log(data);
};

const connect = (onMessageReceived) => {
  stompClient = Stomp.over(function () {
    return new SockJS("/ws");
  });
  // stompClient.debug = function() {} on release uncomment
  stompClient.connect({}, () => {
    stompClient.subscribe("/topic/reg_requests", (message) => {
      onMessageReceived(JSON.parse(message.body));
    });
  });
};

onMounted(() => {
  connect(get_reg_requests);
});

function get() {
  if (stompClient && stompClient.connected) {
    stompClient.send("/app/get_reg_requests", {}, {});
  }
}

function accept(id) {
  if (stompClient && stompClient.connected) {
    stompClient.send("/app/accept", {}, id);
  }
}

function reject(id) {
  if (stompClient && stompClient.connected) {
    stompClient.send("/app/reject", {}, id);
  }
}
</script>

<style scoped>
.container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.btn {
  display: inline-block;
  padding: 10px 20px;
  margin: 10px 0;
  font-size: 16px;
  color: #fff;
  background-color: #007bff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.btn:hover {
  background-color: #0056b3;
}

.message {
  padding: 15px;
  margin: 10px 0;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.actions {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
}

.accept {
  background-color: #28a745;
}

.accept:hover {
  background-color: #218838;
}

.reject {
  background-color: #dc3545;
}

.reject:hover {
  background-color: #c82333;
}
</style>