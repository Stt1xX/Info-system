package com.example.backend.servicies;

import com.example.backend.entities.DTO.RequestDTO;
import com.example.backend.entities.Request;
import com.example.backend.entities.User;
import com.example.backend.entities.enums.Role;
import com.example.backend.repositories.RequestRepository;
import com.example.backend.repositories.UserRepository;
import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    private final SimpMessagingTemplate messagingTemplate;
    private final EntityManager entityManager;

    @Autowired
    public RequestService(RequestRepository requestRepository, UserRepository userRepository, SimpMessagingTemplate messagingTemplate, EntityManager entityManager) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
        this.entityManager = entityManager;
    }

    public void acceptRequestById(Integer id) {
        Request request = requestRepository.findById(id).orElse(null);
        if (request == null) {
            return;
        }
        requestRepository.deleteById(id);
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPasswordWithoutEncode(request.getPassword());
        user.setRole(Role.ROLE_ADMIN);
        userRepository.save(user);
    }

    public void rejectRequestById(Integer id) {
        requestRepository.deleteById(id);
    }

    @Transactional
    public ResponseEntity<?> addRequest(Request request) {
        if (requestRepository.findByUsername(request.getUsername()) != null ) {
            return new ResponseEntity<>("Запрос с таким именем уже существует", HttpStatus.CONFLICT);
        }
        try{
            requestRepository.save(request);
            entityManager.clear(); // clear cache
            notifyAdminsAboutNewRequest();
            return new ResponseEntity<>("Запрос на добавление успешно создан!", HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("Некорректные данные пользователей", HttpStatus.CONFLICT);
        }
    }

    public List<RequestDTO> getAllRequests() {
        return requestRepository
                .findAll()
                .stream()
                .map(RequestDTO::convertToDTO)
                .collect(Collectors.toList());
    }

    public Request getRequestByUsername(String username) {
        return requestRepository.findByUsername(username);
    }

    public void notifyAdminsAboutNewRequest() {
        messagingTemplate.convertAndSend("/topic/reg_requests", getAllRequests());

    }
}
