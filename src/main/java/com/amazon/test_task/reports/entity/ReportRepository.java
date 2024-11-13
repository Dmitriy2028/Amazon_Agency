package com.amazon.test_task.reports.entity;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends MongoRepository<Report, String> {
    // Метод для поиска отчета по marketplaceIds
    Optional<Report> findByReportSpecification_MarketplaceIdsContaining(String marketplaceId);
}
