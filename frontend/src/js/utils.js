import {ref} from "vue";

export const AddEditWindowType = {
    ADDING : 1,
    EDITING : 2
}

export const ItemType = {
    CAR : 1,
    COORDINATES : 2,
    HUMAN : 3
}

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