/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.settlement.service;

import cn.zhuatech.settlement.model.*;
import cn.zhuatech.settlement.repository.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.math.*;
import java.time.LocalDate;
import java.util.*;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Service
public class SettlementTransactionService {
    private final SettlementTransactionRepository transactions;private final ReconciliationBatchRepository batches;
    private final AuditLogRepository audits;
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public SettlementTransactionService(SettlementTransactionRepository transactions,
            ReconciliationBatchRepository batches,AuditLogRepository audits){
        this.transactions=transactions;this.batches=batches;this.audits=audits;
    }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public List<SettlementTransaction> list(Long batchId){requireBatch(batchId);return transactions.findByBatchIdOrderByExternalRefAsc(batchId);}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Transactional
    public List<SettlementTransaction> importLines(Long batchId,ImportRequest request){
        var batch=requireBatch(batchId);Set<String> incoming=new HashSet<>();
        for(var line:request.lines()){
            String key=line.side()+"|"+line.externalRef();
            if(!incoming.add(key)||transactions.existsByBatchIdAndSideAndExternalRef(batchId,line.side(),line.externalRef())){
                throw conflict("检测到重复流水："+key);
            }
        }
        var saved=transactions.saveAll(request.lines().stream().map(line->new SettlementTransaction(batchId,
            line.side(),line.externalRef(),line.amount(),line.occurredDate())).toList());
        audit(batch,"导入逐笔流水","导入 "+saved.size()+" 笔");return saved;
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public TransactionMatchResult match(Long batchId){
        var batch=requireBatch(batchId);Map<String,List<SettlementTransaction>> groups=new TreeMap<>();
        list(batchId).forEach(line->groups.computeIfAbsent(line.getExternalRef(),k->new ArrayList<>()).add(line));
        List<String> matched=new ArrayList<>(),unmatched=new ArrayList<>();BigDecimal variance=BigDecimal.ZERO;
        for(var entry:groups.entrySet()){
            var business=entry.getValue().stream().filter(v->"BUSINESS".equals(v.getSide())).findFirst();
            var ledger=entry.getValue().stream().filter(v->"LEDGER".equals(v.getSide())).findFirst();
            if(business.isPresent()&&ledger.isPresent()&&business.get().getAmount().compareTo(ledger.get().getAmount())==0){
                matched.add(entry.getKey());
            }else{
                unmatched.add(entry.getKey());
                var left=business.map(SettlementTransaction::getAmount).orElse(BigDecimal.ZERO);
                var right=ledger.map(SettlementTransaction::getAmount).orElse(BigDecimal.ZERO);
                variance=variance.add(left.subtract(right).abs());
            }
        }
        return new TransactionMatchResult(batch.getBatchNo(),groups.size(),matched.size(),unmatched.size(),
            variance.setScale(2,RoundingMode.HALF_UP),List.copyOf(matched),List.copyOf(unmatched));
    }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private ReconciliationBatch requireBatch(Long id){return batches.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"对账批次不存在"));}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private ResponseStatusException conflict(String message){return new ResponseStatusException(HttpStatus.CONFLICT,message);}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private void audit(ReconciliationBatch batch,String action,String detail){
        var auth=SecurityContextHolder.getContext().getAuthentication();
        audits.save(new AuditLog("RECONCILIATION",action,batch.getBatchNo(),auth==null?"system":auth.getName(),detail));
    }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record ImportRequest(@NotEmpty @Size(max=500) List<@Valid LineRequest> lines){}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record LineRequest(@NotBlank @Pattern(regexp="BUSINESS|LEDGER") String side,
        @NotBlank @Size(max=80) String externalRef,@NotNull @PositiveOrZero BigDecimal amount,
        @NotNull LocalDate occurredDate){}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record TransactionMatchResult(String batchNo,int referenceCount,int matchedCount,int unmatchedCount,
        BigDecimal variance,List<String> matchedReferences,List<String> unmatchedReferences){}
}
