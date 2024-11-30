package com.example.backend.servicies;

import com.example.backend.entities.DTO.PageRequestDTO;
import com.example.backend.entities.DTO.PageResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.example.backend.repositories.DunamicQuery.Specification.hasNameContaining;

@Service
public abstract class ItemService<ClassDTO , MainClass> {

    protected final JpaRepository<MainClass, Integer> repository;
    protected final JpaSpecificationExecutor<MainClass> specificationExecutor;
    protected final UserService userService;
    protected final SimpMessagingTemplate simpMessagingTemplate;
    protected final Checker checker;

    @Autowired
    public ItemService(JpaRepository<MainClass, Integer> repository, UserService userService, SimpMessagingTemplate simpMessagingTemplate, Checker checker) {
        this.repository = repository;
        this.specificationExecutor = (JpaSpecificationExecutor<MainClass>) repository;
        this.userService = userService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.checker = checker;
    }

    public abstract ResponseEntity<?> add(ClassDTO classDTO);

    public abstract ResponseEntity<?> update(Integer id, ClassDTO classDTO);

    public abstract ResponseEntity<?> delete(Integer id);

    public ResponseEntity<?> getAll(PageRequestDTO pageRequest) {
        Sort sort = pageRequest.isOrder() ? Sort.by(pageRequest.getSortBy()).ascending() : Sort.by(pageRequest.getSortBy()).descending();
        Pageable pageable = PageRequest.of(pageRequest.getPage(), pageRequest.getSize(), sort);
        Specification<MainClass> spec = (Specification<MainClass>) hasNameContaining(pageRequest.getTemplate(), pageRequest.getVarName());
        Page<MainClass> page;
        try{
            page = specificationExecutor.findAll(spec, pageable);
        }
        catch(InvalidDataAccessApiUsageException e){
            return new ResponseEntity<>("Error: Incorrect search field", HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok(new PageResponseDTO<>(page.getContent(),
                new PagedModel.PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements())));
    }

    public Map<String, String> getSocketMessage(){
        Map<String, String> response = new HashMap<>();
        response.put("user_id", userService.getCurrentUser().getId().toString());
        response.put("signal", "update");
        return response;
    }
}
