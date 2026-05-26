package com.medical.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AdminDashboardVo {
    private Long todayAppointments;
    private Long todayVisits;
    private Long pendingDispense;
    private BigDecimal todayIncome;
}
