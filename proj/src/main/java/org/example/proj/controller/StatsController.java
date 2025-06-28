package org.example.proj.controller;

import org.example.proj.enums.ChartType;
import org.example.proj.service.StatsService;
import org.example.proj.service.ExportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final StatsService statsService;
    private final ExportService exportService;

    public StatsController(StatsService statsService, ExportService exportService) {
        this.statsService = statsService;
        this.exportService = exportService;
    }

    @GetMapping("/emotions")
    public ResponseEntity<?> getEmotionStats(
            @RequestParam(defaultValue = "week") String period,
            Authentication authentication) {
        return ResponseEntity.ok(statsService.getEmotionStats(period, authentication));
    }

    @GetMapping("/trends")
    public ResponseEntity<?> getTrends(
            @RequestParam(defaultValue = "week") String period,
            Authentication authentication) {
        return ResponseEntity.ok(statsService.getTrendStats(period, authentication));
    }

    @GetMapping("/export")
    public ResponseEntity<?> exportStatistics(
            @RequestParam(defaultValue = "pdf") String format,
            @RequestParam(defaultValue = "week") String period,
            Authentication authentication) {
        return exportService.export(format, period, authentication);
    }

    @GetMapping("/chart")
    public ResponseEntity<?> getChartData(
            @RequestParam(defaultValue = "week") String period,
            @RequestParam ChartType type,
            Authentication authentication
    ) {
        return switch (type) {
            case PIE -> ResponseEntity.ok(statsService.getEmotionStats(period, authentication));
            case LINE -> ResponseEntity.ok(statsService.getTrendStats(period, authentication));
        };
    }

}
