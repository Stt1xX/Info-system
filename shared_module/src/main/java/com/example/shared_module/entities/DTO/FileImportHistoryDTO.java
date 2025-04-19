package com.example.shared_module.entities.DTO;

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

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getImportStatus() {
        return importStatus;
    }

    public void setImportStatus(String importStatus) {
        this.importStatus = importStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getCompletedCars() {
        return completedCars;
    }

    public void setCompletedCars(Integer completedCars) {
        this.completedCars = completedCars;
    }
}
