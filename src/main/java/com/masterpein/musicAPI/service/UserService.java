package com.masterpein.musicAPI.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.masterpein.musicAPI.entity.Role;
import com.masterpein.musicAPI.entity.User;
import com.masterpein.musicAPI.repository.UserRepository;
import com.masterpein.musicAPI.service.exception.ResourceAlreadyExistsException;
import com.masterpein.musicAPI.service.exception.ResourceNotFoundException;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	public List<User> findAllUsers() {
        return userRepository.findAll();
    }
	
	public User findUserById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
	}
	
	public User findUserByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
	}
	
	@Transactional
    public User createUser(User user) {
        // Check if username already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new ResourceAlreadyExistsException("Username already exists: " + user.getUsername());
        }
        
        // Check if email already exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException("Email already exists: " + user.getEmail());
        }
        
        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Set default role if not specified
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }

        return userRepository.save(user);
	}
	
	@Transactional
	public User updateUser(Long id, User userDetails) {
		User user = findUserById(id);
		
		// Check if new username is already taken by another user
        if (!user.getUsername().equals(userDetails.getUsername()) && 
            userRepository.findByUsername(userDetails.getUsername()).isPresent()) {
            throw new ResourceAlreadyExistsException("Username already exists: " + userDetails.getUsername());
        }
        
        // Check if new email is already taken by another user
        if (!user.getEmail().equals(userDetails.getEmail()) && 
            userRepository.findByEmail(userDetails.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException("Email already exists: " + userDetails.getEmail());
        }
        
        // Update user fields
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        
        // Only admins should be able to change roles (implement this in controller layer)
        if (userDetails.getRole() != null) {
            user.setRole(userDetails.getRole());
        }
        
        return userRepository.save(user);
	}
	
	@Transactional
    public void deleteUser(Long id) {
        User user = findUserById(id);
        userRepository.delete(user);
    }
	
	@Transactional
    public User changeUserRole(Long id, Role role) {
        User user = findUserById(id);
        user.setRole(role);
        return userRepository.save(user);
    }
}
