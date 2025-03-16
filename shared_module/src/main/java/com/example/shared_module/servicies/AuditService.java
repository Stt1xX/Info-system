package com.example.shared_module.servicies;

import com.example.shared_module.entities.*;
import com.example.shared_module.entities.DTO.CommitDTO;
import com.example.shared_module.entities.DTO.PageResponseDTO;
import com.example.shared_module.entities.enums.EntityType;
import com.example.shared_module.repositories.CommitRepository;
import com.example.shared_module.utils.Utils;
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
import java.util.ArrayList;
import java.util.List;

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
                        .map(commit -> new CommitDTO(commit.getDate(),
                                commit.getAuthor(),
                                commit.getAction()))
                        .toList(),
                new PagedModel.PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements())));
    }

    public ResponseEntity<?> doCommit(Integer entityId, EntityType entityType, String action, boolean shouldTrigger, User user){
        Commit commit = new Commit();
        commit.setAuthor(user.getUsername());
        commit.setItemType(entityType);
        commit.setItemId(entityId);
        commit.setDate(Utils.prepareDate(Instant.now()));
        commit.setAction(action);
        try{
            commitRepository.save(commit);
            if (shouldTrigger){
                simpMessagingTemplate.convertAndSend("/topic/audit", "");
            }
            return new ResponseEntity<>("Commit successfully added!", org.springframework.http.HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("Error: Incorrect commit data", HttpStatus.CONFLICT);
        }
    }


    public ResponseEntity<?> doCommits(List<Integer> entityIds, EntityType entityType, String action, User user){
        List<Commit> commits = new ArrayList<>();
        String author = user.getUsername();
        String date = Utils.prepareDate(Instant.now());
        for (Integer entityId : entityIds) {
            Commit commit = new Commit();
            commit.setAuthor(author);
            commit.setItemType(entityType);
            commit.setItemId(entityId);
            commit.setDate(date);
            commit.setAction(action);
            commits.add(commit);
        }
        try {
            commitRepository.saveAll(commits);
            return new ResponseEntity<>("Commits successfully added!", org.springframework.http.HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("Error: Incorrect commit data", HttpStatus.CONFLICT);
        }
    }

    @Transactional
    public void deleteCommits(Integer entityId, EntityType entityType){
        commitRepository.deleteAllByItemTypeAndItemId(entityType, entityId);
    }
}
