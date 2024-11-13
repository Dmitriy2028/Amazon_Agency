package com.amazon.test_task.reports.service;

import com.amazon.test_task.reports.dto.SalesAndTrafficByAsinDto;
import com.amazon.test_task.reports.dto.SalesAndTrafficByDateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;



@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService{

    private final MongoTemplate mongoTemplate;

    // Caches the result of fetching sales and traffic data by date range
    @Override
    @Cacheable(value = "getSalesAndTrafficByDateRange", key = "#startDate + '-' + (#endDate ?: #startDate)")
    public SalesAndTrafficByDateDto getSalesAndTrafficByDateRange(String startDate, String endDate) {
        String actualEndDate = (endDate != null && !endDate.isEmpty()) ? endDate : startDate;
        // Builds the aggregation query for the date range
        Aggregation aggregation = buildAggregationByDateRange("salesAndTrafficByDate", startDate, actualEndDate);
        // Executes the aggregation query and maps the result to the DTO
        AggregationResults<SalesAndTrafficByDateDto> result = mongoTemplate.aggregate(aggregation, "testReports", SalesAndTrafficByDateDto.class);
        return result.getUniqueMappedResult();
    }

    // Caches the result of fetching sales and traffic data by ASIN list
    @Override
    @Cacheable(value = "getSalesAndTrafficByAsin", key = "#asinList != null ? #asinList.stream().sorted().toList().toString() : 'all'")
    public SalesAndTrafficByAsinDto getSalesAndTrafficByAsin(List<String> asinList) {
        Criteria criteria = new Criteria();
        // If ASIN list is provided, applies the filter criteria
        if (asinList != null && !asinList.isEmpty()) {
            criteria.and("salesAndTrafficByAsin.parentAsin").in(asinList);
        }
        // Builds the aggregation query for ASIN filter
        Aggregation aggregation = buildAggregationByAsin("salesAndTrafficByAsin", criteria);
        // Executes the aggregation query and maps the result to the DTO
        AggregationResults<SalesAndTrafficByAsinDto> result = mongoTemplate.aggregate(aggregation, "testReports", SalesAndTrafficByAsinDto.class);
        return result.getUniqueMappedResult();
    }

    // Caches the result of fetching overall sales and traffic data by date
    @Override
    @Cacheable("getOverallSalesAndTrafficByDate")
    public SalesAndTrafficByDateDto getOverallSalesAndTrafficByDate() {
        // Builds the aggregation query without specific date range
        Aggregation aggregation = buildAggregationByDateRange("salesAndTrafficByDate", null, null);
        // Executes the aggregation query and maps the result to the DTO
        AggregationResults<SalesAndTrafficByDateDto> result = mongoTemplate.aggregate(aggregation, "testReports", SalesAndTrafficByDateDto.class);
        return result.getUniqueMappedResult();
    }

    // Helper method to build aggregation query by date range
    private Aggregation buildAggregationByDateRange(String field, String startDate, String endDate) {
        List<AggregationOperation> operations = new ArrayList<>();
        operations.add(Aggregation.unwind(field));

        if (startDate != null && endDate != null) {
            operations.add(Aggregation.match(Criteria.where(field + ".date").gte(startDate).lte(endDate)));
        }
        // Groups data by summing and averaging various metrics related to sales and traffic
        operations.add(Aggregation.group()
                .sum("salesAndTrafficByDate.salesByDate.orderedProductSales.amount").as("totalOrderedProductSales")
                .sum("salesAndTrafficByDate.salesByDate.orderedProductSalesB2B.amount").as("totalOrderedProductSalesB2B")
                .sum("salesAndTrafficByDate.salesByDate.unitsOrdered").as("totalUnitsOrdered")
                .sum("salesAndTrafficByDate.salesByDate.unitsOrderedB2B").as("totalUnitsOrderedB2B")
                .sum("salesAndTrafficByDate.salesByDate.totalOrderItems").as("totalOrderItems")
                .sum("salesAndTrafficByDate.salesByDate.totalOrderItemsB2B").as("totalOrderItemsB2B")
                .avg("salesAndTrafficByDate.salesByDate.averageSalesPerOrderItem.amount").as("totalAverageSalesPerOrderItem")
                .avg("salesAndTrafficByDate.salesByDate.averageSalesPerOrderItemB2B.amount").as("totalAverageSalesPerOrderItemB2B")
                .avg("salesAndTrafficByDate.salesByDate.averageUnitsPerOrderItem").as("totalAverageUnitsPerOrderItem")
                .avg("salesAndTrafficByDate.salesByDate.averageUnitsPerOrderItemB2B").as("totalAverageUnitsPerOrderItemB2B")
                .avg("salesAndTrafficByDate.salesByDate.averageSellingPrice.amount").as("totalAverageSellingPrice")
                .avg("salesAndTrafficByDate.salesByDate.averageSellingPriceB2B.amount").as("totalAverageSellingPriceB2B")
                .sum("salesAndTrafficByDate.salesByDate.unitsRefunded").as("totalUnitsRefunded")
                .avg("salesAndTrafficByDate.salesByDate.refundRate").as("totalRefundRate")
                .sum("salesAndTrafficByDate.salesByDate.claimsGranted").as("totalClaimsGranted")
                .sum("salesAndTrafficByDate.salesByDate.claimsAmount.amount").as("totalClaimsAmount")
                .sum("salesAndTrafficByDate.salesByDate.shippedProductSales.amount").as("totalShippedProductSales")
                .sum("salesAndTrafficByDate.salesByDate.unitsShipped").as("totalUnitsShipped")
                .sum("salesAndTrafficByDate.salesByDate.ordersShipped").as("totalOrdersShipped")
                .sum("salesAndTrafficByDate.trafficByDate.browserPageViews").as("totalBrowserPageViews")
                .sum("salesAndTrafficByDate.trafficByDate.browserPageViewsB2B").as("totalBrowserPageViewsB2B")
                .sum("salesAndTrafficByDate.trafficByDate.mobileAppPageViews").as("totalMobileAppPageViews")
                .sum("salesAndTrafficByDate.trafficByDate.mobileAppPageViewsB2B").as("totalMobileAppPageViewsB2B")
                .sum("salesAndTrafficByDate.trafficByDate.pageViews").as("totalPageViews")
                .sum("salesAndTrafficByDate.trafficByDate.pageViewsB2B").as("totalPageViewsB2B")
                .sum("salesAndTrafficByDate.trafficByDate.browserSessions").as("totalBrowserSessions")
                .sum("salesAndTrafficByDate.trafficByDate.browserSessionsB2B").as("totalBrowserSessionsB2B")
                .sum("salesAndTrafficByDate.trafficByDate.mobileAppSessions").as("totalMobileAppSessions")
                .sum("salesAndTrafficByDate.trafficByDate.mobileAppSessionsB2B").as("totalMobileAppSessionsB2B")
                .sum("salesAndTrafficByDate.trafficByDate.sessions").as("totalSessions")
                .sum("salesAndTrafficByDate.trafficByDate.sessionsB2B").as("totalSessionsB2B")
                .avg("salesAndTrafficByDate.trafficByDate.buyBoxPercentage").as("totalBuyBoxPercentage")
                .avg("salesAndTrafficByDate.trafficByDate.buyBoxPercentageB2B").as("totalBuyBoxPercentageB2B")
                .avg("salesAndTrafficByDate.trafficByDate.orderItemSessionPercentage").as("totalOrderItemSessionPercentage")
                .avg("salesAndTrafficByDate.trafficByDate.orderItemSessionPercentageB2B").as("totalOrderItemSessionPercentageB2B")
                .avg("salesAndTrafficByDate.trafficByDate.unitSessionPercentage").as("totalUnitSessionPercentage")
                .avg("salesAndTrafficByDate.trafficByDate.unitSessionPercentageB2B").as("totalUnitSessionPercentageB2B")
                .avg("salesAndTrafficByDate.trafficByDate.averageOfferCount").as("totalAverageOfferCount")
                .avg("salesAndTrafficByDate.trafficByDate.averageParentItems").as("totalAverageParentItems")
                .sum("salesAndTrafficByDate.trafficByDate.feedbackReceived").as("totalFeedbackReceived")
                .sum("salesAndTrafficByDate.trafficByDate.negativeFeedbackReceived").as("totalNegativeFeedbackReceived")
                .avg("salesAndTrafficByDate.trafficByDate.receivedNegativeFeedbackRate").as("totalReceivedNegativeFeedbackRate")
        );

        return Aggregation.newAggregation(operations);
    }

    // Helper method to build aggregation query for ASIN filter
    private Aggregation buildAggregationByAsin(String field, Criteria criteria) {
        List<AggregationOperation> operations = new ArrayList<>();
        operations.add(Aggregation.unwind(field));

        if (criteria != null) {
            operations.add(Aggregation.match(criteria));
        }
        // Groups data by summing and averaging various metrics related to sales and traffic for ASIN
        operations.add(Aggregation.group()
                .sum("salesAndTrafficByAsin.salesByAsin.unitsOrdered").as("totalUnitsOrdered")
                .sum("salesAndTrafficByAsin.salesByAsin.unitsOrderedB2B").as("totalUnitsOrderedB2B")
                .sum("salesAndTrafficByAsin.salesByAsin.orderedProductSales.amount").as("totalOrderedProductSales")
                .sum("salesAndTrafficByAsin.salesByAsin.orderedProductSalesB2B.amount").as("totalOrderedProductSalesB2B")
                .sum("salesAndTrafficByAsin.salesByAsin.totalOrderItems").as("totalOrderItems")
                .sum("salesAndTrafficByAsin.salesByAsin.totalOrderItemsB2B").as("totalOrderItemsB2B")
                .sum("salesAndTrafficByAsin.trafficByAsin.browserSessions").as("totalBrowserSessions")
                .sum("salesAndTrafficByAsin.trafficByAsin.browserSessionsB2B").as("totalBrowserSessionsB2B")
                .sum("salesAndTrafficByAsin.trafficByAsin.mobileAppSessions").as("totalMobileAppSessions")
                .sum("salesAndTrafficByAsin.trafficByAsin.mobileAppSessionsB2B").as("totalMobileAppSessionsB2B")
                .sum("salesAndTrafficByAsin.trafficByAsin.sessions").as("totalSessions")
                .sum("salesAndTrafficByAsin.trafficByAsin.sessionsB2B").as("totalSessionsB2B")
                .avg("salesAndTrafficByAsin.trafficByAsin.browserSessionPercentage").as("totalBrowserSessionPercentage")
                .avg("salesAndTrafficByAsin.trafficByAsin.browserSessionPercentageB2B").as("totalBrowserSessionPercentageB2B")
                .avg("salesAndTrafficByAsin.trafficByAsin.mobileAppSessionPercentage").as("totalMobileAppSessionPercentage")
                .avg("salesAndTrafficByAsin.trafficByAsin.mobileAppSessionPercentageB2B").as("totalMobileAppSessionPercentageB2B")
                .avg("salesAndTrafficByAsin.trafficByAsin.sessionPercentage").as("totalSessionPercentage")
                .avg("salesAndTrafficByAsin.trafficByAsin.sessionPercentageB2B").as("totalSessionPercentageB2B")
                .sum("salesAndTrafficByAsin.trafficByAsin.browserPageViews").as("totalBrowserPageViews")
                .sum("salesAndTrafficByAsin.trafficByAsin.browserPageViewsB2B").as("totalBrowserPageViewsB2B")
                .sum("salesAndTrafficByAsin.trafficByAsin.mobileAppPageViews").as("totalMobileAppPageViews")
                .sum("salesAndTrafficByAsin.trafficByAsin.mobileAppPageViewsB2B").as("totalMobileAppPageViewsB2B")
                .sum("salesAndTrafficByAsin.trafficByAsin.pageViews").as("totalPageViews")
                .sum("salesAndTrafficByAsin.trafficByAsin.pageViewsB2B").as("totalPageViewsB2B")
                .avg("salesAndTrafficByAsin.trafficByAsin.browserPageViewsPercentage").as("totalBrowserPageViewsPercentage")
                .avg("salesAndTrafficByAsin.trafficByAsin.browserPageViewsPercentageB2B").as("totalBrowserPageViewsPercentageB2B")
                .avg("salesAndTrafficByAsin.trafficByAsin.mobileAppPageViewsPercentage").as("totalMobileAppPageViewsPercentage")
                .avg("salesAndTrafficByAsin.trafficByAsin.mobileAppPageViewsPercentageB2B").as("totalMobileAppPageViewsPercentageB2B")
                .avg("salesAndTrafficByAsin.trafficByAsin.pageViewsPercentage").as("totalPageViewsPercentage")
                .avg("salesAndTrafficByAsin.trafficByAsin.pageViewsPercentageB2B").as("totalPageViewsPercentageB2B")
                .avg("salesAndTrafficByAsin.trafficByAsin.buyBoxPercentage").as("averageBuyBoxPercentage")
                .avg("salesAndTrafficByAsin.trafficByAsin.buyBoxPercentageB2B").as("averageBuyBoxPercentageB2B")
                .avg("salesAndTrafficByAsin.trafficByAsin.unitSessionPercentage").as("averageUnitSessionPercentage")
                .avg("salesAndTrafficByAsin.trafficByAsin.unitSessionPercentageB2B").as("averageUnitSessionPercentageB2B")
        );

        return Aggregation.newAggregation(operations);
    }
}