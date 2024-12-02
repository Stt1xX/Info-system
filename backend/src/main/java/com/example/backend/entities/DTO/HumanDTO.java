package com.example.backend.entities.DTO;

import com.example.backend.entities.Human;
import com.example.backend.entities.enums.Mood;
import com.example.backend.entities.enums.WeaponType;

public class HumanDTO {

    private String name;
    private Integer coordinatesId;
    private Integer carId;
    private Boolean realHero;
    private Boolean hasToothpick;
    private Mood mood;
    private Integer impactSpeed;
    private String soundtrackName;
    private Long minutesOfWaiting;
    private WeaponType weaponType;

    public Human convertToHuman(){
        return new Human(name, weaponType, minutesOfWaiting, impactSpeed, soundtrackName, mood, hasToothpick, realHero);
    }

    public static void setHuman(Human human, HumanDTO humanDTO) {
        human.setName(humanDTO.getName());
        human.setRealHero(humanDTO.getRealHero());
        human.setHasToothpick(humanDTO.getHasToothpick());
        human.setMood(humanDTO.getMood());
        human.setImpactSpeed(humanDTO.getImpactSpeed());
        human.setSoundtrackName(humanDTO.getSoundtrackName());
        human.setWeaponType(humanDTO.getWeaponType());
        human.setMinutesOfWaiting(humanDTO.getMinutesOfWaiting());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCoordinatesId() {
        return coordinatesId;
    }

    public void setCoordinatesId(Integer coordinatesId) {
        this.coordinatesId = coordinatesId;
    }

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public Boolean getRealHero() {
        return realHero;
    }

    public void setRealHero(Boolean realHero) {
        this.realHero = realHero;
    }

    public Boolean getHasToothpick() {
        return hasToothpick;
    }

    public void setHasToothpick(Boolean hasToothpick) {
        this.hasToothpick = hasToothpick;
    }

    public Mood getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = Mood.fromDisplayName(mood);
    }

    public Integer getImpactSpeed() {
        return impactSpeed;
    }

    public void setImpactSpeed(Integer impactSpeed) {
        this.impactSpeed = impactSpeed;
    }

    public String getSoundtrackName() {
        return soundtrackName;
    }

    public void setSoundtrackName(String soundtrackName) {
        this.soundtrackName = soundtrackName;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(String weaponType) {
        this.weaponType = WeaponType.fromDisplayName(weaponType);
    }

    public Long getMinutesOfWaiting() {
        return minutesOfWaiting;
    }

    public void setMinutesOfWaiting(Long minutesOfWaiting) {
        this.minutesOfWaiting = minutesOfWaiting;
    }
}
