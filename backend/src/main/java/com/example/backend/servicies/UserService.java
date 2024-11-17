package com.example.backend.servicies;

import com.example.backend.entities.Request;
import com.example.backend.entities.User;
import com.example.backend.entities.enums.Role;
import com.example.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RequestService requestService;

    @Autowired
    public UserService(UserRepository userRepository, RequestService requestService) {
        this.userRepository = userRepository;
        this.requestService = requestService;
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        return null;
    }

    public Boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return Role.ROLE_ADMIN == Role.valueOf(userDetails.getAuthorities().stream().toList().get(0).toString());
        }
        return false;
    }

    public ResponseEntity<?> addUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return new ResponseEntity<>("Пользователь с таким именем уже существует", HttpStatus.CONFLICT);
        }
        if (requestService.getRequestByUsername(user.getUsername()) != null) {
            return new ResponseEntity<>("Запрос с таким именем уже существует", HttpStatus.CONFLICT);
        }
        try{
            if (user.getRole() == Role.ROLE_ADMIN && !userRepository.findByRole(user.getRole()).isEmpty()) {
                return requestService.addRequest(new Request(user.getUsername(), user.getPassword()));
            }
            userRepository.save(user);
            return new ResponseEntity<>("Пользователь успешно создан!", HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("Некорректные данные пользователей", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<?> getUserInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("username", getCurrentUsername());
        map.put("admin_role", isAdmin());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @Transactional
    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }
}
