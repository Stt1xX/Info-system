import {ref} from "vue";

export const AddEditWindowType = {
    ADDING : 1,
    EDITING : 2
}

export const ItemType = {
    CAR : 1,
    COORDINATES : 2,
    HUMAN : 3,
    CAR_HUMAN_SEARCH : 4,
    COORDINATES_HUMAN_SEARCH : 5
}

export const AddHumanPageNumber = {
    SET_CAR : 1,
    SET_COORDINATES : 2,
    SET_HUMAN : 3,
    PREVIEW : 4
}

export const AdditionalFunctionalType = {
    SET_MOOD : 1,
    TRANSFER_HEROES : 2,
    SOUNDTRACK_NUMBER : 3,
    DELETE_BY_IMPACT_SPEED : 4
}

export const getItemName = (value) => {
    switch (value){
        case ItemType.CAR:
            return 'car';
        case ItemType.COORDINATES:
            return 'coordinates';
        case ItemType.HUMAN:
            return 'human';
        default:
            return 'UNDEFINED';
    }
}

export const getUrlPrefix = (value) => {
    switch (value){
        case ItemType.CAR_HUMAN_SEARCH:
        case ItemType.CAR:
            return 'cars';
        case ItemType.COORDINATES_HUMAN_SEARCH:
        case ItemType.COORDINATES:
            return 'coordinates';
        case ItemType.HUMAN:
            return 'humans';
        default:
            return 'UNDEFINED';
    }
}

export const getHeader = (value) => {
    switch (value){
        case ItemType.CAR:
            return 'Cars';
        case ItemType.COORDINATES:
            return 'Coordinates';
        case ItemType.HUMAN:
            return 'Humans';
        default:
            return 'UNDEFINED';
    }
}

export const username = ref()
export const admin_role = ref()

export function get_user_info() {
    $.ajax({
        type: 'GET',
        url : '/users/get_user_info',
        headers: {
            'Content-Type': 'application/json'
        },
        success : (resp) => {
            username.value = resp.username
            admin_role.value = resp.admin_role
        }
    })
}