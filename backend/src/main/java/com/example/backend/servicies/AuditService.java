package com.example.backend.servicies;

import com.example.backend.entities.Car;
import com.example.backend.entities.Commit;
import com.example.backend.entities.Coordinates;
import com.example.backend.entities.DTO.CommitDTO;
import com.example.backend.entities.DTO.PageResponseDTO;
import com.example.backend.entities.Human;
import com.example.backend.entities.enums.EntityType;
import com.example.backend.repositories.CommitRepository;
import com.example.backend.utils.Utils;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class AuditService {

    private final CommitRepository commitRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserService userService;

    public AuditService(CommitRepository commitRepository, SimpMessagingTemplate simpMessagingTemplate, UserService userService) {
        this.commitRepository = commitRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.userService = userService;
    }

    public ResponseEntity<?> getCommits(Integer id, Integer pageNumber, Class<?> itemClass){
        EntityType entityType;
        if (itemClass.equals(Car.class)) {
            entityType = EntityType.CAR;
        } else if (itemClass.equals(Coordinates.class)) {
            entityType = EntityType.COORDINATES;
        } else if (itemClass.equals(Human.class)) {
            entityType = EntityType.HUMAN;
        } else {
            return new ResponseEntity<>("Error: Incorrect item type", HttpStatus.CONFLICT);
        }
        Sort sort = Sort.by("id").ascending();
        int PAGE_SIZE = 8;
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, sort);

        Page<Commit> page = commitRepository.findAllByItemIdAndItemType(id, entityType, pageable);
        return ResponseEntity.ok(

                new PageResponseDTO<>(page.getContent()
                        .stream()
                        .map(commit -> new CommitDTO(commit.getDate(), commit.getAuthor().getUsername(), commit.getAction()))
                        .toList(),
                new PagedModel.PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements())));
    }

    public ResponseEntity<?> doCommit(Integer entityId, EntityType entityType, String action){
        Commit commit = new Commit();
        commit.setAuthor(userService.getCurrentUser());
        commit.setItemType(entityType);
        commit.setItemId(entityId);
        commit.setDate(Utils.prepareDate(Instant.now()));
        commit.setAction(action);
        try{
            commitRepository.save(commit);
            simpMessagingTemplate.convertAndSend("/topic/audit", "");
            return new ResponseEntity<>("Commit successfully added!", org.springframework.http.HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("Error: Incorrect commit data", HttpStatus.CONFLICT);
        }
    }

    @Transactional
    public void deleteCommits(Integer entityId, EntityType entityType){
        commitRepository.deleteAllByItemTypeAndItemId(entityType, entityId);
    }
}
