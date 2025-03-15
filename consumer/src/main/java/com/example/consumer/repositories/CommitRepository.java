package com.example.consumer.repositories;

import com.example.consumer.entities.Commit;
import com.example.consumer.entities.enums.EntityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommitRepository extends JpaRepository<Commit, Integer> {
    void deleteAllByItemTypeAndItemId(EntityType itemType, Integer id);
    Page<Commit> findAllByItemIdAndItemType(Integer itemId, EntityType itemType, Pageable pageable);
}
