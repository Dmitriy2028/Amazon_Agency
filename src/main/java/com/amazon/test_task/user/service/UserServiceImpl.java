package com.amazon.test_task.user.service;

import com.amazon.test_task.auth.dto.CreateUserDto;
import com.amazon.test_task.auth.dto.ResponseUserDto;
import com.amazon.test_task.role.service.RoleService;
import com.amazon.test_task.user.entity.User;
import com.amazon.test_task.user.entity.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public void passwordEncoder(@Lazy PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Method to find a user by their email.
     * If the user is not found, throws a UsernameNotFoundException.
     */
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found", email)));
    }

    /**
     * Method to save a user in the database.
     */
    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    /**
     * Method to get user details by ID and map them to a ResponseUserDto.
     * Throws EntityNotFoundException if the user with the given ID does not exist.
     */
    @Override
    public ResponseUserDto getUserById(Long id) {
        return userRepository.findById(id).map(this::mapToResponseUserDto).orElseThrow(() -> new EntityNotFoundException("Id not found"));
    }

    /**
     * Method to load user details by username (email).
     * It uses the findByEmail method and returns a Spring Security User object.
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findByEmail(email);
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
        );
    }

    /**
     * Method to create a new user from the provided CreateUserDto.
     * The password is encoded before saving the user.
     */
    @Override
    @Transactional
    public void createUser(CreateUserDto createUserDto) {
        User user = new User();
        user.setFirstName(createUserDto.getFirstName());
        user.setLastName(createUserDto.getLastName());
        user.setEmail(createUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        user.setRoles(List.of(roleService.findByName("ROLE_PERSONAL")));

        userRepository.save(user);
    }

    /**
     * Method to check if a user exists by their email.
     */
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Helper method to map a User entity to a ResponseUserDto.
     */
    private ResponseUserDto mapToResponseUserDto(User user) {
        return ResponseUserDto
                .builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }
}
