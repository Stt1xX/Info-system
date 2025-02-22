import {ref} from "vue";

export let imports = ref([]);
export const pageNumber = ref(0);
export const totalPages = ref(1);

export function get() {
    $.ajax({
        url: `/files/history/${pageNumber.value}`,
        type: "GET",
        success: (response) => {
            totalPages.value = response.page.totalPages;
            pageNumber.value = response.page.number;
            if (totalPages.value <= pageNumber.value) {
                imports.value = [];
            } else {
                imports.value = response.content
            }
        }
    });
}