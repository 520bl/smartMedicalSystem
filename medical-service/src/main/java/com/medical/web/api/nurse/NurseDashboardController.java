package com.medical.web.api.nurse;

import com.medical.common.response.ResultVo;
import com.medical.domain.vo.NurseDashboardVo;
import com.medical.service.NurseDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "护士端-工作台")
@RestController
@RequestMapping("/api/nurse/dashboard")
@RequiredArgsConstructor
public class NurseDashboardController {

    private final NurseDashboardService nurseDashboardService;

    @Operation(summary = "工作台统计")
    @GetMapping("/stats")
    public ResultVo<NurseDashboardVo> stats() {
        return ResultVo.ok(nurseDashboardService.getStats());
    }
}
