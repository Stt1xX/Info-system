package com.example.backend.entities;

import com.example.backend.entities.Anntotations.SearchableField;
import com.example.backend.entities.Anntotations.SortableField;
import com.example.backend.entities.enums.Mood;
import com.example.backend.entities.enums.WeaponType;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "humans")
public class Human extends ManagedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "humans_seq_gen")
    @SequenceGenerator(name = "humans_seq_gen", sequenceName = "humans_id_seq", allocationSize = 1)
    @Column(name = "human_id", nullable = false)
    @SortableField(name = "id")
    @SearchableField(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    @SortableField(name = "name")
    @SearchableField(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coordinates_id", nullable = false, foreignKey = @ForeignKey(name = "coordinates_id",
            foreignKeyDefinition = "FOREIGN KEY (coordinates_id) REFERENCES coordinates(id) ON DELETE CASCADE"))
    private Coordinates coordinates;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "creation_date", insertable = false, updatable = false)
    private Instant creationDate;

    @Column(name = "real_hero")
    private Boolean realHero;

    @Column(name = "has_toothpick")
    private Boolean hasToothpick;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id", nullable = false, foreignKey = @ForeignKey(name = "car_id",
            foreignKeyDefinition = "FOREIGN KEY (car_id) REFERENCES cars(id) ON DELETE CASCADE"))
    private Car car;

    @Enumerated(EnumType.STRING)
    @Column(name = "mood", nullable = false)
    private Mood mood;

    @Column(name = "impact_speed", nullable = false)
    @SortableField(name = "impact speed")
    @SearchableField(name = "impact speed")
    private Integer impactSpeed;

    @Column(name = "soundtrack_name", nullable = false, length = Integer.MAX_VALUE)
    @SortableField(name = "soundtrack name")
    @SearchableField(name = "soundtrack name")
    private String soundtrackName;

    @Column(name = "minutes_of_waiting")
    @SortableField(name = "minutes of waiting")
    @SearchableField(name = "minutes of waiting")
    private Long minutesOfWaiting;

    @Enumerated(EnumType.STRING)
    @Column(name = "weapon_type", nullable = false)
    @SortableField(name = "weapon type")
    @SearchableField(name = "weapon type")
    private WeaponType weaponType;

    public Human(String name, WeaponType weaponType, Long minutesOfWaiting, Integer impactSpeed, String soundtrackName, Mood mood, Boolean hasToothpick, Boolean realHero) {
        this.name = name;
        this.weaponType = weaponType;
        this.minutesOfWaiting = minutesOfWaiting;
        this.impactSpeed = impactSpeed;
        this.soundtrackName = soundtrackName;
        this.mood = mood;
        this.hasToothpick = hasToothpick;
        this.realHero = realHero;
    }

    public Human() {

    }

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

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
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

    public String getMood() {
        return mood.getDisplayName();
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

    public Long getMinutesOfWaiting() {
        return minutesOfWaiting;
    }

    public void setMinutesOfWaiting(Long minutesOfWaiting) {
        this.minutesOfWaiting = minutesOfWaiting;
    }

    public String getWeaponType() {
        return weaponType.getDisplayName();
    }

    public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Human human = (Human) o;
        return Objects.equals(name, human.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}