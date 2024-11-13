package com.amazon.test_task.reports.dto;

import lombok.Data;

import java.util.List;

@Data
public class RequestAsinListDto {
    private List<String> asinList;
}
