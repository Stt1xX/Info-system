package com.example.consumer.entities.DTO;

import lombok.Data;

@Data
public class FileImportHistoryDTO {

    private String authorName;
    private String importStatus;
    private Integer completedCars;
    private Integer completedCoordinates;
    private Integer completedHumans;
    private Integer id;

    public FileImportHistoryDTO(Integer id, String authorName, String importStatus, Integer completedCars, Integer completedCoordinates, Integer completedHumans) {
        this.authorName = authorName;
        this.importStatus = importStatus;
        this.completedCars = completedCars;
        this.completedCoordinates = completedCoordinates;
        this.completedHumans = completedHumans;
        this.id = id;
    }
}
