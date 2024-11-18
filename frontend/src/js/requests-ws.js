import {Stomp} from "@stomp/stompjs";
import SockJS from "sockjs-client";
import {ref} from "vue";

export const messages = ref([]);

export let stompClient = null;

export const connect = () => {
    stompClient = Stomp.over(() => new SockJS('/ws'));
    stompClient.connect({}, () => {
        get();
        stompClient.subscribe("/topic/reg_requests", (message) => {
            messages.value = JSON.parse(message.body);
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
        stompClient.send("/app/get_reg_requests", {}, {});
    }
}

export function accept(id) {
    if (stompClient && stompClient.connected) {
        stompClient.send("/app/accept", {}, id);
    }
}

export function reject(id) {
    if (stompClient && stompClient.connected) {
        stompClient.send("/app/reject", {}, id);
    }
}