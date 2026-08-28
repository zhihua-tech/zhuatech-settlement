/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.settlement.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="reconciliation_batches",uniqueConstraints=@UniqueConstraint(columnNames="batchNo"))
public class ReconciliationBatch {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false,length=40) private String batchNo;
    @Column(nullable=false,length=40) private String organizationCode;
    @Column(nullable=false,length=100) private String counterparty;
    @Column(nullable=false,precision=18,scale=2) private BigDecimal sourceAmount;
    @Column(nullable=false,precision=18,scale=2) private BigDecimal ledgerAmount;
    private int transactionCount;
    private int matchedCount;
    private int unresolvedExceptions;
    private boolean counterpartConfirmed;
    private boolean invoiceReady;
    private boolean bankAccountVerified;
    @Column(nullable=false,length=30) private String state;
    @Version private long version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    protected ReconciliationBatch(){}
    public ReconciliationBatch(String batchNo,String organizationCode,String counterparty,BigDecimal sourceAmount,
            BigDecimal ledgerAmount,int transactionCount,int matchedCount,int unresolvedExceptions,
            boolean counterpartConfirmed,boolean invoiceReady,boolean bankAccountVerified){
        this.batchNo=batchNo;this.organizationCode=organizationCode;this.counterparty=counterparty;
        this.sourceAmount=sourceAmount;this.ledgerAmount=ledgerAmount;this.transactionCount=transactionCount;
        this.matchedCount=matchedCount;this.unresolvedExceptions=unresolvedExceptions;
        this.counterpartConfirmed=counterpartConfirmed;this.invoiceReady=invoiceReady;
        this.bankAccountVerified=bankAccountVerified;this.state="DRAFT";
    }
    @PrePersist void created(){createdAt=updatedAt=LocalDateTime.now();}
    @PreUpdate void updated(){updatedAt=LocalDateTime.now();}
    public void reconcile(boolean clean){state=clean?"RECONCILED":"EXCEPTION";}
    public void resolve(BigDecimal ledger,int matched,int exceptions){
        ledgerAmount=ledger;matchedCount=matched;unresolvedExceptions=exceptions;
    }
    public void confirm(){state="CONFIRMED";} public void settle(){state="SETTLED";}

    public Long getId(){return id;} public String getBatchNo(){return batchNo;}
    public String getOrganizationCode(){return organizationCode;} public String getCounterparty(){return counterparty;}
    public BigDecimal getSourceAmount(){return sourceAmount;} public BigDecimal getLedgerAmount(){return ledgerAmount;}
    public int getTransactionCount(){return transactionCount;} public int getMatchedCount(){return matchedCount;}
    public int getUnresolvedExceptions(){return unresolvedExceptions;}
    public boolean isCounterpartConfirmed(){return counterpartConfirmed;} public boolean isInvoiceReady(){return invoiceReady;}
    public boolean isBankAccountVerified(){return bankAccountVerified;} public String getState(){return state;}
    public long getVersion(){return version;} public LocalDateTime getCreatedAt(){return createdAt;}
    public LocalDateTime getUpdatedAt(){return updatedAt;}
}
