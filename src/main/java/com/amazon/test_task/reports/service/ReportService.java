package com.amazon.test_task.reports.service;

import com.amazon.test_task.reports.dto.SalesAndTrafficByAsinDto;
import com.amazon.test_task.reports.dto.SalesAndTrafficByDateDto;

import java.util.List;

public interface ReportService {
    SalesAndTrafficByDateDto getSalesAndTrafficByDateRange(String startDate, String endDate);

    SalesAndTrafficByAsinDto getSalesAndTrafficByAsin(List<String> asinList);

    SalesAndTrafficByDateDto getOverallSalesAndTrafficByDate();


}
