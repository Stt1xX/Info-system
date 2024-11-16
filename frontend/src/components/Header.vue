<template>
  <header class="header bg-gray-800 text-white">
    <div class="left-section">
      <GhostLogo />
      <span class="username">Stt1xX</span>
    </div>
    <div class="right-section">
      <button class="table-btn">
        <svg class="fill-current w-4 h-4 mr-2" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
          <path d="M3 3h18v18H3V3zm2 2v2h2V5H5zm4 0v2h2V5H9zm4 0v2h2V5h-2zm4 0v2h2V5h-2zM5 9v2h2V9H5zm4 0v2h2V9H9zm4 0v2h2V9h-2zm4 0v2h2V9h-2zM5 13v2h2v-2H5zm4 0v2h2v-2H9zm4 0v2h2v-2h-2zm4 0v2h2v-2h-2zM5 17v2h2v-2H5zm4 0v2h2v-2H9zm4 0v2h2v-2h-2zm4 0v2h2v-2h-2z"/>
        </svg>
        Table1
      </button>
      <button class="table-btn">
        <svg class="fill-current w-4 h-4 mr-2" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
          <path d="M3 3h18v18H3V3zm2 2v2h2V5H5zm4 0v2h2V5H9zm4 0v2h2V5h-2zm4 0v2h2V5h-2zM5 9v2h2V9H5zm4 0v2h2V9H9zm4 0v2h2V9h-2zm4 0v2h2V9h-2zM5 13v2h2v-2H5zm4 0v2h2v-2H9zm4 0v2h2v-2h-2zm4 0v2h2v-2h-2zM5 17v2h2v-2H5zm4 0v2h2v-2H9zm4 0v2h2v-2h-2zm4 0v2h2v-2h-2z"/>
        </svg>
        Table2
      </button>
      <button class="table-btn">
        <svg class="fill-current w-4 h-4 mr-2" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
          <path d="M3 3h18v18H3V3zm2 2v2h2V5H5zm4 0v2h2V5H9zm4 0v2h2V5h-2zm4 0v2h2V5h-2zM5 9v2h2V9H5zm4 0v2h2V9H9zm4 0v2h2V9h-2zm4 0v2h2V9h-2zM5 13v2h2v-2H5zm4 0v2h2v-2H9zm4 0v2h2v-2h-2zm4 0v2h2v-2h-2zM5 17v2h2v-2H5zm4 0v2h2v-2H9zm4 0v2h2v-2h-2zm4 0v2h2v-2h-2z"/>
        </svg>
        Table3
      </button>
      <RequestsButton v-if="admin_role" :is_admin="admin_role" />
      <LogoutButton :token="token" />
      <DeleteAccountButton :token="token" :username="username" />
    </div>
  </header>
</template>

<script setup>
import DeleteAccountButton from "@/components/buttons/DeleteAccountButton.vue"
import LogoutButton from "@/components/buttons/LogoutButton.vue"
import RequestsButton from "@/components/buttons/RequestsButton.vue"
import GhostLogo from "@/components/GhostLogo.vue";
import {onMounted, ref} from "vue";

const username = ref('')
const admin_role = ref()

onMounted(() => {
  $.get('/users/get_user_info', (resp) => {
    username.value = resp.username
    admin_role.value = resp.admin_role
  })
})

const props = defineProps({
  token: String,
});
</script>

<style scoped>
.header {
  @apply flex justify-between items-center p-4;
}

.left-section {
  @apply flex items-center;
}

.username {
  @apply ml-2 text-lg font-bold;
}

.right-section {
  @apply flex items-center space-x-2;
}

.table-btn {
  @apply flex items-center bg-transparent hover:bg-blue-500 text-blue-500 hover:text-white font-bold py-2 px-4 rounded;
}
</style>