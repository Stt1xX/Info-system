import {ref} from "vue";

import {Stomp} from "@stomp/stompjs";
// import SockJS from "sockjs-client";

export const messages = ref([]);

export let stompClient = null;

export const connect = () => {
    stompClient = Stomp.over(() => new SockJS('/ws'));
    stompClient.debug = function() {}
    stompClient.connect({}, ()   => {
        get();
        stompClient.subscribe("/topic/reg_requests", (message) => {
            messages.value = JSON.parse(message.body);
        });
    });
};

function get() {
    if (stompClient && stompClient.connected) {
        stompClient.send("/app/admin/get_reg_requests", {}, {});
    }
}

export function accept(id) {
    if (stompClient && stompClient.connected) {
        stompClient.send("/app/admin/accept", {}, id);
    }
}

export function reject(id) {
    if (stompClient && stompClient.connected) {
        stompClient.send("/app/admin/reject", {}, id);
    }
}