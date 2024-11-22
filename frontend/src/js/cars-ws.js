import {ref} from "vue";

import {Stomp} from "@stomp/stompjs";
import SockJS from "sockjs-client";

export const cars = ref([]);

export let stompClient = null;

export const connect = () => {
    stompClient = Stomp.over(() => new SockJS('/ws'));
    stompClient.connect({}, () => {
        get();
        stompClient.subscribe("/topic/cars", (message) => {
            cars.value = JSON.parse(message.body).content;
        });
    });
};

export function disconnect() {
    if (stompClient) {
        stompClient.disconnect(() => {
            stompClient = null;
        });
    }
}

function get() {
    if (stompClient && stompClient.connected) {
        stompClient.send("/app/get_cars", {}, JSON.stringify({
            page: 0,
            size: 1,
            sortBy: "id",
            order: true
        }));
    }
}