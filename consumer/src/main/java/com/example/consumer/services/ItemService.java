package com.example.consumer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public abstract class ItemService<ClassDTO , MainClass> {

    protected final JpaRepository<MainClass, Integer> repository;
    protected final JpaSpecificationExecutor<MainClass> specificationExecutor;
    protected final AuditService auditService;
    protected final Checker checker;
    protected final Class<MainClass> mainClassType;

    @Autowired
    public ItemService(JpaRepository<MainClass, Integer> repository, AuditService auditService, Checker checker, Class<MainClass> mainClassType) {
        this.repository = repository;
        this.specificationExecutor = (JpaSpecificationExecutor<MainClass>) repository;
        this.auditService = auditService;
        this.checker = checker;
        this.mainClassType = mainClassType;
    }

    @Retryable(value = CannotAcquireLockException.class, maxAttempts = 3, backoff = @Backoff(delay = 500))
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public abstract ResponseEntity<?> add(ClassDTO classDTO);

    // Also Serializable but Annotation set on mainImport method in FileService
    public abstract ResponseEntity<?> addAll(Map<Integer, ClassDTO> classDTOs);

    public List<MainClass> getAll(){
        return repository.findAll();
    }

    public MainClass findById(Integer id){
        return repository.findById(id).orElse(null);
    }

    @SuppressWarnings("SpringTransactionalMethodCallsInspection")
    public MainClass getObjWhileAddFunc(ClassDTO DTO) {
        ResponseEntity<?> resp = add(DTO);
        if (resp.getStatusCode() == HttpStatus.OK) {
            return (MainClass)(((List<Object>)(Objects.requireNonNull(resp.getBody()))).get(1));
        } else {
            throw new DataIntegrityViolationException((String) resp.getBody());
        }
    }
}
