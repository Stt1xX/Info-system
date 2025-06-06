package com.example.shared_module.servicies;

import com.example.shared_module.entities.DTO.PageRequestDTO;
import com.example.shared_module.entities.DTO.PageResponseDTO;
import com.example.shared_module.entities.User;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.transaction.annotation.Transactional;
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
import org.springframework.transaction.annotation.Isolation;

import java.util.*;

import static com.example.shared_module.repositories.DunamicQuery.Specification.hasNameContaining;

@Service
public abstract class ItemService<ClassDTO , MainClass> {

    protected final JpaRepository<MainClass, Integer> repository;
    protected final JpaSpecificationExecutor<MainClass> specificationExecutor;
    protected final UserService userService;
    protected final AuditService auditService;
    protected final SimpMessagingTemplate simpMessagingTemplate;
    protected final Checker checker;
    protected final Class<MainClass> mainClassType;

    @Autowired
    public ItemService(JpaRepository<MainClass, Integer> repository, UserService userService, AuditService auditService, SimpMessagingTemplate simpMessagingTemplate, Checker checker, Class<MainClass> mainClassType) {
        this.repository = repository;
        this.specificationExecutor = (JpaSpecificationExecutor<MainClass>) repository;
        this.userService = userService;
        this.auditService = auditService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.checker = checker;
        this.mainClassType = mainClassType;
    }

    @Retryable(value = CannotAcquireLockException.class, maxAttempts = 3, backoff = @Backoff(delay = 500))
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public abstract ResponseEntity<?> add(ClassDTO classDTO, boolean shouldTrigger, User user);

    // Also Serializable but Annotation set on mainImport method in FileService
    public abstract ResponseEntity<?> addAll(Map<Integer, ClassDTO> classDTOs, User user);

    @Retryable(value = CannotAcquireLockException.class, maxAttempts = 3, backoff = @Backoff(delay = 500))
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public abstract ResponseEntity<?> update(Integer id, ClassDTO classDTO);

    @Transactional
    public abstract ResponseEntity<?> delete(Integer id);

    public ResponseEntity<?> getCommits(Integer id, Integer pageNumber){
        return auditService.getCommits(id, pageNumber, mainClassType);
    }

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

    public List<MainClass> getAll(){
        return repository.findAll();
    }

    public MainClass findById(Integer id){
        return repository.findById(id).orElse(null);
    }

    @SuppressWarnings("SpringTransactionalMethodCallsInspection")
    public MainClass getObjWhileAddFunc(ClassDTO DTO, User user) {
        ResponseEntity<?> resp = add(DTO, false, user);
        if (resp.getStatusCode() == HttpStatus.OK) {
            return (MainClass)(((List<Object>)(Objects.requireNonNull(resp.getBody()))).get(1));
        } else {
            throw new DataIntegrityViolationException((String) resp.getBody());
        }
    }
}
