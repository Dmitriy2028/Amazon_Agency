package com.amazon.test_task.reports.controller;


import com.amazon.test_task.reports.dto.RequestAsinListDto;
import com.amazon.test_task.reports.dto.RequestDateRangeDto;
import com.amazon.test_task.reports.dto.SalesAndTrafficByAsinDto;
import com.amazon.test_task.reports.dto.SalesAndTrafficByDateDto;
import com.amazon.test_task.reports.service.ReportServiceImpl;
import com.amazon.test_task.reports.service.ReportUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/closed/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportServiceImpl reportServiceImpl;
    private final ReportUpdateService reportUpdateService;

    @GetMapping("/date/all")
    public ResponseEntity<SalesAndTrafficByDateDto> getOverallSalesAndTrafficByDate() {
        return ResponseEntity.ok(reportServiceImpl.getOverallSalesAndTrafficByDate());
    }

    @GetMapping("/asin/all")
    public ResponseEntity<SalesAndTrafficByAsinDto> getOverallSalesAndTrafficByAsin() {
        return ResponseEntity.ok(reportServiceImpl.getSalesAndTrafficByAsin(null));
    }

    @GetMapping("/date")
    public ResponseEntity<SalesAndTrafficByDateDto> getSalesAndTrafficByDateRange(@RequestBody RequestDateRangeDto requestDateRangeDto) {
        return ResponseEntity.ok(reportServiceImpl.getSalesAndTrafficByDateRange(requestDateRangeDto.getStartDate(), requestDateRangeDto.getEndDate()));
    }

    @GetMapping("/asin")
    public ResponseEntity<SalesAndTrafficByAsinDto> getSalesAndTrafficByAsin(@RequestBody RequestAsinListDto requestAsinListDto) {
        return ResponseEntity.ok(reportServiceImpl.getSalesAndTrafficByAsin(requestAsinListDto.getAsinList()));
    }

    @PostMapping("/update-db")
    public ResponseEntity<?> updateReportData() {
        reportUpdateService.updateReportData();
        return ResponseEntity.status(202).body("");
    }
    
}
