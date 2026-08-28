/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.settlement.controller;
import cn.zhuatech.settlement.common.ApiResponse;
import cn.zhuatech.settlement.model.EnterpriseControl;
import cn.zhuatech.settlement.service.EnterpriseControlService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/admin/enterprise/controls")
public class EnterpriseAdminController {
    private final EnterpriseControlService service;
    public EnterpriseAdminController(EnterpriseControlService service){this.service=service;}
    @PostMapping("/{id}/review") ApiResponse<EnterpriseControl> review(@PathVariable Long id,@Valid @RequestBody EnterpriseControlService.ReviewRequest request){return ApiResponse.ok(service.review(id,request));}
    @PostMapping("/{id}/sync") ApiResponse<EnterpriseControl> sync(@PathVariable Long id,@Valid @RequestBody EnterpriseControlService.SyncRequest request){return ApiResponse.ok(service.sync(id,request));}
}
