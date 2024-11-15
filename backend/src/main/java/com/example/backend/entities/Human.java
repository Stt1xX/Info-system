package com.example.backend.entities;

import com.example.backend.entities.enums.Mood;
import com.example.backend.entities.enums.WeaponType;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Table(name = "humans")
public class Human {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "humans_id_gen")
    @SequenceGenerator(name = "humans_id_gen", sequenceName = "human_human_id_seq", allocationSize = 1)
    @Column(name = "human_id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coordinates_id", nullable = false)
    private Coordinate coordinates;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @Column(name = "real_hero")
    private Boolean realHero;

    @Column(name = "has_toothpick")
    private Boolean hasToothpick;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;

    @Column(name = "mood", columnDefinition = "mood not null")
    private Mood mood;

    @Column(name = "impact_speed", nullable = false)
    private Integer impactSpeed;

    @Column(name = "soundtrack_name", nullable = false, length = Integer.MAX_VALUE)
    private String soundtrackName;

    @Column(name = "minutes_of_waiting")
    private Integer minutesOfWaiting;

    @Column(name = "weapon_type", columnDefinition = "weapon_type not null")
    private WeaponType weaponType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinate getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinate coordinates) {
        this.coordinates = coordinates;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
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

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Mood getMood() {
        return mood;
    }

    public void setMood(Mood mood) {
        this.mood = mood;
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

    public Integer getMinutesOfWaiting() {
        return minutesOfWaiting;
    }

    public void setMinutesOfWaiting(Integer minutesOfWaiting) {
        this.minutesOfWaiting = minutesOfWaiting;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
    }

}