import {ref} from "vue";

import {Stomp} from "@stomp/stompjs";
import SockJS from "sockjs-client";

export const cars = ref([]);
export const page_number = ref(0)
export const size_number = ref(8)
export const sortBy = ref('id')
export const order = ref(true)
export const total_pages = ref(0)

export let stompClient = null;

export const connect = () => {
    stompClient = Stomp.over(() => new SockJS('/ws'));
    stompClient.connect({}, () => {
        get();
        stompClient.subscribe("/topic/cars", () => {
            get();
        });
    });
};

export function get() {
    $.ajax({
        url: "/cars/get_all",
        type: "GET",
        contentType: "application/json",
        data : {
            page: page_number.value,
            size: size_number.value,
            sortBy: sortBy.value,
            order: order.value
        },
        success: (response) => {
            console.log(response)
            total_pages.value = response.page.totalPages;
            page_number.value = response.page.number;
            if (total_pages.value <= page_number.value) {
                cars.value = [];
            } else {
                cars.value = response.content
            }
        }
    });
}