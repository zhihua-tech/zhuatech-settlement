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

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@RestController
@RequestMapping("/api/settlement/reconciliation")
public class ReconciliationController {
    private final ReconciliationService service;private final SettlementTransactionService transactionService;
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public ReconciliationController(ReconciliationService service,SettlementTransactionService transactionService){this.service=service;this.transactionService=transactionService;}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @GetMapping ApiResponse<List<ReconciliationBatch>> list(){return ApiResponse.ok(service.list());}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping ApiResponse<ReconciliationBatch> create(@Valid @RequestBody ReconciliationService.CreateRequest request){return ApiResponse.ok(service.create(request));}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/{id}/match") ApiResponse<ReconciliationService.MatchResult> reconcile(@PathVariable Long id){return ApiResponse.ok(service.reconcile(id));}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @GetMapping("/{id}/metrics") ApiResponse<ReconciliationService.Metrics> metrics(@PathVariable Long id){return ApiResponse.ok(service.metrics(id));}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @GetMapping("/{id}/transactions") ApiResponse<List<SettlementTransaction>> transactions(@PathVariable Long id){return ApiResponse.ok(transactionService.list(id));}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/{id}/transactions") ApiResponse<List<SettlementTransaction>> importTransactions(@PathVariable Long id,
        @Valid @RequestBody SettlementTransactionService.ImportRequest request){return ApiResponse.ok(transactionService.importLines(id,request));}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @GetMapping("/{id}/transaction-match") ApiResponse<SettlementTransactionService.TransactionMatchResult> transactionMatch(@PathVariable Long id){return ApiResponse.ok(transactionService.match(id));}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/{id}/confirm") ApiResponse<ReconciliationBatch> confirm(@PathVariable Long id){return ApiResponse.ok(service.confirm(id));}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/{id}/settle") ApiResponse<ReconciliationBatch> settle(@PathVariable Long id,@RequestParam String paymentReference){return ApiResponse.ok(service.settle(id,paymentReference));}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @GetMapping("/dashboard") ApiResponse<ReconciliationService.Dashboard> dashboard(){return ApiResponse.ok(service.dashboard());}
}
