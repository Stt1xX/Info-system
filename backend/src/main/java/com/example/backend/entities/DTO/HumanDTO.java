package com.example.backend.entities.DTO;

import com.example.backend.entities.Human;
import com.example.backend.entities.enums.Mood;
import com.example.backend.entities.enums.WeaponType;

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
        human.setRealHero(humanDTO.getRealHero());
        human.setHasToothpick(humanDTO.getHasToothpick());
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
        this.mood = Mood.fromDisplayName(human.getMood());
        this.impactSpeed = human.getImpactSpeed();
        this.soundtrackName = human.getSoundtrackName();
        this.minutesOfWaiting = human.getMinutesOfWaiting();
        this.weaponType = WeaponType.fromDisplayName(human.getWeaponType());
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

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public Boolean getCarIsCool() {
        return carIsCool;
    }

    public void setCarIsCool(Boolean carIsCool) {
        this.carIsCool = carIsCool;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
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
