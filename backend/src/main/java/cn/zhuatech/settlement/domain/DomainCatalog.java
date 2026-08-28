/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.settlement.domain;
import org.springframework.stereotype.Component;
import java.util.*;
@Component
public class DomainCatalog {
    private final Map<String, WorkflowAction> actions = new LinkedHashMap<>();
    public DomainCatalog() {
        actions.put("MATCH", new WorkflowAction("MATCH", "执行批次对账", List.of("草稿"), "对账中", "OPERATOR"));
        actions.put("CONFIRM", new WorkflowAction("CONFIRM", "确认结算单", List.of("对账中"), "待付款", "ADMIN"));
        actions.put("PAY", new WorkflowAction("PAY", "确认付款核销", List.of("待付款"), "已结算", "ADMIN"));
    }
    public String systemName() { return "知华科技企业对账与结算管理系统"; }
    public String scene() { return "结算主体、合同规则、交易归集、自动对账、差异、账单、发票、付款和审计"; }
    public String initialStatus() { return "草稿"; }
    public String partyLabel() { return "结算主体/账单"; }
    public String amountLabel() { return "结算金额"; }
    public String quantityLabel() { return "交易笔数"; }
    public String dueLabel() { return "付款到期日"; }
    public List<ModuleDefinition> modules() { return List.of(
            new ModuleDefinition("COUNTERPARTY", "结算主体", "维护客户、供应商、平台、账户和结算周期"),
            new ModuleDefinition("CONTRACT_RULE", "结算规则", "配置费率、税率、账期、舍入和容差规则"),
            new ModuleDefinition("TRANSACTION", "交易归集", "导入订单、退款、费用、调整和资金流水"),
            new ModuleDefinition("RECONCILIATION", "自动对账", "按业务键、金额、日期和组合规则执行匹配"),
            new ModuleDefinition("EXCEPTION", "差异处理", "分派长短款、重复、缺失和跨期差异并复核"),
            new ModuleDefinition("STATEMENT", "结算单", "生成明细、汇总、调整项和版本化结算单"),
            new ModuleDefinition("CONFIRMATION", "对方确认", "支持线上确认、异议、证据和重开流程"),
            new ModuleDefinition("INVOICE_PAYMENT", "开票付款", "连接发票、付款计划、银行回单和核销结果"),
            new ModuleDefinition("AUDIT", "结算审计", "保留导入批次、规则版本、差异和审批证据")
        ); }
    public Map<String, WorkflowAction> actions() { return Collections.unmodifiableMap(actions); }
    public record ModuleDefinition(String code,String name,String description) {}
    public record WorkflowAction(String code,String label,List<String> from,String to,String requiredRole) {}
}
