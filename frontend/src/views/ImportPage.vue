<template>
  <div class="min-h-screen flex flex-col bg-gray-900 text-white">
    <main class="flex-grow container mx-auto p-8">
      <h1 class="text-2xl font-bold mb-6 text-center">Import File</h1>
      <div class="bg-gray-800 p-12 rounded-lg shadow-lg max-w-4xl mx-auto">
        <div class="grid grid-cols-[1fr,1fr,1fr,0.75fr] gap-4 min-h-[72px]">
          <div class="flex items-center justify-center">
            <input type="file" @change="handleFileUpload" class="hidden-file-input" ref="fileInput"/>
            <button type="button" class="file-btn" @click="triggerFileInput">
              {{ file ? file.name : 'No File Selected' }}
            </button>
          </div>
          <div class="flex items-center justify-center">
            <div :class="{'warning-block': showWarning, 'warning-placeholder': !showWarning}" class="text-center">
              Please upload a file before sending.
            </div>
          </div>
          <div class="flex items-center justify-center">
            <button type="button" class="send-btn" @click="handleSend">
              Send
            </button>
          </div>
          <div class="flex items-center justify-center col-span-1">
            <button type="button" class="history-btn" @click="viewImportHistory">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="0.7" stroke="currentColor" class="clock-icon">
                <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z" />
              </svg>
            </button>
          </div>
        </div>
      </div>
    </main>
  </div>
  <HistoryImportWindow v-if="isHistoryVisible" @close="closeHistoryWindow"/>
</template>

<script setup>
import { ref } from 'vue';
import HistoryImportWindow from "@/components/windows/HistoryImportWindow.vue";
import {token} from "@/js/csrf-token.js";
import {showAlert} from "@/js/custom-alert.js";

const file = ref(null);
const fileInput = ref(null);
const showWarning = ref(false);
const isHistoryVisible = ref(false);

const closeHistoryWindow = () => {
  isHistoryVisible.value = false;
};
const openHistoryWindow = () => {
  isHistoryVisible.value = true;
};

const handleFileUpload = (event) => {
  file.value = event.target.files[0];
  showWarning.value = false;
};

const triggerFileInput = () => {
  fileInput.value.click();
};

const handleSend = () => {
  if (!file.value) {
    showWarning.value = true;
  } else {
    const formData = new FormData();
    formData.append('file', file.value);
    $.ajax({
      url: '/files/import',
      type: 'POST',
      data: formData,
      processData: false,
      contentType: false,
      headers: {
        'X-CSRF-Token': token.value
      },
      success: (data) => {
        showAlert(data);
      },
      error: (error) => {
        showAlert(error);
      }
    })
    file.value = null;
    fileInput.value.value = null;
  }
};

const viewImportHistory = () => {
  openHistoryWindow();
};
</script>

<style scoped>
.hidden-file-input {
  display: none;
}

.file-btn, .send-btn {
  @apply bg-blue-500 text-white font-bold py-2 px-4 rounded cursor-pointer;
  transition: background-color 0.3s, transform 0.3s;
  width: 100%;
}

.file-btn:hover {
  @apply bg-blue-700;
}

.send-btn {
  @apply border border-white;
  background-color: transparent;
}

.send-btn:hover {
  @apply transform scale-105;
}

.history-btn {
  @apply text-white cursor-pointer;
}

.history-btn:hover {
  @apply transform scale-105;
}

.clock-icon {
  width: 3rem; /* Increase size */
  height: 3rem; /* Increase size */
  transition: transform 0.3s;
}

.clock-icon:hover {
  transform: scale(1.2);
}

.warning-block {
  @apply text-red-500 font-bold;
}

.warning-placeholder {
  visibility: hidden;
  height: 2rem; /* Adjust height as needed */
}
</style>