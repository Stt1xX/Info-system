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