package com.example.consumer.services;

import com.example.consumer.entities.ImportRecord;
import com.example.consumer.entities.User;
import com.example.consumer.entities.enums.ImportStatus;
import com.example.consumer.repositories.ImportRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ImportRecordService {

    private final ImportRecordRepository importRecordRepository;

    public ImportRecordService(ImportRecordRepository importRecordRepository) {
        this.importRecordRepository = importRecordRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createFailedRecord() {
        ImportRecord importRecord = new ImportRecord();
        importRecord.setStatus(ImportStatus.FAILED);
        importRecord.setAuthor(UserGlobalService.getCurrentUser());
        importRecordRepository.save(importRecord);
    }

//    @Transactional(propagation = Propagation.REQUIRES_NEW) -- not needed
    public void createSuccessRecord(int cars, int humans, int coordinates) {
        ImportRecord importRecord = new ImportRecord();
        importRecord.setStatus(ImportStatus.SUCCESS);
        importRecord.setAuthor(UserGlobalService.getCurrentUser());
        importRecord.setCompletedCars(cars);
        importRecord.setCompletedHumans(humans);
        importRecord.setCompletedCoordinates(coordinates);
        importRecordRepository.save(importRecord);
    }
}
