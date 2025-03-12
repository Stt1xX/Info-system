package com.example.backend.servicies;

import com.example.backend.entities.DTO.FileImportHistoryDTO;
import com.example.backend.entities.DTO.PageResponseDTO;
import com.example.backend.entities.ImportRecord;
import com.example.backend.entities.enums.ImportStatus;
import com.example.backend.entities.enums.Role;
import com.example.backend.entities.User;
import com.example.backend.repositories.ImportRecordRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ImportRecordService {

    private final UserService userService;
    private final ImportRecordRepository importRecordRepository;

    public ImportRecordService(UserService userService, ImportRecordRepository importRecordRepository) {
        this.userService = userService;
        this.importRecordRepository = importRecordRepository;
    }

    public ResponseEntity<?> getHistory(Integer pageNumber) {
        User user = userService.getCurrentUser();

        Sort sort = Sort.by("id").ascending();
        int PAGE_SIZE = 8;
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, sort);
        Page<ImportRecord> page;

        if (user.getRole() == Role.ROLE_ADMIN) {
            page = importRecordRepository.findAll(pageable);
        } else {
            page = importRecordRepository.findByAuthor_Id(user.getId(), pageable);
        }
        return ResponseEntity.ok(
                new PageResponseDTO<>(page.getContent()
                        .stream()
                        .map(record -> new FileImportHistoryDTO(record.getId(),
                                record.getAuthor().getUsername(),
                                record.getStatus().getDisplayName(),
                                record.getCompletedCars(),
                                record.getCompletedCoordinates(),
                                record.getCompletedHumans()
                                ))
                        .toList(),
                        new PagedModel.PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements())));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createFailedRecord() {
        ImportRecord importRecord = new ImportRecord();
        importRecord.setStatus(ImportStatus.FAILED);
        importRecord.setAuthor(userService.getCurrentUser());
        importRecordRepository.save(importRecord);
    }

//    @Transactional(propagation = Propagation.REQUIRES_NEW) -- not needed
    public void createSuccessRecord(int cars, int humans, int coordinates) {
        ImportRecord importRecord = new ImportRecord();
        importRecord.setStatus(ImportStatus.SUCCESS);
        importRecord.setAuthor(userService.getCurrentUser());
        importRecord.setCompletedCars(cars);
        importRecord.setCompletedHumans(humans);
        importRecord.setCompletedCoordinates(coordinates);
        importRecordRepository.save(importRecord);
    }
}
