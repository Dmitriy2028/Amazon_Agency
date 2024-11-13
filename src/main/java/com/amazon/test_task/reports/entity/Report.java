package com.amazon.test_task.reports.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "testReports")
public class Report {

    @Id
    private String id;
    private ReportSpecification reportSpecification;
    private List<SalesAndTrafficByDate> salesAndTrafficByDate;
    private List<SalesAndTrafficByAsin> salesAndTrafficByAsin;

    @Data
    public static class ReportSpecification {
        private String reportType;
        private ReportOptions reportOptions;
        private String dataStartTime;
        private String dataEndTime;
        private List<String> marketplaceIds;
    }

    @Data
    public static class ReportOptions {
        private String dateGranularity;
        private String asinGranularity;
    }

    @Data
    public static class SalesAndTrafficByDate {
        private String date;
        private SalesByDate salesByDate;
        private TrafficByDate trafficByDate;
    }

    @Data
    public static class SalesByDate {
        private Money orderedProductSales;
        private Money orderedProductSalesB2B;
        private int unitsOrdered;
        private int unitsOrderedB2B;
        private int totalOrderItems;
        private int totalOrderItemsB2B;
        private Money averageSalesPerOrderItem;
        private Money averageSalesPerOrderItemB2B;
        private double averageUnitsPerOrderItem;
        private double averageUnitsPerOrderItemB2B;
        private Money averageSellingPrice;
        private Money averageSellingPriceB2B;
        private int unitsRefunded;
        private double refundRate;
        private int claimsGranted;
        private Money claimsAmount;
        private Money shippedProductSales;
        private int unitsShipped;
        private int ordersShipped;
    }

    @Data
    public static class TrafficByDate {
        private int browserPageViews;
        private int browserPageViewsB2B;
        private int mobileAppPageViews;
        private int mobileAppPageViewsB2B;
        private int pageViews;
        private int pageViewsB2B;
        private int browserSessions;
        private int browserSessionsB2B;
        private int mobileAppSessions;
        private int mobileAppSessionsB2B;
        private int sessions;
        private int sessionsB2B;
        private double buyBoxPercentage;
        private double buyBoxPercentageB2B;
        private double orderItemSessionPercentage;
        private double orderItemSessionPercentageB2B;
        private double unitSessionPercentage;
        private double unitSessionPercentageB2B;
        private int averageOfferCount;
        private int averageParentItems;
        private int feedbackReceived;
        private int negativeFeedbackReceived;
        private double receivedNegativeFeedbackRate;
    }

    @Data
    public static class SalesAndTrafficByAsin {
        private String parentAsin;
        private SalesByAsin salesByAsin;
        private TrafficByAsin trafficByAsin;
    }

    @Data
    public static class SalesByAsin {
        private int unitsOrdered;
        private int unitsOrderedB2B;
        private Money orderedProductSales;
        private Money orderedProductSalesB2B;
        private int totalOrderItems;
        private int totalOrderItemsB2B;
    }

    @Data
    public static class TrafficByAsin {
        private int browserSessions;
        private int browserSessionsB2B;
        private int mobileAppSessions;
        private int mobileAppSessionsB2B;
        private int sessions;
        private int sessionsB2B;
        private double browserSessionPercentage;
        private double browserSessionPercentageB2B;
        private double mobileAppSessionPercentage;
        private double mobileAppSessionPercentageB2B;
        private double sessionPercentage;
        private double sessionPercentageB2B;
        private int browserPageViews;
        private int browserPageViewsB2B;
        private int mobileAppPageViews;
        private int mobileAppPageViewsB2B;
        private int pageViews;
        private int pageViewsB2B;
        private double browserPageViewsPercentage;
        private double browserPageViewsPercentageB2B;
        private double mobileAppPageViewsPercentage;
        private double mobileAppPageViewsPercentageB2B;
        private double pageViewsPercentage;
        private double pageViewsPercentageB2B;
        private double buyBoxPercentage;
        private double buyBoxPercentageB2B;
        private double unitSessionPercentage;
        private double unitSessionPercentageB2B;
    }

    @Data
    public static class Money {
        private double amount;
        private String currencyCode;
    }

    public void updateFrom(Report newReport) {
        this.reportSpecification = newReport.getReportSpecification();
        this.salesAndTrafficByDate = newReport.getSalesAndTrafficByDate();
        this.salesAndTrafficByAsin = newReport.getSalesAndTrafficByAsin();
    }
}