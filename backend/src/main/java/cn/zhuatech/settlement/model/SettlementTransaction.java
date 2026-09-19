/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.settlement.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Entity
@Table(name="settlement_transactions",uniqueConstraints=@UniqueConstraint(columnNames={"batchId","side","externalRef"}))
public class SettlementTransaction {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private Long batchId;
    @Column(nullable=false,length=20) private String side;
    @Column(nullable=false,length=80) private String externalRef;
    @Column(nullable=false,precision=18,scale=2) private BigDecimal amount;
    @Column(nullable=false) private LocalDate occurredDate;
    private LocalDateTime createdAt;
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    protected SettlementTransaction(){}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public SettlementTransaction(Long batchId,String side,String externalRef,BigDecimal amount,LocalDate occurredDate){
        this.batchId=batchId;this.side=side;this.externalRef=externalRef;this.amount=amount;this.occurredDate=occurredDate;
    }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PrePersist void created(){createdAt=LocalDateTime.now();}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public Long getId(){return id;} /**
                                     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                     */
public Long getBatchId(){return batchId;} /**
                                                                               * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                               */
public String getSide(){return side;}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public String getExternalRef(){return externalRef;} /**
                                                         * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                         */
public BigDecimal getAmount(){return amount;}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public LocalDate getOccurredDate(){return occurredDate;} /**
                                                              * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                              */
public LocalDateTime getCreatedAt(){return createdAt;}
}
