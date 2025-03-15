package com.example.consumer.entities;

import com.example.consumer.entities.enums.Mood;
import com.example.consumer.entities.enums.WeaponType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.Objects;

@Data
@Entity
@Table(name = "humans")
public class Human extends ManagedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "humans_seq_gen")
    @SequenceGenerator(name = "humans_seq_gen", sequenceName = "humans_id_seq", allocationSize = 1)
    @Column(name = "human_id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
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
    private Integer impactSpeed;

    @Column(name = "soundtrack_name", nullable = false, length = Integer.MAX_VALUE)
    private String soundtrackName;

    @Column(name = "minutes_of_waiting")
    private Long minutesOfWaiting;

    @Enumerated(EnumType.STRING)
    @Column(name = "weapon_type", nullable = false)
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