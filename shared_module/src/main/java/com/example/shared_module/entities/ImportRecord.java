package com.example.shared_module.entities;

import com.example.shared_module.entities.enums.ImportStatus;
import jakarta.persistence.*;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ImportStatus getStatus() {
        return status;
    }

    public void setStatus(ImportStatus status) {
        this.status = status;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Integer getCompletedCars() {
        return completedCars;
    }

    public void setCompletedCars(Integer completedCars) {
        this.completedCars = completedCars;
    }

    public Integer getCompletedCoordinates() {
        return completedCoordinates;
    }

    public void setCompletedCoordinates(Integer completedCoordinates) {
        this.completedCoordinates = completedCoordinates;
    }

    public Integer getCompletedHumans() {
        return completedHumans;
    }

    public void setCompletedHumans(Integer completedHumans) {
        this.completedHumans = completedHumans;
    }
}