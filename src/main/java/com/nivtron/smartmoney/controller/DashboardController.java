package com.nivtron.smartmoney.controller;

import com.nivtron.smartmoney.api.DashboardApi;
import com.nivtron.smartmoney.dto.DashboardResponse;
import com.nivtron.smartmoney.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DashboardController implements DashboardApi {

    private final DashboardService dashboardService;

    private String getCurrentUserEmail() {
        return (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

    @Override
    public ResponseEntity<DashboardResponse> getDashboard() {
        return ResponseEntity.ok(dashboardService.getDashboard(getCurrentUserEmail()));
    }
}