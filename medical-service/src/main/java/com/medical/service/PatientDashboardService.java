package com.medical.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.medical.domain.entity.Appointment;
import com.medical.domain.entity.MedicalRecord;
import com.medical.domain.entity.Prescription;
import com.medical.domain.vo.PatientDashboardVo;
import com.medical.mapper.AppointmentMapper;
import com.medical.mapper.MedicalRecordMapper;
import com.medical.mapper.PrescriptionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientDashboardService {

    private final AppointmentMapper appointmentMapper;
    private final PrescriptionMapper prescriptionMapper;
    private final MedicalRecordMapper medicalRecordMapper;
    private final PaymentService paymentService;

    public PatientDashboardVo getStats(Long patientId) {
        LocalDate today = LocalDate.now();
        PatientDashboardVo vo = new PatientDashboardVo();

        vo.setPendingAppointments(appointmentMapper.selectCount(
                new LambdaQueryWrapper<Appointment>()
                        .eq(Appointment::getPatientId, patientId)
                        .eq(Appointment::getStatus, 1)
                        .ge(Appointment::getAppointmentDate, today)));

        long unpaidAppointments = appointmentMapper.selectCount(
                new LambdaQueryWrapper<Appointment>()
                        .eq(Appointment::getPatientId, patientId)
                        .eq(Appointment::getStatus, 1)
                        .eq(Appointment::getPaid, 0)
                        .ge(Appointment::getAppointmentDate, today));

        List<Prescription> pendingPrescriptions = prescriptionMapper.selectList(
                new LambdaQueryWrapper<Prescription>()
                        .eq(Prescription::getPatientId, patientId)
                        .eq(Prescription::getStatus, 1));

        long unpaidPrescriptions = pendingPrescriptions.stream()
                .filter(p -> !paymentService.isBizPaid("PRESCRIPTION", p.getPrescriptionId()))
                .count();

        vo.setUnpaidCount(unpaidAppointments + unpaidPrescriptions);

        LocalDate from = today.minusDays(30);
        vo.setRecentMedicalRecords(medicalRecordMapper.selectCount(
                new LambdaQueryWrapper<MedicalRecord>()
                        .eq(MedicalRecord::getPatientId, patientId)
                        .ge(MedicalRecord::getVisitDate, from)
                        .and(w -> w.isNull(MedicalRecord::getStatus).or().ne(MedicalRecord::getStatus, 1))));

        return vo;
    }
}
