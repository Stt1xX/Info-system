package com.example.shared_module.repositories;

import com.example.shared_module.entities.ImportRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImportRecordRepository extends JpaRepository<ImportRecord, Integer> {
    Page<ImportRecord> findByAuthor_Id(Integer authorId, Pageable pageable);
}
