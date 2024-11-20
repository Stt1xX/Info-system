import {ref} from "vue";

import {Stomp} from "@stomp/stompjs";
import SockJS from "sockjs-client";

export const coordinates = ref([]);

export let stompClient = null;

export const connect = () => {
    stompClient = Stomp.over(() => new SockJS('/ws'));
    stompClient.connect({}, () => {
        get();
        stompClient.subscribe("/topic/coordinates", (message) => {
            coordinates.value = JSON.parse(message.body);
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
        stompClient.send("/app/get_coordinates", {}, {});
    }
}