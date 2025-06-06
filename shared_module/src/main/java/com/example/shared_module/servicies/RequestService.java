package com.example.shared_module.servicies;

import com.example.shared_module.entities.DTO.RegRequestDTO;
import com.example.shared_module.entities.Request;
import com.example.shared_module.entities.User;
import com.example.shared_module.entities.enums.Role;
import com.example.shared_module.repositories.RequestRepository;
import com.example.shared_module.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    public RequestService(RequestRepository requestRepository, UserRepository userRepository,
                          SimpMessagingTemplate messagingTemplate, EntityManager entityManager) {
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
            return new ResponseEntity<>("A request with this name already exists", HttpStatus.CONFLICT);
        }
        try{
            requestRepository.save(request);
            entityManager.clear(); // clear cache
            notifyAdminsAboutNewRequest();
            return new ResponseEntity<>("The request to add has been successfully created!", HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("Incorrect user data", HttpStatus.CONFLICT);
        }
    }

    public List<RegRequestDTO> getAllRequests() {
        return requestRepository
                .findAll()
                .stream()
                .map(RegRequestDTO::convertToDTO)
                .collect(Collectors.toList());
    }

    public Request getRequestByUsername(String username) {
        return requestRepository.findByUsername(username);
    }

    public void notifyAdminsAboutNewRequest() {
        messagingTemplate.convertAndSend("/topic/reg_requests", getAllRequests());

    }
}
