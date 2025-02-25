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
      <ImportFileButton />
      <RequestsButton v-if="admin_role" :is_admin="admin_role" />
      <LogoutButton @logout="intermediateEmit" :token="token" />
<!--      <DeleteAccountButton @logout="intermediateEmit" :token="token" />-->
<!--      For simple database's links between user's id and commits and file imports entities-->
    </div>
  </header>
</template>

<script setup>
import LogoutButton from "@/components/buttons/LogoutButton.vue"
import RequestsButton from "@/components/buttons/RequestsButton.vue"
import CarTableButton from "@/components/buttons/CarTableButton.vue";
import HumanTableButton from "@/components/buttons/HumanTableButton.vue";
import ImportFileButton from "@/components/buttons/ImportFileButton.vue";
import CoordinateTableButton from "@/components/buttons/CoordinateTableButton.vue";
import GhostLogo from "@/components/logos/GhostLogo.vue";
import {onMounted} from "vue";
import {get_user_info, admin_role, username} from "@/js/utils.js";

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