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

const sendMessageHandler = () => {
  sendMessage(message.value);
  message.value = '';
};

const onMessageReceived = (msg) => {
  messages.value = msg;
};

onMounted(() => {
  connect(onMessageReceived);
});



let stompClient = null;

function connect(onMessageReceived) {
  stompClient = Stomp.over(function(){
    return new SockJS('/ws')
  });

  // stompClient.debug = function() {} on release uncomment

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
