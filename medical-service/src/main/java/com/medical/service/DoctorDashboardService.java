package com.medical.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.medical.domain.entity.Appointment;
import com.medical.domain.entity.MedicalRecord;
import com.medical.domain.vo.DoctorDashboardVo;
import com.medical.mapper.AppointmentMapper;
import com.medical.mapper.MedicalRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorDashboardService {

    private final AppointmentMapper appointmentMapper;
    private final MedicalRecordMapper medicalRecordMapper;

    public DoctorDashboardVo getStats(Long doctorId) {
        LocalDate today = LocalDate.now();
        DoctorDashboardVo vo = new DoctorDashboardVo();

        vo.setTodayWaiting(appointmentMapper.selectCount(
                new LambdaQueryWrapper<Appointment>()
                        .eq(Appointment::getDoctorId, doctorId)
                        .eq(Appointment::getAppointmentDate, today)
                        .eq(Appointment::getStatus, 1)
                        .eq(Appointment::getPaid, 1)));

        vo.setTodayCompleted(appointmentMapper.selectCount(
                new LambdaQueryWrapper<Appointment>()
                        .eq(Appointment::getDoctorId, doctorId)
                        .eq(Appointment::getAppointmentDate, today)
                        .eq(Appointment::getStatus, 2)));

        long draftRecords = medicalRecordMapper.selectCount(
                new LambdaQueryWrapper<MedicalRecord>()
                        .eq(MedicalRecord::getDoctorId, doctorId)
                        .eq(MedicalRecord::getVisitDate, today)
                        .eq(MedicalRecord::getStatus, 1));

        List<Appointment> inConsult = appointmentMapper.selectList(
                new LambdaQueryWrapper<Appointment>()
                        .eq(Appointment::getDoctorId, doctorId)
                        .eq(Appointment::getAppointmentDate, today)
                        .eq(Appointment::getStatus, 2));

        long withoutRecord = inConsult.stream()
                .filter(a -> medicalRecordMapper.selectCount(
                        new LambdaQueryWrapper<MedicalRecord>()
                                .eq(MedicalRecord::getPatientId, a.getPatientId())
                                .eq(MedicalRecord::getDoctorId, doctorId)
                                .eq(MedicalRecord::getVisitDate, today)) == 0)
                .count();

        vo.setPendingRecords(draftRecords + withoutRecord);
        return vo;
    }
}
