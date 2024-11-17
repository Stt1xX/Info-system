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
import CarTableButton from "@/components/buttons/CarTableButton.vue";
import HumanTableButton from "@/components/buttons/HumanTableButton.vue";
import CoordinateTableButton from "@/components/buttons/CoordinateTableButton.vue";
import GhostLogo from "@/components/GhostLogo.vue";

const props = defineProps({
  token: String,
  username : String,
  admin_role : Boolean
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
  @apply ml-2 text-2xl font-bold;
}

.welcome-message {
  @apply mx-4 text-lg;
}

.right-section {
  @apply flex items-center space-x-2;
}
</style>