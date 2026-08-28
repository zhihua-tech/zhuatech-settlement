/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.settlement.controller;

import cn.zhuatech.settlement.common.ApiResponse;
import cn.zhuatech.settlement.model.ReconciliationBatch;
import cn.zhuatech.settlement.service.ReconciliationService;
import cn.zhuatech.settlement.service.SettlementTransactionService;
import cn.zhuatech.settlement.model.SettlementTransaction;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/settlement/reconciliation")
public class ReconciliationController {
    private final ReconciliationService service;private final SettlementTransactionService transactionService;
    public ReconciliationController(ReconciliationService service,SettlementTransactionService transactionService){this.service=service;this.transactionService=transactionService;}
    @GetMapping ApiResponse<List<ReconciliationBatch>> list(){return ApiResponse.ok(service.list());}
    @PostMapping ApiResponse<ReconciliationBatch> create(@Valid @RequestBody ReconciliationService.CreateRequest request){return ApiResponse.ok(service.create(request));}
    @PostMapping("/{id}/match") ApiResponse<ReconciliationService.MatchResult> reconcile(@PathVariable Long id){return ApiResponse.ok(service.reconcile(id));}
    @GetMapping("/{id}/metrics") ApiResponse<ReconciliationService.Metrics> metrics(@PathVariable Long id){return ApiResponse.ok(service.metrics(id));}
    @GetMapping("/{id}/transactions") ApiResponse<List<SettlementTransaction>> transactions(@PathVariable Long id){return ApiResponse.ok(transactionService.list(id));}
    @PostMapping("/{id}/transactions") ApiResponse<List<SettlementTransaction>> importTransactions(@PathVariable Long id,
        @Valid @RequestBody SettlementTransactionService.ImportRequest request){return ApiResponse.ok(transactionService.importLines(id,request));}
    @GetMapping("/{id}/transaction-match") ApiResponse<SettlementTransactionService.TransactionMatchResult> transactionMatch(@PathVariable Long id){return ApiResponse.ok(transactionService.match(id));}
    @PostMapping("/{id}/confirm") ApiResponse<ReconciliationBatch> confirm(@PathVariable Long id){return ApiResponse.ok(service.confirm(id));}
    @PostMapping("/{id}/settle") ApiResponse<ReconciliationBatch> settle(@PathVariable Long id,@RequestParam String paymentReference){return ApiResponse.ok(service.settle(id,paymentReference));}
    @GetMapping("/dashboard") ApiResponse<ReconciliationService.Dashboard> dashboard(){return ApiResponse.ok(service.dashboard());}
}
