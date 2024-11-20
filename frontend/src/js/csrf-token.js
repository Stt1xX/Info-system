import {ref} from "vue";

export const token = ref();

export const get_token = () => {
    $.ajax({
        type: "GET",
        url: "/app/csrf-token",
        success: function (response) {
            token.value =response.csrfToken
        }
    });
}