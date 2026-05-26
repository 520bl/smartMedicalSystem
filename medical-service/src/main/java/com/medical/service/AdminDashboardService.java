package com.medical.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.medical.domain.entity.Appointment;
import com.medical.domain.entity.Prescription;
import com.medical.domain.vo.AdminDashboardVo;
import com.medical.mapper.AppointmentMapper;
import com.medical.mapper.PrescriptionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final AppointmentMapper appointmentMapper;
    private final PrescriptionMapper prescriptionMapper;
    private final PaymentService paymentService;

    public AdminDashboardVo getStats() {
        LocalDate today = LocalDate.now();
        AdminDashboardVo vo = new AdminDashboardVo();

        vo.setTodayAppointments(appointmentMapper.selectCount(
                new LambdaQueryWrapper<Appointment>()
                        .eq(Appointment::getAppointmentDate, today)
                        .ne(Appointment::getStatus, 3)));

        vo.setTodayVisits(appointmentMapper.selectCount(
                new LambdaQueryWrapper<Appointment>()
                        .eq(Appointment::getAppointmentDate, today)
                        .eq(Appointment::getStatus, 2)));

        vo.setPendingDispense(prescriptionMapper.selectCount(
                new LambdaQueryWrapper<Prescription>()
                        .eq(Prescription::getStatus, 1)));

        vo.setTodayIncome(paymentService.sumTodayPaidAmount());
        return vo;
    }
}
