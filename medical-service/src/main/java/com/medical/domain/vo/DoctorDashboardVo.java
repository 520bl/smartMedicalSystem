package com.medical.domain.vo;

import lombok.Data;

@Data
public class DoctorDashboardVo {
    /** 今日待诊（已支付、待就诊） */
    private Long todayWaiting;
    /** 今日已接诊 */
    private Long todayCompleted;
    /** 待写病历（草稿 + 已接诊未建档） */
    private Long pendingRecords;
}
