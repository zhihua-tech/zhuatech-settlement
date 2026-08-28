/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.settlement.service;
import jakarta.validation.constraints.*;
import org.springframework.stereotype.Service;
import java.util.*;
@Service public class DomainDecisionService {
 public DecisionResult assess(DecisionRequest request) { if(request.matchedCount()>request.transactionCount())throw new IllegalArgumentException("匹配笔数不能大于交易笔数");double variance=Math.abs(request.sourceAmount()-request.ledgerAmount());double matchRate=request.transactionCount()==0?100:request.matchedCount()*100d/request.transactionCount();double tolerance=Math.max(1,request.sourceAmount()*0.001);int score=100;List<String> actions=new ArrayList<>();if(variance>tolerance){score-=35;actions.add("处理业务源与账务金额差异");}if(matchRate<99){score-=25;actions.add("提升交易自动匹配率");}if(request.unresolvedExceptions()>0){score-=Math.min(30,request.unresolvedExceptions()*5);actions.add("关闭未解决对账差异");}if(!request.counterpartConfirmed()){score-=25;actions.add("取得结算对方确认");}if(!request.invoiceReady()){score-=30;actions.add("补齐发票资料");}if(!request.bankAccountVerified()){score-=60;actions.add("验证收款账户后再付款");}return result(score,actions,"AUTO_SETTLE","EXCEPTION_REVIEW","PAYMENT_BLOCKED",Map.of("amountVariance",variance,"tolerance",tolerance,"matchRate",matchRate,"unresolvedExceptions",request.unresolvedExceptions())); }
 private DecisionResult result(int raw,List<String> actions,String good,String warn,String bad,Map<String,Object> metrics) { int score=Math.max(0,Math.min(100,raw));String decision=score>=80?good:score>=50?warn:bad;return new DecisionResult(decision,score,metrics,List.copyOf(actions)); }
 private DecisionResult riskResult(int raw,List<String> actions,String good,String warn,String bad,Map<String,Object> metrics) { int score=Math.max(0,Math.min(100,raw));String decision=score>=70?bad:score>=40?warn:good;return new DecisionResult(decision,score,metrics,List.copyOf(actions)); }
 public record DecisionRequest(
        @NotBlank String batchNo,
        @PositiveOrZero double sourceAmount,
        @PositiveOrZero double ledgerAmount,
        @PositiveOrZero int transactionCount,
        @PositiveOrZero int matchedCount,
        @PositiveOrZero int unresolvedExceptions,
        boolean counterpartConfirmed,
        boolean invoiceReady,
        boolean bankAccountVerified) {}
 public record DecisionResult(String decision,int score,Map<String,Object> metrics,List<String> actions) {}
}
