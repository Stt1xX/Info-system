<template>
  <div class="min-h-screen flex flex-col bg-gray-900 text-white">
    <div class="flex-grow flex flex-col items-center justify-center">
      <div class="w-1/3">
        <div class="flex justify-center mb-4">
          <GhostLogo />
        </div>
        <h1 class="text-2xl font-bold mb-6 text-center">Sign In</h1>
        <div class="login-container bg-gray-800 p-4 rounded-lg shadow-lg">
          <form @submit.prevent="submitForm" class="login-form space-y-4">
            <div class="form-group">
              <input type="text" name="username" placeholder="Username" v-model="username" class="form-control w-full p-3 rounded bg-gray-700 border border-gray-600 focus:outline-none focus:ring-2 focus:ring-blue-500"/>
              <div v-if="usernameError" class="warning text-red-500 mt-2">{{ usernameError }}</div>
            </div>
            <div class="form-group">
              <input type="password" name="password" placeholder="Password" v-model="password" class="form-control w-full p-3 rounded bg-gray-700 border border-gray-600 focus:outline-none focus:ring-2 focus:ring-blue-500"/>
              <div v-if="passwordError" class="warning text-red-500 mt-2">{{ passwordError }}</div>
            </div>
            <div class="form-actions flex justify-between items-center mt-4">
              <input type="submit" value="Log in" class="btn btn-primary bg-blue-500 hover:bg-blue-700 text-white font-bold py-3 px-6 rounded cursor-pointer text-lg" @click="validateForm"/>
              <div class="flex items-center">
                <span>Not registered?</span>
                <a @click="navigateToRegister" class="text-blue-500 hover:underline cursor-pointer ml-1">sign up</a>
              </div>
            </div>
          </form>
          <div v-if="message !== ''" class="message mt-4 p-3 bg-gray-700 border border-gray-600 rounded text-center">
            {{message}}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref} from 'vue'
import { useRouter } from 'vue-router'
import GhostLogo from "@/components/logos/GhostLogo.vue";

const username = ref('')
const password = ref('')
const message = ref('')
const usernameError = ref('')
const passwordError = ref('')

const router = useRouter()

const emit = defineEmits()

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
  return isValid;
}

const submitForm = () => {
  if (!validateForm()) {
    return;
  }
  $.ajax({
    type: "POST",
    url: "/login",
    headers: {
      'X-CSRF-Token': props.token
    },
    data: {
      "username": username.value,
      "password": password.value
    },
    success: () => {
      message.value = ''
      emit('logged')
      router.push("/humanTable")
    },
    error: (resp) => {
      message.value = resp.responseJSON.responseText
    }
  })
}
const navigateToRegister = () => {
  router.push('/registration')
}
</script>