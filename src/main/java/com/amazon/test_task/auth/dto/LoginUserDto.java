package com.amazon.test_task.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginUserDto {
    @NotBlank
    @Email
    public String email;
    @NotBlank
    @Size(min = 6)
    public String password;
}
