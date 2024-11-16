<template>
  <div class="min-h-screen flex flex-col bg-gray-900 text-white">
    <div class="flex-grow flex flex-col items-center justify-center">
      <div class="w-1/3">
        <div class="flex justify-center mb-4">
          <svg @click="reloadPage" fill="#ffffff" width="64px" height="64px" viewBox="0 0 512 512" xmlns="http://www.w3.org/2000/svg" stroke="#ffffff" class="cursor-pointer">
            <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
            <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
            <g id="SVGRepo_iconCarrier">
              <title>ionicons-v5_logos</title>
              <path d="M496,347.21a190.31,190.31,0,0,1-32.79-5.31c-27.28-6.63-54.84-24.26-68.12-52.43-6.9-14.63-2.64-18.59,11.86-24,14.18-5.27,29.8-7.72,36.86-23,5.89-12.76,1.13-27.76-10.41-35.49-15.71-10.53-30.35-.21-46.62,2.07,3.73-46.66,8.66-88.57-22.67-127.73C338.14,48.86,297.34,32,256.29,32S174.43,48.86,148.48,81.33c-31.38,39.26-26.4,81.18-22.67,127.92C109.49,207,95,196.46,79.18,207.07c-14.72,9.85-17,29.76-5.44,43s31.64,9.5,43.45,20.6c6.49,6.09,3.49,12.61-.35,20.14-14.48,28.4-39.26,45.74-69.84,51.56-4,.76-22.31,2.87-31,3.65,0,9.28.52,16.78,1.63,21.73,2.94,13.06,12.32,23.58,23.69,30.1C52.5,404.25,76.8,404.28,83,413.36c3,4.48,1.76,12.28,5.33,17.38a23.8,23.8,0,0,0,15.37,9.75c18.61,3.61,37.32-7.2,56.42-2.1,14.85,3.95,26.52,15.87,39.26,24,15.51,9.85,32.34,16.42,50.83,17.49,38.1,2.21,59.93-18.91,90.58-36.42,19.5-11.14,38.15-3.86,58.88-2.68,20.1,1.15,23.53-9.25,29.62-24.88a27.37,27.37,0,0,0,1.54-4.85,10.52,10.52,0,0,0,2.28-1.47c2-1.57,10.55-2.34,12.76-2.86,10.28-2.44,20.34-5.15,29.17-11.2,11.31-7.76,17.65-18.5,19.58-32.64A93.73,93.73,0,0,0,496,347.21ZM208,128c8.84,0,16,10.74,16,24s-7.16,24-16,24-16-10.74-16-24S199.16,128,208,128Zm103.62,77.7c-15.25,15-35,23.3-55.62,23.3a78.37,78.37,0,0,1-55.66-23.34,8,8,0,0,1,11.32-11.32A62.46,62.46,0,0,0,256,213c16.39,0,32.15-6.64,44.39-18.7a8,8,0,0,1,11.23,11.4ZM304,176c-8.84,0-16-10.75-16-24s7.16-24,16-24,16,10.75,16,24S312.84,176,304,176Z"></path>
            </g>
          </svg>
        </div>
        <h1 class="text-2xl font-bold mb-6 text-center">Sign Up</h1>
        <div class="registry-container bg-gray-800 p-4 rounded-lg shadow-lg">
          <form @submit.prevent="submit_form" class="registry-form space-y-4">
            <div class="form-group">
              <input type="text" name="username" placeholder="Username" v-model="username" class="form-control w-full p-3 rounded bg-gray-700 border border-gray-600 focus:outline-none focus:ring-2 focus:ring-blue-500"/>
              <div v-if="usernameError" class="warning text-red-500 mt-2">{{ usernameError }}</div>
            </div>
            <div class="form-group">
              <input type="password" name="password" placeholder="Password" v-model="password" class="form-control w-full p-3 rounded bg-gray-700 border border-gray-600 focus:outline-none focus:ring-2 focus:ring-blue-500"/>
              <div v-if="passwordError" class="warning text-red-500 mt-2">{{ passwordError }}</div>
            </div>
            <div class="form-group">
              <label for="role" class="block mb-2 text-center">Select your role:</label>
              <select v-model="role" name="role" class="form-control w-full p-3 rounded bg-gray-700 border border-gray-600 focus:outline-none focus:ring-2 focus:ring-blue-500">
                <option value="USER">USER</option>
                <option value="ADMIN">ADMIN</option>
              </select>
              <div v-if="roleError" class="warning text-red-500 mt-2">{{ roleError }}</div>
            </div>
            <div class="form-actions flex justify-between items-center mt-4">
              <input type="submit" value="Register" class="btn btn-primary bg-blue-500 hover:bg-blue-700 text-white font-bold py-3 px-6 rounded cursor-pointer text-lg"/>
              <div class="flex items-center">
                <span>Registered?</span>
                <a @click="navigateToLogin" class="text-blue-500 hover:underline cursor-pointer ml-1">sign in</a>
              </div>
            </div>
          </form>
          <div v-if="message !== ''" class="message mt-4 p-3 bg-gray-700 border border-gray-600 rounded text-center">
            {{message}}
          </div>
        </div>
      </div>
    </div>
    <Footer />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import Footer from '@/components/Footer.vue';

const username = ref('')
const password = ref('')
const role = ref('')
const message = ref('')
const usernameError = ref('')
const passwordError = ref('')
const roleError = ref('')

const router = useRouter()
const emit = defineEmits(['update:username'])

const props = defineProps({
  token: String
})

const validateForm = () => {
  let isValid = true;
  if (!username.value) {
    usernameError.value = "Username is required";
    isValid = false;
  } else {
    usernameError.value = "";
  }
  if (!password.value) {
    passwordError.value = "Password is required";
    isValid = false;
  } else if (password.value.length < 5) {
    passwordError.value = "Password must be at least 5 characters long";
    isValid = false;
  } else {
    passwordError.value = "";
  }
  if (!role.value) {
    roleError.value = "Role is required";
    isValid = false;
  } else {
    roleError.value = "";
  }
  return isValid;
}

const submit_form = () => {
  if (!validateForm()) {
    return;
  }
  $.ajax({
    type: "POST",
    url: "/users/add_new_user",
    headers: {
      'X-CSRF-Token': props.token
    },
    contentType: "application/json",
    data: JSON.stringify({
      "username": username.value,
      "password": password.value,
      "role": role.value
    }),
    success: (resp) => {
      message.value = resp
      emit('update:username', username.value)
    },
    error: (resp) => {
      message.value = resp.responseText
    }
  })
}

const reloadPage = () => {
  window.location.reload()
}

const navigateToLogin = () => {
  router.push('/login')
}
</script>