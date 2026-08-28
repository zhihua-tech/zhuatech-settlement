/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.settlement.controller;

import cn.zhuatech.settlement.common.ApiResponse;
import cn.zhuatech.settlement.model.ReconciliationBatch;
import cn.zhuatech.settlement.service.ReconciliationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/settlement/reconciliation")
public class ReconciliationController {
    private final ReconciliationService service;
    public ReconciliationController(ReconciliationService service){this.service=service;}
    @GetMapping ApiResponse<List<ReconciliationBatch>> list(){return ApiResponse.ok(service.list());}
    @PostMapping ApiResponse<ReconciliationBatch> create(@Valid @RequestBody ReconciliationService.CreateRequest request){return ApiResponse.ok(service.create(request));}
    @PostMapping("/{id}/match") ApiResponse<ReconciliationService.MatchResult> reconcile(@PathVariable Long id){return ApiResponse.ok(service.reconcile(id));}
    @GetMapping("/{id}/metrics") ApiResponse<ReconciliationService.Metrics> metrics(@PathVariable Long id){return ApiResponse.ok(service.metrics(id));}
    @PostMapping("/{id}/confirm") ApiResponse<ReconciliationBatch> confirm(@PathVariable Long id){return ApiResponse.ok(service.confirm(id));}
    @PostMapping("/{id}/settle") ApiResponse<ReconciliationBatch> settle(@PathVariable Long id,@RequestParam String paymentReference){return ApiResponse.ok(service.settle(id,paymentReference));}
    @GetMapping("/dashboard") ApiResponse<ReconciliationService.Dashboard> dashboard(){return ApiResponse.ok(service.dashboard());}
}
