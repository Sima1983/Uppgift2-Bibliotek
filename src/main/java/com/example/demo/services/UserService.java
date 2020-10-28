package com.example.demo.services;



import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Cacheable(value = "userCache")
    public List<User> findAll(){
        log.info("Request to find all users");
        return userRepository.findAll();
    }

    @Cacheable(value = "userCache",key="#id")
    public User findById(String id){
        return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("user not found %s.", id)));
    }

    @CachePut(value = "userCache",key="#result.id")
    public User save(User user){
        log.info("saving user.");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @CachePut(value = "userCache",key="#id")
    public void update(String id, User user){
        log.info("update a user.");

        if(!userRepository.existsById(id)){
            log.error(String.format("Could not find user by id %s", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("not found user by id %s", id));
        }
        user.setId(id);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @CacheEvict(value = "userCache",key="#id")
    public void delete(String id){
        if(!userRepository.existsById(id)){
            log.error(String.format("Could not find user by id %s", id));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Could not find user by id %s", id));
        }
        userRepository.deleteById(id);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
