package com.amazon.test_task.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseUserDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
}
