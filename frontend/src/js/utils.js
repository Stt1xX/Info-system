export const AddEditWindowType = {
    ADDING : 1,
    EDITING : 2
}

export const ItemType = {
    CAR : 1,
    COORDINATES : 2,
    HUMAN : 3
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
        case ItemType.CAR:
            return 'cars';
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