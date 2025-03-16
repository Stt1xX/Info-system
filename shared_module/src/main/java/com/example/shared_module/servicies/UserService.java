package com.example.shared_module.servicies;

import com.example.shared_module.entities.Request;
import com.example.shared_module.entities.User;
import com.example.shared_module.entities.enums.Role;
import com.example.shared_module.repositories.UserRepository;
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

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userRepository.findByUsername(userDetails.getUsername());
        }
        return new User();
    }

    public ResponseEntity<?> addUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return new ResponseEntity<>("A user with that name already exists", HttpStatus.CONFLICT);
        }
        if (requestService.getRequestByUsername(user.getUsername()) != null) {
            return new ResponseEntity<>("A request with this name already exists", HttpStatus.CONFLICT);
        }
        try{
            if (user.getRole() == Role.ROLE_ADMIN && !userRepository.findByRole(user.getRole()).isEmpty()) {
                return requestService.addRequest(new Request(user.getUsername(), user.getPassword()));
            }
            userRepository.save(user);
            return new ResponseEntity<>("The user has been successfully created!", HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("Incorrect user data", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<?> getUserInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("username", getCurrentUser().getUsername());
        map.put("admin_role", getCurrentUser().getRole() == Role.ROLE_ADMIN);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @Transactional
    public void deleteUser() {
        String username = getCurrentUser().getUsername();
        userRepository.deleteByUsername(username);
    }

    public Map<String, String> getSocketMessage(){
        Map<String, String> response = new HashMap<>();
        response.put("user_id", "");
        response.put("signal", "update");
        return response;
    }
}
