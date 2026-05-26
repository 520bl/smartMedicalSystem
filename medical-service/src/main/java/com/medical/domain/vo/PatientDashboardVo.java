package com.medical.domain.vo;

import lombok.Data;

@Data
public class PatientDashboardVo {
    /** 待就诊预约（含今日及以后） */
    private Long pendingAppointments;
    /** 待支付（未付挂号费 + 未付处方费） */
    private Long unpaidCount;
    /** 近 30 天已归档病历数 */
    private Long recentMedicalRecords;
}
