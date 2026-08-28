/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.settlement.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;

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
    protected SettlementTransaction(){}
    public SettlementTransaction(Long batchId,String side,String externalRef,BigDecimal amount,LocalDate occurredDate){
        this.batchId=batchId;this.side=side;this.externalRef=externalRef;this.amount=amount;this.occurredDate=occurredDate;
    }
    @PrePersist void created(){createdAt=LocalDateTime.now();}
    public Long getId(){return id;} public Long getBatchId(){return batchId;} public String getSide(){return side;}
    public String getExternalRef(){return externalRef;} public BigDecimal getAmount(){return amount;}
    public LocalDate getOccurredDate(){return occurredDate;} public LocalDateTime getCreatedAt(){return createdAt;}
}
