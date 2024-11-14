<template>
  <div>
    <h1>WebSocket Chat</h1>
    <input v-model="message" @keyup.enter="sendMessageHandler" placeholder="Enter a message" />
    <div>
      {{messages}}
    </div>
  </div>
</template>


<script setup>
import { ref, onMounted } from 'vue';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';


const message = ref('');
const messages = ref([]);

// Функция для отправки сообщения
const sendMessageHandler = () => {
  sendMessage(message.value);
  message.value = '';
};

// Функция для обработки полученных сообщений
const onMessageReceived = (msg) => {
  messages.value = msg;
};

// Подключение к WebSocket при монтировании компонента
onMounted(() => {
  connect(onMessageReceived);
});



let stompClient = null;

function connect(onMessageReceived) {
  stompClient = Stomp.over(function(){
    return new SockJS('/ws')
  });

  stompClient.connect({}, () => {
    stompClient.subscribe('/topic/messages', (message) => {
      onMessageReceived(JSON.parse(message.body));
    });
  });
}

function sendMessage(message) {
  if (stompClient && stompClient.connected) {
    stompClient.send('/app/message', {}, JSON.stringify(message));
  }
}
</script>
