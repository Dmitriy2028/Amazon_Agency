package com.amazon.test_task.user.service;


import com.amazon.test_task.auth.dto.CreateUserDto;
import com.amazon.test_task.auth.dto.ResponseUserDto;
import com.amazon.test_task.user.entity.User;

public interface UserService {

    void createUser(CreateUserDto createUserDto);

    boolean existsByEmail(String email);

    User findByEmail(String email);

    void save(User user);

    ResponseUserDto getUserById(Long id);
}
