package com.example.consumer.entities;

import com.example.consumer.entities.enums.ImportStatus;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "import_record")
public class ImportRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "import_record_id_gen")
    @SequenceGenerator(name = "import_record_id_gen", sequenceName = "importfile_record_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ImportStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "user_id",
            foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE"))
    private User author;

    @Column(name = "completed_cars")
    private Integer completedCars;

    @Column(name = "completed_coordinates")
    private Integer completedCoordinates;

    @Column(name = "completed_humans")
    private Integer completedHumans;
}