import {ref} from "vue";

import {Stomp} from "@stomp/stompjs";
// import SockJS from "sockjs-client";
import {getUrlPrefix, ItemType} from "@/js/utils.js";

const defObj = () => ({
    data: [],
    total_pages: 0,
    page_number: 0,
    size_number: 8,
    sortBy: 'id',
    order: true,
    searchBy: 'id',
    searchTemplate : ''
});

export const objects = ref({
    [ItemType.CAR] : defObj(),
    [ItemType.COORDINATES] : defObj(),
    [ItemType.HUMAN] : defObj()
});

let stompClient= new Array(3); // HUMANS, CARS, COORDINATES

export const connect = (itemType) => {
    stompClient[itemType] = Stomp.over(() => new SockJS('/ws'));
    stompClient[itemType].debug = function() {}
    stompClient[itemType].connect({}, () => {
        get(itemType);
        stompClient[itemType].subscribe(`/topic/${getUrlPrefix(itemType)}`, () => {
            get(itemType);
        });
    });
};

export function get(itemType) {
    const obj = objects.value[itemType]
    $.ajax({
        url: `/${getUrlPrefix(itemType)}/get_all`,
        type: "GET",
        contentType: "application/json",
        data : {
            page: obj.page_number,
            size: obj.size_number,
            sortBy: obj.sortBy,
            order: obj.order,
            template: obj.searchTemplate,
            varName: obj.searchBy
        },
        success: (response) => {
            objects.value[itemType].total_pages = response.page.totalPages;
            objects.value[itemType].page_number = response.page.number;
            if (response.page.totalPages <= response.page.number) {
                objects.value[itemType].data = [];
            } else {
                objects.value[itemType].data = response.content
            }
        }
    });
}