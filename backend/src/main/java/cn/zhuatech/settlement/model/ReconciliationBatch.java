/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.settlement.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
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

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    protected ReconciliationBatch(){}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public ReconciliationBatch(String batchNo,String organizationCode,String counterparty,BigDecimal sourceAmount,
            BigDecimal ledgerAmount,int transactionCount,int matchedCount,int unresolvedExceptions,
            boolean counterpartConfirmed,boolean invoiceReady,boolean bankAccountVerified){
        this.batchNo=batchNo;this.organizationCode=organizationCode;this.counterparty=counterparty;
        this.sourceAmount=sourceAmount;this.ledgerAmount=ledgerAmount;this.transactionCount=transactionCount;
        this.matchedCount=matchedCount;this.unresolvedExceptions=unresolvedExceptions;
        this.counterpartConfirmed=counterpartConfirmed;this.invoiceReady=invoiceReady;
        this.bankAccountVerified=bankAccountVerified;this.state="DRAFT";
    }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PrePersist void created(){createdAt=updatedAt=LocalDateTime.now();}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PreUpdate void updated(){updatedAt=LocalDateTime.now();}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public void reconcile(boolean clean){state=clean?"RECONCILED":"EXCEPTION";}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public void resolve(BigDecimal ledger,int matched,int exceptions){
        ledgerAmount=ledger;matchedCount=matched;unresolvedExceptions=exceptions;
    }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public void confirm(){state="CONFIRMED";} /**
                                               * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                               */
public void settle(){state="SETTLED";}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public Long getId(){return id;} /**
                                     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                     */
public String getBatchNo(){return batchNo;}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public String getOrganizationCode(){return organizationCode;} /**
                                                                   * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                   */
public String getCounterparty(){return counterparty;}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public BigDecimal getSourceAmount(){return sourceAmount;} /**
                                                               * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                               */
public BigDecimal getLedgerAmount(){return ledgerAmount;}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public int getTransactionCount(){return transactionCount;} /**
                                                                * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                */
public int getMatchedCount(){return matchedCount;}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public int getUnresolvedExceptions(){return unresolvedExceptions;}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public boolean isCounterpartConfirmed(){return counterpartConfirmed;} /**
                                                                           * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                           */
public boolean isInvoiceReady(){return invoiceReady;}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public boolean isBankAccountVerified(){return bankAccountVerified;} /**
                                                                         * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                         */
public String getState(){return state;}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public long getVersion(){return version;} /**
                                               * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                               */
public LocalDateTime getCreatedAt(){return createdAt;}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public LocalDateTime getUpdatedAt(){return updatedAt;}
}
