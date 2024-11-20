import {ref} from "vue";

import {Stomp} from "@stomp/stompjs";
import SockJS from "sockjs-client";

export const cars = ref([{id: "1", name : "Test", author : "Timur", cool : true}]);

export let stompClient = null;

export const connect = () => {
    stompClient = Stomp.over(() => new SockJS('/ws'));
    stompClient.connect({}, () => {
        get();
        stompClient.subscribe("/topic/cars", (message) => {
            cars.value = JSON.parse(message.body);
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
        stompClient.send("/app/get_cars", {}, {});
    }
}