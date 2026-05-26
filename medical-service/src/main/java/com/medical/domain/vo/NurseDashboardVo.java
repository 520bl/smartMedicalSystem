package com.medical.domain.vo;

import lombok.Data;

@Data
public class NurseDashboardVo {
    /** 待发药处方数 */
    private Long pendingDispense;
    /** 今日已发药数 */
    private Long todayDispensed;
}
