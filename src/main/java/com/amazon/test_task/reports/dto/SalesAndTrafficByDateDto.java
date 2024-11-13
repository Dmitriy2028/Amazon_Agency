package com.amazon.test_task.reports.dto;

import lombok.Data;

@Data
public class SalesAndTrafficByDateDto {
    private double totalOrderedProductSales;
    private double totalOrderedProductSalesB2B;
    private int totalUnitsOrdered;
    private int totalUnitsOrderedB2B;
    private int totalOrderItems;
    private int totalOrderItemsB2B;
    private double totalAverageSalesPerOrderItem;
    private double totalAverageSalesPerOrderItemB2B;
    private double totalAverageUnitsPerOrderItem;
    private double totalAverageUnitsPerOrderItemB2B;
    private double totalAverageSellingPrice;
    private double totalAverageSellingPriceB2B;
    private int totalUnitsRefunded;
    private double totalRefundRate;
    private int totalClaimsGranted;
    private double totalClaimsAmount;
    private double totalShippedProductSales;
    private int totalUnitsShipped;
    private int totalOrdersShipped;
    private int totalBrowserPageViews;
    private int totalBrowserPageViewsB2B;
    private int totalMobileAppPageViews;
    private int totalMobileAppPageViewsB2B;
    private int totalPageViews;
    private int totalPageViewsB2B;
    private int totalBrowserSessions;
    private int totalBrowserSessionsB2B;
    private int totalMobileAppSessions;
    private int totalMobileAppSessionsB2B;
    private int totalSessions;
    private int totalSessionsB2B;
    private double totalBuyBoxPercentage;
    private double totalBuyBoxPercentageB2B;
    private double totalOrderItemSessionPercentage;
    private double totalOrderItemSessionPercentageB2B;
    private double totalUnitSessionPercentage;
    private double totalUnitSessionPercentageB2B;
    private int totalAverageOfferCount;
    private int totalAverageParentItems;
    private int totalFeedbackReceived;
    private int totalNegativeFeedbackReceived;
    private double totalReceivedNegativeFeedbackRate;
}
