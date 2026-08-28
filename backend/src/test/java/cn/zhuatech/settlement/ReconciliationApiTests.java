/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.settlement;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.*;
import java.util.regex.Pattern;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ReconciliationApiTests {
    @Autowired MockMvc mvc;

    @Test
    void cleanBatchCompletesReconciliationConfirmationAndSettlement() throws Exception {
        long id=create("REC-DOMAIN-001",100000,99950,1000,1000,0,true,true,true);
        mvc.perform(post("/api/settlement/reconciliation/{id}/match",id)
                .with(httpBasic("operator","operator123")))
            .andExpect(status().isOk()).andExpect(jsonPath("$.data.batch.state").value("RECONCILED"))
            .andExpect(jsonPath("$.data.metrics.matchRate").value(100));
        mvc.perform(post("/api/settlement/reconciliation/{id}/confirm",id)
                .with(httpBasic("operator","operator123")))
            .andExpect(status().isOk()).andExpect(jsonPath("$.data.state").value("CONFIRMED"));
        mvc.perform(post("/api/settlement/reconciliation/{id}/settle",id)
                .param("paymentReference","PAY-2026-001").with(httpBasic("operator","operator123")))
            .andExpect(status().isOk()).andExpect(jsonPath("$.data.state").value("SETTLED"));
    }

    @Test
    void exceptionBatchRequiresAdminResolutionBeforeConfirmation() throws Exception {
        long id=create("REC-DOMAIN-EX",100000,70000,1000,700,8,true,true,true);
        mvc.perform(post("/api/settlement/reconciliation/{id}/match",id)
                .with(httpBasic("operator","operator123")))
            .andExpect(status().isOk()).andExpect(jsonPath("$.data.batch.state").value("EXCEPTION"));
        mvc.perform(post("/api/settlement/reconciliation/{id}/confirm",id)
                .with(httpBasic("operator","operator123"))).andExpect(status().isConflict());
        String resolved="{\"ledgerAmount\":100000,\"matchedCount\":1000,\"unresolvedExceptions\":0,\"remark\":\"差异凭证复核完成\"}";
        mvc.perform(post("/api/admin/settlement/reconciliation/{id}/resolve",id)
                .with(httpBasic("operator","operator123")).contentType(MediaType.APPLICATION_JSON).content(resolved))
            .andExpect(status().isForbidden());
        mvc.perform(post("/api/admin/settlement/reconciliation/{id}/resolve",id)
                .with(httpBasic("admin","admin123")).contentType(MediaType.APPLICATION_JSON).content(resolved))
            .andExpect(status().isOk()).andExpect(jsonPath("$.data.batch.state").value("RECONCILED"));
    }

    @Test
    void paymentGateRejectsMissingInvoiceOrUnverifiedAccount() throws Exception {
        long id=create("REC-DOMAIN-PAY",50000,50000,500,500,0,true,false,false);
        mvc.perform(post("/api/settlement/reconciliation/{id}/match",id)
                .with(httpBasic("operator","operator123"))).andExpect(status().isOk());
        mvc.perform(post("/api/settlement/reconciliation/{id}/confirm",id)
                .with(httpBasic("operator","operator123"))).andExpect(status().isOk());
        mvc.perform(post("/api/settlement/reconciliation/{id}/settle",id)
                .param("paymentReference","PAY-BLOCKED").with(httpBasic("operator","operator123")))
            .andExpect(status().isConflict());
        mvc.perform(get("/api/settlement/reconciliation/dashboard")
                .with(httpBasic("operator","operator123")))
            .andExpect(status().isOk()).andExpect(jsonPath("$.data.confirmed").isNumber());
    }

    @Test
    void transactionLinesAreDeduplicatedAndMatchedByExternalReference() throws Exception {
        long id=create("REC-LINES-001",300,250,3,2,1,true,true,true);
        String payload="""
            {"lines":[
              {"side":"BUSINESS","externalRef":"TX-001","amount":100,"occurredDate":"2026-08-28"},
              {"side":"LEDGER","externalRef":"TX-001","amount":100,"occurredDate":"2026-08-28"},
              {"side":"BUSINESS","externalRef":"TX-002","amount":200,"occurredDate":"2026-08-28"},
              {"side":"LEDGER","externalRef":"TX-002","amount":150,"occurredDate":"2026-08-28"}
            ]}
            """;
        mvc.perform(post("/api/settlement/reconciliation/{id}/transactions",id)
                .with(httpBasic("operator","operator123")).contentType(MediaType.APPLICATION_JSON).content(payload))
            .andExpect(status().isOk()).andExpect(jsonPath("$.data.length()").value(4));
        mvc.perform(get("/api/settlement/reconciliation/{id}/transaction-match",id)
                .with(httpBasic("operator","operator123")))
            .andExpect(status().isOk()).andExpect(jsonPath("$.data.matchedCount").value(1))
            .andExpect(jsonPath("$.data.unmatchedCount").value(1)).andExpect(jsonPath("$.data.variance").value(50));
        mvc.perform(post("/api/settlement/reconciliation/{id}/transactions",id)
                .with(httpBasic("operator","operator123")).contentType(MediaType.APPLICATION_JSON)
                .content("{\"lines\":[{\"side\":\"BUSINESS\",\"externalRef\":\"TX-001\",\"amount\":100,\"occurredDate\":\"2026-08-28\"}]}"))
            .andExpect(status().isConflict());
    }

    private long create(String no,double source,double ledger,int transactions,int matched,int exceptions,
            boolean confirmed,boolean invoice,boolean bank)throws Exception{
        var result=mvc.perform(post("/api/settlement/reconciliation")
                .with(httpBasic("operator","operator123")).contentType(MediaType.APPLICATION_JSON)
                .content("{\"batchNo\":\""+no+"\",\"organizationCode\":\"ZH-SH\","
                    +"\"counterparty\":\"知华示例供应商\",\"sourceAmount\":"+source
                    +",\"ledgerAmount\":"+ledger+",\"transactionCount\":"+transactions
                    +",\"matchedCount\":"+matched+",\"unresolvedExceptions\":"+exceptions
                    +",\"counterpartConfirmed\":"+confirmed+",\"invoiceReady\":"+invoice
                    +",\"bankAccountVerified\":"+bank+"}"))
            .andExpect(status().isOk()).andReturn();
        var matcher=Pattern.compile("\\\"id\\\":(\\d+)").matcher(result.getResponse().getContentAsString());
        Assertions.assertTrue(matcher.find());return Long.parseLong(matcher.group(1));
    }
}
