package com.amazon.test_task.reports.dto;

import lombok.Data;

@Data
public class SalesAndTrafficByAsinDto {
    private int totalUnitsOrdered;
    private int totalUnitsOrderedB2B;
    private double totalOrderedProductSales;
    private double totalOrderedProductSalesB2B;
    private int totalOrderItems;
    private int totalOrderItemsB2B;
    private int totalBrowserSessions;
    private int totalBrowserSessionsB2B;
    private int totalMobileAppSessions;
    private int totalMobileAppSessionsB2B;
    private int totalSessions;
    private int totalSessionsB2B;
    private double totalBrowserSessionPercentage;
    private double totalBrowserSessionPercentageB2B;
    private double totalMobileAppSessionPercentage;
    private double totalMobileAppSessionPercentageB2B;
    private double totalSessionPercentage;
    private double totalSessionPercentageB2B;
    private int totalBrowserPageViews;
    private int totalBrowserPageViewsB2B;
    private int totalMobileAppPageViews;
    private int totalMobileAppPageViewsB2B;
    private int totalPageViews;
    private int totalPageViewsB2B;
    private double totalBrowserPageViewsPercentage;
    private double totalBrowserPageViewsPercentageB2B;
    private double totalMobileAppPageViewsPercentage;
    private double totalMobileAppPageViewsPercentageB2B;
    private double totalPageViewsPercentage;
    private double totalPageViewsPercentageB2B;
    private double averageBuyBoxPercentage;
    private double averageBuyBoxPercentageB2B;
    private double averageUnitSessionPercentage;
    private double averageUnitSessionPercentageB2B;
}
