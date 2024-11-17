<template>
  <header class="header bg-gray-800 text-white">
    <div class="left-section">
      <GhostLogo />
      <span class="username">Stt1xX</span>
    </div>
    <span class="welcome-message">Welcome, {{ username }}!</span>
    <div class="right-section">
      <HumanTableButton />
      <CarTableButton />
      <CoordinateTableButton />
      <RequestsButton :is_admin="admin_role" />
      <LogoutButton @logout="intermediateEmit" :token="token" />
      <DeleteAccountButton @logout="intermediateEmit" :token="token" :username="username" />
    </div>
  </header>
</template>

<script setup>
import DeleteAccountButton from "@/components/buttons/DeleteAccountButton.vue"
import LogoutButton from "@/components/buttons/LogoutButton.vue"
import RequestsButton from "@/components/buttons/RequestsButton.vue"
import CarTableButton from "@/components/buttons/CarTableButton.vue";
import HumanTableButton from "@/components/buttons/HumanTableButton.vue";
import CoordinateTableButton from "@/components/buttons/CoordinateTableButton.vue";
import GhostLogo from "@/components/GhostLogo.vue";
import {onMounted, ref} from "vue";

const username = ref()
const admin_role = ref()

const emit = defineEmits()

const intermediateEmit = () => {
  emit('logout')
}

const props = defineProps({
  token: String,
});

onMounted(() => {
  get_user_info()
})

function get_user_info() {
  $.get('/users/get_user_info', (resp) => {
    username.value = resp.username
    admin_role.value = resp.admin_role
  })
}
</script>

<style scoped>
.header {
  @apply flex justify-between items-center p-4;
}

.left-section {
  @apply flex items-center;
}

.username {
  @apply ml-2 text-2xl font-bold;
}

.welcome-message {
  @apply mx-4 text-lg;
}

.right-section {
  @apply flex items-center space-x-2;
}
</style>