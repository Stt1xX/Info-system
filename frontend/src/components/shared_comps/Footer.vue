<template>
  <footer class="footer bg-gray-800 text-white">
    <p>&copy; 2024 Stt1xX Company. All rights reserved.</p>
    <p>The site was made as part of the laboratory work number 1 of Information Systems, the 3rd year subject of ITMO University, specializing in system applied software</p>
    <div class="footer-content pl-16 pr-16">
      <a href="https://t.me/Stt0xX" target="_blank" class="icon-link">
        <TelegramLogo />
      </a>
      <span class="footer-text">Rameev Timur</span>
      <a href="https://vk.com/id583660799" target="_blank" class="icon-link">
        <VkLogo />
      </a>
    </div>
  </footer>
</template>

<script setup>
import TelegramLogo from "@/components/logos/TelegramLogo.vue";
import VkLogo from "@/components/logos/VkLogo.vue";
</script>

<style scoped>
.footer {
  @apply text-center p-4;
}

.footer-content {
  @apply flex justify-between items-center mt-8;
}

.icon-link {
  @apply transition-transform duration-300 ease-in-out cursor-pointer;
}

.icon-link:hover {
  transform: scale(1.2);
}

.footer-text {
  @apply mx-4;
}
</style>