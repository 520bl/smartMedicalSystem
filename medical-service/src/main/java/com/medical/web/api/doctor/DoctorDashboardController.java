package com.medical.web.api.doctor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.medical.common.exception.ServiceException;
import com.medical.common.response.ResultVo;
import com.medical.domain.entity.Doctor;
import com.medical.domain.entity.SysUser;
import com.medical.domain.vo.DoctorDashboardVo;
import com.medical.mapper.DoctorMapper;
import com.medical.mapper.SysUserMapper;
import com.medical.service.DoctorDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "医生端-工作台")
@RestController
@RequestMapping("/api/doctor/dashboard")
@RequiredArgsConstructor
public class DoctorDashboardController {

    private final DoctorDashboardService doctorDashboardService;
    private final DoctorMapper doctorMapper;
    private final SysUserMapper sysUserMapper;

    @Operation(summary = "工作台统计")
    @GetMapping("/stats")
    public ResultVo<DoctorDashboardVo> stats() {
        Long doctorId = getCurrentDoctorId();
        return ResultVo.ok(doctorDashboardService.getStats(doctorId));
    }

    private Long getCurrentDoctorId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ServiceException("未登录");
        }
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails userDetails)) {
            throw new ServiceException("未登录");
        }
        SysUser user = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, userDetails.getUsername()));
        if (user == null) {
            throw new ServiceException("用户不存在");
        }
        Doctor doctor = doctorMapper.selectOne(
                new LambdaQueryWrapper<Doctor>().eq(Doctor::getUserId, user.getUserId()));
        if (doctor == null) {
            throw new ServiceException("未找到医生信息");
        }
        return doctor.getDoctorId();
    }
}
