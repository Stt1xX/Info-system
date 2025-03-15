package com.example.consumer.entities.DTO;

import com.example.consumer.entities.Human;
import com.example.consumer.entities.enums.Mood;
import com.example.consumer.entities.enums.WeaponType;
import lombok.Data;

@Data
public class HumanDTO {

    private String name;
    private Integer coordinatesId;
    private Integer carId;
    private boolean realHero;
    private boolean hasToothpick;
    private Mood mood;
    private Integer impactSpeed;
    private String soundtrackName;
    private Long minutesOfWaiting;
    private WeaponType weaponType;
    private String carName;
    private boolean carIsCool;
    private double x;
    private float y;

    public static void setHuman(Human human, HumanDTO humanDTO) {
        human.setName(humanDTO.getName());
        human.setRealHero(humanDTO.isRealHero());
        human.setHasToothpick(humanDTO.isHasToothpick());
        human.setMood(humanDTO.getMood());
        human.setImpactSpeed(humanDTO.getImpactSpeed());
        human.setSoundtrackName(humanDTO.getSoundtrackName());
        human.setWeaponType(humanDTO.getWeaponType());
        human.setMinutesOfWaiting(humanDTO.getMinutesOfWaiting());
    }

    public HumanDTO() {
    }

    public HumanDTO(Human human){
        this.name = human.getName();
        this.coordinatesId = human.getCoordinates().getId();
        this.carId = human.getCar().getId();
        this.realHero = human.getRealHero();
        this.hasToothpick = human.getHasToothpick();
        this.mood = Mood.fromDisplayName(human.getMood().getDisplayName());
        this.impactSpeed = human.getImpactSpeed();
        this.soundtrackName = human.getSoundtrackName();
        this.minutesOfWaiting = human.getMinutesOfWaiting();
        this.weaponType = WeaponType.fromDisplayName(human.getWeaponType().getDisplayName());
    }

    public void setMood(String mood) {
        this.mood = Mood.fromDisplayName(mood);
    }

    public void setWeaponType(String weaponType) {
        this.weaponType = WeaponType.fromDisplayName(weaponType);
    }

    @Override
    public String toString() {
        return "HumanDTO{" +
                "name='" + name + '\'' +
                ", coordinatesId=" + coordinatesId +
                ", carId=" + carId +
                ", realHero=" + realHero +
                ", hasToothpick=" + hasToothpick +
                ", mood=" + mood +
                ", impactSpeed=" + impactSpeed +
                ", soundtrackName='" + soundtrackName + '\'' +
                ", minutesOfWaiting=" + minutesOfWaiting +
                ", weaponType=" + weaponType +
                ", carName='" + carName + '\'' +
                ", carIsCool=" + carIsCool +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
