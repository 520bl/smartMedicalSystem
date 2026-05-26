package com.medical.web.api.admin;

import com.medical.common.response.ResultVo;
import com.medical.domain.vo.AdminDashboardVo;
import com.medical.service.AdminDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "管理端-工作台")
@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    @Operation(summary = "工作台统计")
    @GetMapping("/stats")
    public ResultVo<AdminDashboardVo> stats() {
        return ResultVo.ok(adminDashboardService.getStats());
    }
}
