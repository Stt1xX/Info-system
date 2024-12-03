import {ref} from "vue";

import {Stomp} from "@stomp/stompjs";
// import SockJS from "sockjs-client";
import {getUrlPrefix} from "@/js/utils.js";

export const commits = ref([]);
export const pageNumber = ref(0);
export const totalPages = ref(1);
export let stompClient = null;

export const connect = (id, itemCode) => {
    stompClient = Stomp.over(() => new SockJS('/ws'));
    stompClient.debug = function() {}
    stompClient.connect({}, ()   => {
        get(id, itemCode);
        stompClient.subscribe(`/topic/audit`, () => {
            get(id, itemCode);
        });
    });
};

export function get(id, itemCode) {
    $.ajax({
        url: `/${getUrlPrefix(itemCode)}/get_commits/${id}/${pageNumber.value}`,
        type: "GET",
        success: (response) => {
            commits.value = response.content;
            totalPages.value = response.page.totalPages;
            pageNumber.value = response.page.number;
            if (totalPages.value <= pageNumber.value) {
                commits.value = [];
            } else {
                commits.value = response.content
            }
        }
    });
}

export const disconnect = () => {
    if (stompClient && stompClient.connected) {
        stompClient.disconnect();
    }
};