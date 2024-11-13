package com.amazon.test_task.role.service;


import com.amazon.test_task.role.entity.Role;

public interface RoleService {
    Role findByName(String name);
}
