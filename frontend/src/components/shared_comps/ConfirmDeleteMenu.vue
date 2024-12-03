<template>
  <div class="fixed inset-0 flex flex-col items-center justify-center bg-black bg-opacity-50 z-50">
    <h1 class="text-2xl font-bold mb-6 text-center text-white">Confirmation</h1>
    <div class="bg-gray-800 p-4 rounded-lg shadow-lg w-[550px] text-center">
      <form>
        <div class="form-group">
          Are you sure you want to delete the <span class="font-bold">{{author}}'s {{getItemName(props.itemCode)}}</span> with id: <span class="font-bold">{{id}}</span>?
        </div>
        <div class="warning text-red-500 mt-2 h-4">{{warningMessage}}</div>
        <div class="flex justify-between items-center mt-6">
          <button @click="confirmFunc" type="button" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">Yes</button>
          <button @click="closeMenu" type="button" class="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">No</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import {getItemName, getUrlPrefix, ItemType} from "@/js/utils.js";
import {token} from "@/js/csrf-token.js";
import {showAlert} from "@/js/custom-alert.js";
import {onMounted, ref} from "vue";

const warningMessage = ref('')

const props = defineProps({
  itemCode: Number,
  id: String,
  author : String
});

const emit = defineEmits(['close']);

const confirmFunc = () => {
  delete_obj()
  closeMenu();
};

const closeMenu = () => {
  emit('close');
  warningMessage.value = ''
};

const delete_obj = () => {
  $.ajax({
    type: 'DELETE',
    url: `${getUrlPrefix(props.itemCode)}/delete/${props.id}`,
    headers: {
      'X-CSRF-Token': token.value
    },
    success: function (data) {
      showAlert(data)
    },
    error: function (error) {
      showAlert(error.responseText)
    }
  })
}

onMounted(() => {
  if (props.itemCode === ItemType.CAR || props.itemCode === ItemType.COORDINATES){
    $.ajax({
      type: 'GET',
      url: `${getUrlPrefix(props.itemCode)}/get_depends/${props.id}`,
      success: (resp) => {
        if (resp > 0){
          warningMessage.value = `Warning: This ${getItemName(props.itemCode)} is used in ${resp} human(s).`
        }
      }
    })
  }
})

</script>

<style scoped>
</style>