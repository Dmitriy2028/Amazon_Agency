package com.amazon.test_task.reports.service;

import com.amazon.test_task.reports.entity.Report;
import com.amazon.test_task.reports.entity.ReportRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class ReportUpdateService {

    private final ReportRepository reportRepository;
    private final ObjectMapper objectMapper;

    /**
     * Method to load data from a file into the database
     */
    @Transactional
    public void loadReportDataFromFile() {
        try {
            // Get InputStream for the file from the classpath
            try (InputStream inputStream = new FileInputStream("/app/db/test_report.json")) {
                // Read the JSON and convert it into a Report object
                Report report = objectMapper.readValue(inputStream, Report.class);

                // Check if there is an existing report with the same marketplaceId
                reportRepository.findByReportSpecification_MarketplaceIdsContaining(report.getReportSpecification().getMarketplaceIds().get(0))
                        .ifPresentOrElse(existingReport -> {
                            // If the report already exists, update its fields
                            existingReport.updateFrom(report); // method to update fields
                            reportRepository.save(existingReport);
                        }, () -> {
                            // If the report is new, save it
                            reportRepository.save(report);
                        });
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading file from classpath: /app/db/test_report.json", e);
        }
    }

    /**
     * Method to update all statistics in the database
     */
    @Transactional
    public void updateReportData() {
        loadReportDataFromFile();
    }

    /**
     * Scheduler that runs the data update every 5 minutes (for testing purposes)
     * The frequency can be adjusted as needed
     */
    @Scheduled(fixedRate = 300000) // 5 minutes in milliseconds
    public void scheduledUpdateReportData() {
        updateReportData();
    }
}
