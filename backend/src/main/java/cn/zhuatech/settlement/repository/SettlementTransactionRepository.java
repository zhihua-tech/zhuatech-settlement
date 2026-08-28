/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.settlement.repository;
import cn.zhuatech.settlement.model.SettlementTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface SettlementTransactionRepository extends JpaRepository<SettlementTransaction,Long>{
    List<SettlementTransaction> findByBatchIdOrderByExternalRefAsc(Long batchId);
    boolean existsByBatchIdAndSideAndExternalRef(Long batchId,String side,String externalRef);
}
