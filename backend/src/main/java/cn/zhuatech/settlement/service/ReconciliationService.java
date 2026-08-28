/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.settlement.service;

import cn.zhuatech.settlement.model.*;
import cn.zhuatech.settlement.repository.*;
import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.math.*;
import java.util.*;

@Service
public class ReconciliationService {
    private final ReconciliationBatchRepository batches;private final AuditLogRepository audits;
    public ReconciliationService(ReconciliationBatchRepository batches,AuditLogRepository audits){
        this.batches=batches;this.audits=audits;
    }
    public List<ReconciliationBatch> list(){return batches.findAllByOrderByUpdatedAtDesc();}

    @Transactional
    public ReconciliationBatch create(CreateRequest r){
        if(r.matchedCount()>r.transactionCount())throw bad("匹配笔数不能大于交易笔数");
        if(batches.findByBatchNo(r.batchNo()).isPresent())throw conflict("对账批次号已存在");
        var item=batches.save(new ReconciliationBatch(r.batchNo(),r.organizationCode(),r.counterparty(),
            r.sourceAmount(),r.ledgerAmount(),r.transactionCount(),r.matchedCount(),r.unresolvedExceptions(),
            r.counterpartConfirmed(),r.invoiceReady(),r.bankAccountVerified()));
        audit("创建对账批次",item,r.counterparty());return item;
    }

    @Transactional
    public MatchResult reconcile(Long id){
        var item=get(id);require(item,"DRAFT","只有草稿批次允许执行匹配");
        var metrics=metrics(item);boolean clean=metrics.amountVariance().compareTo(metrics.tolerance())<=0
            && metrics.matchRate()>=99 && item.getUnresolvedExceptions()==0;
        item.reconcile(clean);audit("执行自动对账",item,clean?"自动匹配通过":"进入差异处理");
        return new MatchResult(item,metrics);
    }

    @Transactional
    public MatchResult resolve(Long id,ResolveRequest r){
        var item=get(id);require(item,"EXCEPTION","只有差异批次允许调整");
        if(r.matchedCount()>item.getTransactionCount())throw bad("匹配笔数不能大于交易笔数");
        item.resolve(r.ledgerAmount(),r.matchedCount(),r.unresolvedExceptions());
        var metrics=metrics(item);boolean clean=metrics.amountVariance().compareTo(metrics.tolerance())<=0
            && metrics.matchRate()>=99 && item.getUnresolvedExceptions()==0;
        item.reconcile(clean);audit("复核对账差异",item,r.remark());return new MatchResult(item,metrics);
    }

    @Transactional
    public ReconciliationBatch confirm(Long id){
        var item=get(id);require(item,"RECONCILED","仅无未决差异批次允许确认");
        if(!item.isCounterpartConfirmed())throw conflict("尚未取得结算对方确认");
        item.confirm();audit("确认结算单",item,"对方确认完成");return item;
    }

    @Transactional
    public ReconciliationBatch settle(Long id,String paymentReference){
        var item=get(id);require(item,"CONFIRMED","仅已确认结算单允许付款结算");
        if(!item.isInvoiceReady())throw conflict("发票资料不完整");
        if(!item.isBankAccountVerified())throw conflict("收款账户未验证");
        item.settle();audit("完成付款结算",item,paymentReference);return item;
    }

    public Metrics metrics(Long id){return metrics(get(id));}
    private Metrics metrics(ReconciliationBatch item){
        BigDecimal variance=item.getSourceAmount().subtract(item.getLedgerAmount()).abs();
        BigDecimal tolerance=item.getSourceAmount().multiply(new BigDecimal("0.001")).max(BigDecimal.ONE)
            .setScale(2,RoundingMode.HALF_UP);
        double rate=item.getTransactionCount()==0?100:
            Math.round(item.getMatchedCount()*10000d/item.getTransactionCount())/100d;
        return new Metrics(variance,tolerance,rate,item.getUnresolvedExceptions());
    }

    public Dashboard dashboard(){return new Dashboard(batches.count(),batches.countByState("EXCEPTION"),
        batches.countByState("RECONCILED"),batches.countByState("CONFIRMED"),batches.countByState("SETTLED"));}

    private ReconciliationBatch get(Long id){return batches.findById(id).orElseThrow(()->
        new ResponseStatusException(HttpStatus.NOT_FOUND,"对账批次不存在"));}
    private void require(ReconciliationBatch item,String state,String message){if(!state.equals(item.getState()))throw conflict(message);}
    private ResponseStatusException conflict(String message){return new ResponseStatusException(HttpStatus.CONFLICT,message);}
    private ResponseStatusException bad(String message){return new ResponseStatusException(HttpStatus.BAD_REQUEST,message);}
    private void audit(String action,ReconciliationBatch item,String detail){
        var auth=SecurityContextHolder.getContext().getAuthentication();
        audits.save(new AuditLog("RECONCILIATION",action,item.getBatchNo(),auth==null?"system":auth.getName(),detail));
    }

    public record CreateRequest(@NotBlank @Size(max=40) String batchNo,@NotBlank @Size(max=40) String organizationCode,
        @NotBlank @Size(max=100) String counterparty,@NotNull @PositiveOrZero BigDecimal sourceAmount,
        @NotNull @PositiveOrZero BigDecimal ledgerAmount,@PositiveOrZero int transactionCount,
        @PositiveOrZero int matchedCount,@PositiveOrZero int unresolvedExceptions,
        boolean counterpartConfirmed,boolean invoiceReady,boolean bankAccountVerified){}
    public record ResolveRequest(@NotNull @PositiveOrZero BigDecimal ledgerAmount,@PositiveOrZero int matchedCount,
        @PositiveOrZero int unresolvedExceptions,@NotBlank @Size(max=300) String remark){}
    public record Metrics(BigDecimal amountVariance,BigDecimal tolerance,double matchRate,int unresolvedExceptions){}
    public record MatchResult(ReconciliationBatch batch,Metrics metrics){}
    public record Dashboard(long total,long exception,long reconciled,long confirmed,long settled){}
}
