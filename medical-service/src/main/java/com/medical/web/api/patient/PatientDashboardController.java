package com.medical.web.api.patient;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.medical.common.exception.BusinessWarningException;
import com.medical.common.response.ResultVo;
import com.medical.domain.entity.Patient;
import com.medical.domain.entity.SysUser;
import com.medical.domain.vo.PatientDashboardVo;
import com.medical.mapper.PatientMapper;
import com.medical.mapper.SysUserMapper;
import com.medical.service.PatientDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "患者端-工作台")
@RestController
@RequestMapping("/api/patient/dashboard")
@RequiredArgsConstructor
public class PatientDashboardController {

    private final PatientDashboardService patientDashboardService;
    private final PatientMapper patientMapper;
    private final SysUserMapper sysUserMapper;

    @Operation(summary = "首页统计")
    @GetMapping("/stats")
    public ResultVo<PatientDashboardVo> stats() {
        Long patientId = getCurrentPatientId();
        return ResultVo.ok(patientDashboardService.getStats(patientId));
    }

    private Long getCurrentPatientId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new BusinessWarningException("请先登录");
        }
        SysUser user = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, auth.getName()));
        if (user == null) {
            throw new BusinessWarningException("用户不存在");
        }
        Patient patient = patientMapper.selectOne(
                new LambdaQueryWrapper<Patient>().eq(Patient::getUserId, user.getUserId()));
        if (patient == null) {
            throw new BusinessWarningException("患者档案不存在");
        }
        return patient.getPatientId();
    }
}
