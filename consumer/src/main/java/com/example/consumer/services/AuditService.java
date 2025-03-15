package com.example.consumer.services;

import com.example.consumer.entities.Commit;
import com.example.consumer.entities.enums.EntityType;
import com.example.consumer.repositories.CommitRepository;
import com.example.consumer.utils.Utils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuditService {

    private final CommitRepository commitRepository;

    public AuditService(CommitRepository commitRepository) {
        this.commitRepository = commitRepository;
    }


    public ResponseEntity<?> doCommits(List<Integer> entityIds, EntityType entityType, String action){
        List<Commit> commits = new ArrayList<>();
        String author = UserGlobalService.getCurrentUser().getUsername();
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
//            simpMessagingTemplate.convertAndSend("/topic/audit", "");
            return new ResponseEntity<>("Commits successfully added!", HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("Error: Incorrect commit data", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<?> doCommit(Integer entityId, EntityType entityType, String action){
        Commit commit = new Commit();
        commit.setAuthor(UserGlobalService.getCurrentUser().getUsername());
        commit.setItemType(entityType);
        commit.setItemId(entityId);
        commit.setDate(Utils.prepareDate(Instant.now()));
        commit.setAction(action);
        try{
            commitRepository.save(commit);
//            simpMessagingTemplate.convertAndSend("/topic/audit", "");
            return new ResponseEntity<>("Commit successfully added!", org.springframework.http.HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("Error: Incorrect commit data", HttpStatus.CONFLICT);
        }
    }
}
