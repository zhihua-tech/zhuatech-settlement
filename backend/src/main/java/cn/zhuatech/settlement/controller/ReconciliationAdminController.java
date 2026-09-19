/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.settlement.controller;

import cn.zhuatech.settlement.common.ApiResponse;
import cn.zhuatech.settlement.service.ReconciliationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@RestController
@RequestMapping("/api/admin/settlement/reconciliation")
public class ReconciliationAdminController {
    private final ReconciliationService service;
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public ReconciliationAdminController(ReconciliationService service){this.service=service;}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/{id}/resolve") ApiResponse<ReconciliationService.MatchResult> resolve(@PathVariable Long id,
        @Valid @RequestBody ReconciliationService.ResolveRequest request){return ApiResponse.ok(service.resolve(id,request));}
}
