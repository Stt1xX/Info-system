<template>
  <div class="min-h-screen flex flex-col bg-gray-900 text-white">
    <main class="flex-grow container mx-auto p-8">
      <h1 class="text-2xl font-bold mb-6 text-center">Requests</h1>
      <div class="bg-gray-800 p-12 rounded-lg shadow-lg max-w-4xl mx-auto">
        <div class="grid grid-cols-2 gap-4">
          <div v-if="messages.length === 0" class="col-span-2 text-center text-xl">
            It's quiet here so far...
          </div>
          <div v-for="message in messages" :key="message.id" class="bg-gray-700 p-6 rounded flex justify-between items-center">
            <p>№ : {{ message.id }}<br>Username : {{ message.username }}<br>Date : {{ message.date }}</p>
            <div class="flex flex-col items-end space-y-2">
              <button class="border-2 border-green-500 text-green-500 p-1.5 rounded-full transform hover:scale-110 transition-transform duration-200" type="button" @click="accept(message.id)">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
                </svg>
              </button>
              <button class="border-2 border-red-500 text-red-500 p-1.5 rounded-full transform hover:scale-110 transition-transform duration-200" type="button" @click="reject(message.id)">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                </svg>
              </button>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import {onMounted, ref} from "vue";
import {Stomp} from "@stomp/stompjs";
import SockJS from "sockjs-client";

const messages = ref([]);
let stompClient = null;

const props = defineProps({
  token: String,
});

onMounted(() => {
  connect(get_reg_requests);
});

const get_reg_requests = (data) => {
  messages.value = data;
};

const connect = (onMessageReceived) => {
  stompClient = Stomp.over(() => new SockJS("/ws"));
  stompClient.connect({}, () => {
    get();
    stompClient.subscribe("/topic/reg_requests", (message) => {
      onMessageReceived(JSON.parse(message.body));
    });
  });
};

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